package ca.flymile.service;

import ca.flymile.dtoAmerican.AmericanDailyCheapestDto;
import ca.flymile.ModelAmericanWeekly.WeeklyData;
import ca.flymile.ModelAmericanWeekly.dailyCheapest;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ca.flymile.API.RequestHandlerAmericanWeekly.requestHandlerAmericanWeekly;

/**
 * Handles the aggregation of weekly flight data over a year to provide yearly flight data analytics.
 */
@Component
public class AmericanYearly {

    /**
     * Retrieves flight data for a given origin and destination over a year, broken down into weekly segments.
     *
     * @param origin        The starting location for the flight.
     * @param destination   The ending location for the flight.
     * @param numPassengers The number of passengers.
     * @param cabin         Specifies if the cabin class is to be considered in the search.
     * @return A {@link CompletableFuture} containing a list of the cheapest daily flights over the year.
     */
    public CompletableFuture<List<AmericanDailyCheapestDto>> getFlightDataListAmericanYearly(String origin, String destination, int numPassengers, boolean cabin) {
        LocalDate date = LocalDate.now().plusDays(6);
        List<CompletableFuture<List<dailyCheapest>>> futures = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            final String start = date.toString();
            CompletableFuture<List<dailyCheapest>> future = CompletableFuture.supplyAsync(() -> getFlightDataListAmericanWeekly(origin, destination, start, numPassengers, cabin));
            futures.add(future);
            date = date.plusDays(13);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .map(AmericanDailyCheapestDto :: toDto)
                        .collect(Collectors.toList()));
    }

    /**
     * Retrieves a list of the cheapest flights for a specific week.
     *
     * @param origin        The starting location for the flight.
     * @param destination   The ending location for the flight.
     * @param start         The start date for the weekly data.
     * @param numPassengers The number of passengers.
     * @param cabin         Indicates if the cabin class should be considered.
     * @return A list of {@link dailyCheapest} objects representing the cheapest flights for the week.
     */
    public List<dailyCheapest> getFlightDataListAmericanWeekly(String origin, String destination, String start, int numPassengers, boolean cabin) {
        return getDailyCheapestS(origin, destination, start, numPassengers, cabin);
    }

    /**
     * Fetches and processes the JSON data for the cheapest daily flights of a week.
     *
     * @param origin        The starting location for the flight.
     * @param destination   The ending location for the flight.
     * @param start         The starting date for the query.
     * @param numPassengers The number of passengers.
     * @param cabin         Indicates if the cabin class should be considered.
     * @return A list of {@link dailyCheapest} objects if successful; otherwise, an empty list.
     */
    public static List<dailyCheapest> getDailyCheapestS(String origin, String destination, String start, int numPassengers, boolean cabin) {
        String json = requestHandlerAmericanWeekly(origin, destination, start, numPassengers, cabin);
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<WeeklyData>() {}.getType();
        WeeklyData weeklyData = gson.fromJson(json, type);
        if (weeklyData == null) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(weeklyData.getDays()).orElse(Collections.emptyList());
    }
}

