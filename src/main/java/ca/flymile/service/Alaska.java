package ca.flymile.service;
import ca.flymile.Flight.FlightDto;

import ca.flymile.ModelAlaska.FlightSlices;
import ca.flymile.dtoAlaska.FlightMapper;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ca.flymile.API.RequestHandlerAlaska.requestHandlerAlaska;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.ALASKA_CODE;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.generateCacheKey;

@RequiredArgsConstructor
@Component
public class Alaska {
    private static final Gson gson = new Gson();
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(Alaska.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * Private final ExecutorService instance used for managing asynchronous tasks.*
     * An ExecutorService represents an asynchronous execution mechanism which is capable of
     * executing tasks concurrently. It provides a higher level of abstraction over managing
     * threads and executing tasks in a multithreading environment.
     * Executors.newFixedThreadPool(int n) creates a thread pool that reuses a fixed number of
     * threads operating off a shared unbounded queue. Here, the number of threads is determined
     * by Runtime.getRuntime().availableProcessors(), which returns the number of available
     * processors on the current system. This is often used as a reasonable default for the
     * number of threads in the pool, allowing efficient utilization of available CPU resources.
     * Using a fixed thread pool is suitable when the number of tasks to execute is known in
     *  advance, and there is a need to limit the number of concurrent threads to prevent resource
     * exhaustion. It provides a balance between concurrency and resource management.
     * The 'private final' modifier ensures that this ExecutorService instance cannot be
     * reassigned or modified once initialized, providing thread safety and immutability.
     */
    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param start          The start date of the travel period (format: "YYYY-MM-DD").
     * @param end            The end date of the travel period (format: "YYYY-MM-DD").
     * @param numPassengers  The number of passengers.
     * @return A list of flight data, each containing a list of slices representing the different legs of the journey.
     *             <p>Slice represents a single flight.</p>
     *            <p>The outer list contains flights grouped by date, where each inner list represents flights for a particular date.</p>
     */

    public CompletableFuture<List<FlightDto>> getFlightDataListAlaska(String origin, String destination, String start, String end, int numPassengers) {
        LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);
        List<CompletableFuture<List<FlightDto>>> futures = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String stringDate = date.format(DATE_FORMATTER);
            CompletableFuture<List<FlightDto>> future = CompletableFuture.supplyAsync(() ->
                    fetchFlightDataAlaska(stringDate, origin, destination, numPassengers), pool);
            futures.add(future);
        }

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allDone.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // This join is non-blocking since all futures are complete
                        .filter(list -> !list.isEmpty())
                        .flatMap(List::stream) // Flattens the nested lists
                        .collect(Collectors.toList())
        );
    }

    /**
     * Fetches flight data for a specific route on a given date.
     *
     * @param date The date of the flight.
     * @param origin The origin airport code.
     * @param destination The destination airport code.
     * @param numPassengers The number of passengers.
     * @return A list of FlightDto objects representing the available flights, or an empty list if no flights are available or an error occurs.
     */
    public List<FlightDto> fetchFlightDataAlaska(String date, String origin, String destination, int numPassengers) {
        try {
            String cacheKey = generateCacheKey(ALASKA_CODE,"0", date, origin, destination, String.valueOf(numPassengers));
            String cachedFlights = stringRedisTemplate.opsForValue().get(cacheKey);
            if(cachedFlights != null)
                return gson.fromJson(cachedFlights, new TypeToken<List<FlightDto>>(){});
            String json = requestHandlerAlaska(date, origin, destination, numPassengers);
            if(json == null)
                return Collections.emptyList();
            if (json.startsWith("<")) {
                LOGGER.log(Level.SEVERE, "Blocked By Alaska");
                return Collections.emptyList();
            }

            FlightSlices jsonResponse = gson.fromJson(json, FlightSlices.class);
            if (jsonResponse != null && jsonResponse.getSlices() != null) {
                List<FlightDto> res =  jsonResponse.getSlices().stream()
                        .map(FlightMapper::toDto)
                        .collect(Collectors.toList());
                stringRedisTemplate.opsForValue().set(cacheKey, gson.toJson(res), Duration.ofHours(2));
                return res;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @PreDestroy
    public void cleanUp() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
