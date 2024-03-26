package ca.flymile.service;

import ca.flymile.Flight.FlightDto;
import ca.flymile.ModelDelta.Root;

import ca.flymile.dtoDelta.FlightMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.flymile.API.RequestHandlerDelta.requestHandlerDelta;
@Component
public class Delta {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(Delta.class.getName());

    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Gson gson = new Gson();
    private List<FlightDto> fetchFlightDataDelta(String date, String origin, String destination, int numPassengers, boolean upperCabin) {
        try {
            String json = requestHandlerDelta(origin, destination, date, numPassengers, upperCabin);
            if (json == null) {
                LOGGER.log(Level.SEVERE, "Failed to fetch Delta flight data: JSON is null.");
                return new ArrayList<>();
            } else if (json.startsWith("<")) {
                LOGGER.log(Level.SEVERE, "Blocked By Delta, received HTML response.");
                return new ArrayList<>();
            }

            Root root = gson.fromJson(json, Root.class);
            if (root != null && root.getData() != null) {
                return root.getData().getGqlSearchOffers().getGqlOffersSets().stream()
                        .map(FlightMapper::map)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching Delta flight data: " + e.getMessage(), e);
        }
        return new ArrayList<>();
    }
    public CompletableFuture<List<FlightDto>> getFlightDataListDelta(
            String origin, String destination, String start, String end, int numPassengers, boolean upperCabin) {

        LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);
        List<CompletableFuture<List<FlightDto>>> futures = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            final String stringDate = date.format(DATE_FORMATTER);
            CompletableFuture<List<FlightDto>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return fetchFlightDataDelta(stringDate, origin, destination, numPassengers, upperCabin);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error fetching flight data for " + stringDate + ": " + e.getMessage(), e);
                    return Collections.emptyList();
                }
            }, pool);
            futures.add(future);
        }

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allDone.thenApply(v -> {
            // Use Stream.concat to create a Stream<List<FlightDto>> and collect into a List<FlightDto>
            Stream<List<FlightDto>> listStream = futures.stream()
                    .map(future -> {
                        try {
                            return future.join();
                        } catch (CompletionException e) {
                            LOGGER.log(Level.SEVERE, "Unexpected error during asynchronous operation: " + e.getCause().getMessage(), e);
                            return Collections.emptyList();
                        }
                    });
            return listStream.flatMap(List::stream)
                    .collect(Collectors.toList());
        });
    }


}
