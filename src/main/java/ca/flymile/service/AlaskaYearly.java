/**
 * Provides services for retrieving yearly flight data for Alaska flights.
 * This class handles the process of collecting flight data over a year by breaking
 * it down into monthly segments and then aggregating the results.
 */
package ca.flymile.service;

import ca.flymile.ModelAlaska30Days.MonthlyDetails;
import ca.flymile.ModelAlaska30Days.dailyCheapest;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ca.flymile.API.RequestHandlerAlaska30Days.requestHandlerAlaska30Days;
import static ca.flymile.service.Alaska30Days.getDailyCheapests;

@Component
public class AlaskaYearly {

    /**
     * Retrieves the cheapest daily flight data for an entire year from a specified origin to destination.
     *
     * @param origin The airport code of the origin location.
     * @param destination The airport code of the destination location.
     * @return A CompletableFuture that, when completed, will return a list of the cheapest daily flights for each day of the year.
     */
    public CompletableFuture<List<dailyCheapest>> getFlightDataListAlaskaYearly(String origin, String destination) {
        LocalDate date = LocalDate.now().plusDays(15);
        List<CompletableFuture<List<dailyCheapest>>> futures = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            final String start = date.toString();
            CompletableFuture<List<dailyCheapest>> future = CompletableFuture.supplyAsync(() -> getFlightDataListAlaska30Days(origin, destination, start))
                    .exceptionally(ex -> {
                        System.err.println("Error fetching data for " + start + ": " + ex.getMessage());
                        return new ArrayList<dailyCheapest>();  // Return an empty list in case of error
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

    /**
     * Retrieves the cheapest daily flight data for a 30-day period starting from a given date.
     * This is used internally to gather monthly data for the yearly aggregation.
     *
     * @param origin The airport code of the origin location.
     * @param destination The airport code of the destination location.
     * @param start The start date for the 30-day period in YYYY-MM-DD format.
     * @return A list of dailyCheapest objects representing the cheapest flights for each day of the specified period.
     */
    public List<dailyCheapest> getFlightDataListAlaska30Days(String origin, String destination, String start) {

        // Make the API request with the adjusted start date
        return getDailyCheapests(origin, destination, start);
    }


}
