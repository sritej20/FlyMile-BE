package ca.flymile.service;
import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.ModelDeltaMonthly.GqlSearchOffers;
import ca.flymile.ModelDeltaMonthly.Info;
import ca.flymile.ModelDeltaMonthly.JsonResponse;
import ca.flymile.ModelDeltaMonthly.OfferSet;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ca.flymile.API.RequestHandlerDeltaMonthly.requestHandlerDeltaMonthly;
import static ca.flymile.dtoDelta.DailyCheapestMapper.toDto;
import static ca.flymile.service.DateHandler.currentDate;
import static ca.flymile.service.DateHandler.limitDate;
import static ca.flymile.service.DeltaYearly.DATE_FORMATTER;

@Component
public class DeltaMonthly {
    private static final Gson gson = new Gson();
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(Delta.class.getName());


    public static List<DailyCheapest> getDailyCheapestInternal(String origin, String destination, String start, int numPassengers, boolean upperCabin) {
        String modifiedStart = start.replaceAll("-\\d{2}$", "-01");
        String json = requestHandlerDeltaMonthly(origin, destination, modifiedStart, numPassengers, upperCabin);
        if (json == null) {
            LOGGER.log(Level.SEVERE, "JSON is null, failed to fetch Delta monthly data.");
            return Collections.emptyList();
        } else if (json.startsWith("<")) {
            LOGGER.log(Level.SEVERE, "Blocked by Delta Monthly, received HTML response.");
            return Collections.emptyList();
        }

        JsonResponse jsonResponse = gson.fromJson(json, JsonResponse.class);
        if (jsonResponse == null) {
            LOGGER.log(Level.SEVERE, "Failed to parse JSON into JsonResponse.");
            return Collections.emptyList();
        }

        Info info = jsonResponse.getInfo();
        if (info == null) {
            LOGGER.log(Level.SEVERE, "Info is null in JsonResponse.");
            return Collections.emptyList();
        }

        GqlSearchOffers gqlSearchOffers = info.getGqlSearchOffers();
        if (gqlSearchOffers == null) {
            return Collections.emptyList();
        }

        List<OfferSet> gqlOffersSets = gqlSearchOffers.getGqlOffersSets();
        if (gqlOffersSets == null) {
            return Collections.emptyList();
        }

        List<DailyCheapest> dtoOffers = toDto(gqlOffersSets).getDailyCheapest();
        if (dtoOffers == null) {
            LOGGER.log(Level.SEVERE, "DtoOffers is null after mapping from GqlOffersSets.");
            return Collections.emptyList();
        }
        return dtoOffers;
    }
    public CompletableFuture<List<DailyCheapest>> getFlightDataListDeltaMonthly(String origin, String destination, String startDate, int numPassengers, boolean upperCabin) {
        String modifiedStart = startDate.replaceAll("-\\d{2}$", "-07");
        LocalDate date = LocalDate.parse(modifiedStart);
        List<CompletableFuture<List<DailyCheapest>>> futures = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            final String start = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            CompletableFuture<List<DailyCheapest>> future = CompletableFuture.supplyAsync(
                    () -> getDailyCheapestInternal(origin, destination, start, numPassengers, upperCabin)
            );
            futures.add(future);
            date = date.plusDays(35);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .thenApply(allOffers -> filterByThisMonth(allOffers, LocalDate.parse(startDate)))
                .thenApply(this::filterByDateRange)
                .thenApply(this::removeDuplicatesDates);
    }
    public List<DailyCheapest> filterByDateRange(List<DailyCheapest> offers) {
        int startIndex = 0;
        while (startIndex < offers.size() && LocalDate.parse(offers.get(startIndex).getDate(), DATE_FORMATTER).isBefore(currentDate)) {
            startIndex++;
        }

        int endIndex = offers.size();
        while (endIndex > startIndex && LocalDate.parse(offers.get(endIndex - 1).getDate(), DATE_FORMATTER).isAfter(limitDate)) {
            endIndex--;
        }
        return offers.subList(startIndex, endIndex);
    }

    public List<DailyCheapest> filterByThisMonth(List<DailyCheapest> offers, LocalDate referenceDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        offers.removeIf(offer -> {
            LocalDate offerDate = LocalDate.parse(offer.getDate(), formatter);
            return offerDate.getMonth() != referenceDate.getMonth() || offerDate.getYear() != referenceDate.getYear();
        });
        return offers;
    }
    public List<DailyCheapest> removeDuplicatesDates(List<DailyCheapest> offers) {
        Map<String, DailyCheapest> uniqueDatesMap = new LinkedHashMap<>();

        for (DailyCheapest offer : offers) {
            String date = offer.getDate();
            uniqueDatesMap.putIfAbsent(date, offer);
        }
    return new ArrayList<>(uniqueDatesMap.values());
    }



}
