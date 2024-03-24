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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ca.flymile.API.RequestHandlerDeltaMonthly.requestHandlerDeltaMonthly;
import static ca.flymile.dtoDelta.DtoOffersMapper.toDto;
import static ca.flymile.service.DateHandler.currentDate;
import static ca.flymile.service.DateHandler.limitDate;


@Component
public class DeltaYearly {
    private static final Gson gson = new Gson();

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d");

    public CompletableFuture<List<DtoOffers>> getFlightDataListDeltaYearly(String origin, String destination, int numPassengers, String upperCabin) {
        LocalDate date = LocalDate.now();
        List<CompletableFuture<List<DtoOffers>>> futures = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            final String start = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            CompletableFuture<List<DtoOffers>> future = CompletableFuture.supplyAsync(
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
    public static List<DtoOffers> getDailyCheapestS(String origin, String destination, String start, int numPassengers, String upperCabin) {
        String json = requestHandlerDeltaMonthly(origin, destination, start, numPassengers, upperCabin);
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
}
