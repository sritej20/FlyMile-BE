package ca.flymile.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ca.flymile.service.AmericanMonthly.getFlightDataListAmericanMonthly;
import ca.flymile.ModelAmericalMonthly.dailyCheapest;

/**
 * Handles the aggregation of weekly flight data over a year to provide yearly flight data analytics.
 */
@Component
public class AmericanYearly {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Retrieves flight data for a given origin and destination over a year, broken down into weekly segments.
     *
     * @param origin        The starting location for the flight.
     * @param destination   The ending location for the flight.
     * @param numPassengers The number of passengers.
     * @param upperCabin    Specifies if the cabin class is to be considered in the search.
     * @return A {@link CompletableFuture} containing a list of the cheapest daily flights over the year.
     */
    public CompletableFuture<List<dailyCheapest>> getFlightDataListAmericanYearly(String origin, String destination, int numPassengers, boolean upperCabin) {
        LocalDate startDate = LocalDate.now();

        List<CompletableFuture<List<dailyCheapest>>> futures = IntStream.range(0, 12)
                .mapToObj(i -> i == 0 ? startDate : startDate.plusMonths(i).withDayOfMonth(1))
                .map(date -> date.format(DATE_FORMATTER))
                .map(date -> getFlightDataListAmericanMonthlyAsync(origin, destination, numPassengers, upperCabin, date))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .flatMap(future -> future.join().stream())
                        .collect(Collectors.toList()));
    }

    private CompletableFuture<List<dailyCheapest>> getFlightDataListAmericanMonthlyAsync(String origin, String destination, int numPassengers, boolean upperCabin, String startDate) {
        return CompletableFuture.supplyAsync(() -> getFlightDataListAmericanMonthly(origin, destination, startDate, numPassengers, upperCabin), executor);
    }
}
