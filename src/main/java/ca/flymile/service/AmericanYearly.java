package ca.flymile.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ca.flymile.DailyCheapest.DailyCheapest;
import org.springframework.stereotype.Component;

@Component
public class AmericanYearly {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static CompletableFuture<List<DailyCheapest>> getFlightDataListAmericanYearly(String origin, String destination, int numPassengers, boolean upperCabin, String maxStops) {
        LocalDate startDate = DateHandler.getCurrentDate();

        List<CompletableFuture<List<DailyCheapest>>> futures = IntStream.range(0, 12)
                .mapToObj(i -> i == 0 ? startDate : startDate.plusMonths(i).withDayOfMonth(1))
                .map(date -> date.format(DATE_FORMATTER))
                .map(date -> AmericanMonthly.getFlightDataListAmericanMonthly(origin, destination, date, numPassengers, upperCabin, maxStops))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .flatMap(future -> future.join().stream())
                        .collect(Collectors.toList()));
    }
}
