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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ca.flymile.API.RequestHandlerDeltaMonthly.requestHandlerDeltaMonthly;
import static ca.flymile.dtoDelta.DailyCheapestMapper.toDto;
import static ca.flymile.service.DateHandler.currentDate;
import static ca.flymile.service.DateHandler.limitDate;


@Component
public class DeltaYearly {
    private static final Gson gson = new Gson();
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(DeltaYearly.class.getName());


    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d");

    public CompletableFuture<List<DailyCheapest>> getFlightDataListDeltaYearly(String origin, String destination, int numPassengers, String upperCabin) {
        LocalDate date = DateHandler.getCurrentDate();
        List<CompletableFuture<List<DailyCheapest>>> futures = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            final String start = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            CompletableFuture<List<DailyCheapest>> future = CompletableFuture.supplyAsync(
                    () -> getDailyCheapestS(origin, destination, start, numPassengers, upperCabin)
            );
            futures.add(future);
            date = date.plusDays(35);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .thenApply(this::filterByDateRange);
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
    public static List<DailyCheapest> getDailyCheapestS(String origin, String destination, String start, int numPassengers, String upperCabin) {
        String json = requestHandlerDeltaMonthly(origin, destination, start, numPassengers, upperCabin);
        if (json == null) {
            LOGGER.log(Level.SEVERE, "JSON is null, failed to fetch Delta monthly data.");
            return Collections.emptyList();
        } else if (json.startsWith("<")) {
            LOGGER.log(Level.SEVERE, "Blocked by Delta, received HTML response.");
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
            LOGGER.log(Level.SEVERE, "GqlSearchOffers is null in Info.");
            return Collections.emptyList();
        }

        List<OfferSet> gqlOffersSets = gqlSearchOffers.getGqlOffersSets();
        if (gqlOffersSets == null) {
            LOGGER.log(Level.SEVERE, "GqlOffersSets is null in GqlSearchOffers.");
            return Collections.emptyList();
        }

        List<DailyCheapest> dtoOffers = toDto(gqlOffersSets).getDailyCheapest();
        if (dtoOffers == null) {
            LOGGER.log(Level.SEVERE, "DtoOffers is null after mapping from GqlOffersSets.");
            return Collections.emptyList();
        }
        return dtoOffers;
    }

}
