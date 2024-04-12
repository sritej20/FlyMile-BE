package ca.flymile.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ca.flymile.DailyCheapest.DailyCheapest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class AmericanYearly {

    private final AmericanMonthly americanMonthly;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public  CompletableFuture<List<DailyCheapest>> getFlightDataListAmericanYearly(String origin, String destination, int numPassengers, boolean upperCabin, String maxStops) {
        LocalDate startDate = DateHandler.getCurrentDate();

        // Create a list of CompletableFuture wrapping the synchronous call to getFlightDataListAmericanMonthly
        List<CompletableFuture<List<DailyCheapest>>> futures = IntStream.range(0, 12)
                .mapToObj(i -> i == 0 ? startDate : startDate.plusMonths(i).withDayOfMonth(1))
                .map(date -> date.format(DATE_FORMATTER))
                .map(date -> CompletableFuture.supplyAsync(() ->
                        americanMonthly.getFlightDataListAmericanMonthly(origin, destination, date, numPassengers, upperCabin, maxStops)))
                .toList();

        // Combine all futures into one CompletableFuture that will complete when all futures are complete
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .flatMap(future -> future.join().stream())
                        .collect(Collectors.toList()));
    }
}
