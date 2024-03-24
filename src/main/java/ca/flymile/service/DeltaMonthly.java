package ca.flymile.service;
import ca.flymile.ModelDelta.GqlSearchOffers;
import ca.flymile.ModelDelta.Info;
import ca.flymile.ModelDelta.JsonResponse;
import ca.flymile.ModelDelta.OfferSet;
import ca.flymile.dtoDelta.DtoOffers;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ca.flymile.API.RequestHandlerDeltaMonthly.requestHandlerDeltaMonthly;
import static ca.flymile.dtoDelta.DtoOffersMapper.toDto;
import static ca.flymile.service.DateHandler.currentDate;
import static ca.flymile.service.DateHandler.limitDate;
import static ca.flymile.service.DeltaYearly.DATE_FORMATTER;

@Component
public class DeltaMonthly {
    private static final Gson gson = new Gson();

    public static List<DtoOffers> getDailyCheapestInternal(String origin, String destination, String start, int numPassengers, String upperCabin) {
        String modifiedStart = start.replaceAll("-\\d{2}$", "-01");
        String json = requestHandlerDeltaMonthly(origin, destination, modifiedStart, numPassengers, upperCabin);
        if (json == null || json.startsWith("<")) {
            return new ArrayList<>();
        }

        JsonResponse jsonResponse = gson.fromJson(json, JsonResponse.class);
        if (jsonResponse == null) {
            return Collections.emptyList();
        }

        Info info = jsonResponse.getInfo();
        if (info == null) {
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

        List<DtoOffers> dtoOffers = toDto(gqlOffersSets).getDtoOffers();
        return dtoOffers != null ? dtoOffers : Collections.emptyList();
    }
    public CompletableFuture<List<DtoOffers>> getFlightDataListDeltaMonthly(String origin, String destination, String startDate, int numPassengers, String upperCabin) {
        String modifiedStart = startDate.replaceAll("-\\d{2}$", "-07");
        LocalDate date = LocalDate.parse(modifiedStart);
        List<CompletableFuture<List<DtoOffers>>> futures = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            final String start = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            CompletableFuture<List<DtoOffers>> future = CompletableFuture.supplyAsync(
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
    public List<DtoOffers> filterByDateRange(List<DtoOffers> offers) {
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

    public List<DtoOffers> filterByThisMonth(List<DtoOffers> offers, LocalDate referenceDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        offers.removeIf(offer -> {
            LocalDate offerDate = LocalDate.parse(offer.getDate(), formatter);
            return offerDate.getMonth() != referenceDate.getMonth() || offerDate.getYear() != referenceDate.getYear();
        });
        return offers;
    }
    public List<DtoOffers> removeDuplicatesDates(List<DtoOffers> offers) {
        Map<String, DtoOffers> uniqueDatesMap = new LinkedHashMap<>();

        for (DtoOffers offer : offers) {
            String date = offer.getDate();
            uniqueDatesMap.putIfAbsent(date, offer);
        }
    return new ArrayList<>(uniqueDatesMap.values());
    }



}
