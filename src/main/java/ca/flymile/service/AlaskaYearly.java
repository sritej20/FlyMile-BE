/**
 * Provides services for retrieving yearly flight data for Alaska flights.
 * This class handles the process of collecting flight data over a year by breaking
 * it down into monthly segments and then aggregating the results.
 */
package ca.flymile.service;

import ca.flymile.DailyCheapest.DailyCheapest;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static ca.flymile.service.AlaskaMonthly.getDailyCheapests;

@Component
public class AlaskaYearly {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(AlaskaYearly.class.getName());

    /**
     * Retrieves the cheapest daily flight data for an entire year from a specified origin to destination.
     *
     * @param origin The airport code of the origin location.
     * @param destination The airport code of the destination location.
     * @return A CompletableFuture that, when completed, will return a list of the cheapest daily flights for each day of the year.
     */
    public CompletableFuture<List<DailyCheapest>> getFlightDataListAlaskaYearly(String origin, String destination, int numPassengers) {
        LocalDate date = DateHandler.getCurrentDate().withDayOfMonth(27);
        List<CompletableFuture<List<DailyCheapest>>> futures = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            final String start = date.toString();
            CompletableFuture<List<DailyCheapest>> future = CompletableFuture.supplyAsync(() -> getDailyCheapests(origin, destination, start,numPassengers))
                    .exceptionally(ex -> {
                        LOGGER.log(Level.SEVERE, "Error fetching data for " + start + ": " + ex.getMessage(), ex);
                        return new ArrayList<>();  // Return an empty list in case of error
                    });
            futures.add(future);
            date = date.plusMonths(1);  // Increment by one month instead of 31 days
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
    }

}
