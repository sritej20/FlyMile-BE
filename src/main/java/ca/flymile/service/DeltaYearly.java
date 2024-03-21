package ca.flymile.service;
import ca.flymile.ModelAmericanWeekly.dailyCheapest;
import ca.flymile.dtoDelta.DtoOffers;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static ca.flymile.service.DeltaMonthly.getDailyCheapestS;

/**
 * Handles the aggregation of weekly flight data over a year to provide yearly flight data analytics.
 */
@Component
public class DeltaYearly {



    /**
     * Fetches and processes the JSON data for the cheapest daily flights of a week.
     *
     * @param origin        The starting location for the flight.
     * @param destination   The ending location for the flight.
     * @param numPassengers The number of passengers.
     * @return A list of {@link dailyCheapest} objects if successful; otherwise, an empty list.
     */

    public CompletableFuture<List<DtoOffers>> getFlightDataListDeltaYearly(String origin, String destination, int numPassengers) {
        LocalDate date = LocalDate.now().plusDays(11);
        List<CompletableFuture<List<DtoOffers>>> futures = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            final String start = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            CompletableFuture<List<DtoOffers>> future = CompletableFuture.supplyAsync(
                    () -> getDailyCheapestS(origin, destination, start, numPassengers)
            );
            futures.add(future);
            date = date.plusMonths(1).plusDays(5);  // Adjust the date increment to ensure correct monthly progression
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)  // Join each future to get its result list
                        .flatMap(List::stream)  // Flatten the lists of DtoOffers into a single stream
                        .collect(Collectors.toList()));  // Collect the results into a single list
    }


}

