package ca.flymile.service;

import ca.flymile.Flight.FlightDto;
import ca.flymile.ModelAmerican.FlightSlices;
import ca.flymile.dtoAmerican.FlightMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static ca.flymile.API.RequestHandlerAmerican.requestHandlerAmerican;



/**
 * The AAScrapperService class provides methods to retrieve flight data.
 */
@RequiredArgsConstructor
@Component
public class American {
    private static final Gson gson = new Gson();
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(American.class.getName());

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final StringRedisTemplate stringRedisTemplate;
    /**
     * Private final ExecutorService instance used for managing asynchronous tasks.
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


    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param start          The start date of the travel period (format: "YYYY-MM-DD").
     * @param end            The end date of the travel period (format: "YYYY-MM-DD").
     * @param numPassengers  The number of passengers.
     * @param upperCabin     Indicates if upper cabin (Business/First) seats are preferred.
     * @return A list of flight data, each containing a list of slices representing the different legs of the journey.
     *             <p>Slice represents a single flight.</p>
     *            <p>The outer list contains flights grouped by date, where each inner list represents flights for a particular date.</p>
     */

    public CompletableFuture<List<FlightDto>> getFlightDataListAmerican(String origin, String destination, String start, String end, int numPassengers, boolean upperCabin, String maxStops) {
        LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);
        List<CompletableFuture<List<FlightDto>>> futures = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String stringDate = date.format(DATE_FORMATTER);
            CompletableFuture<List<FlightDto>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return fetchFlightDataAmerican(stringDate, origin, destination, numPassengers, upperCabin, maxStops);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error during asynchronous operation: " + e.getCause().getMessage(), e);
                    return Collections.emptyList();
                }
            }, pool);
            futures.add(future);
        }

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allDone.thenApply(v ->
                futures.stream()
                        .map(future -> {
                            try {
                                return future.join();
                            } catch (CompletionException e) {
                                LOGGER.log(Level.SEVERE, "Error during asynchronous operation: " + e.getCause().getMessage(), e);
                                return new ArrayList<FlightDto>();
                            }
                        })
                        .filter(list -> !list.isEmpty())
                        .flatMap(List::stream) // This will flatten the list of lists into a single list
                        .collect(Collectors.toList())
        );
    }



    /**
     * Fetches flight data for a given date, origin, destination, number of passengers, and cabin preference.
     *
     * @param date          The date for which flight data is requested.
     * @param origin        The origin airport code.
     * @param destination   The destination airport code.
     * @param numPassengers The number of passengers for the flight.
     * @param upperCabin    Whether the search should include upper cabin flights.
     * @return A list of flight slices representing the available flights for the given parameters.
     * When multiple threads execute fetchFlightData and encounter errors, leading to calls to save309ErrorToFile or saveUnknownErrorToFile,
     * several issues can occur if these methods write to the same files:
     *     Race Conditions: Concurrent writes may interleave, corrupting data as threads overwrite each other's output.
     *     File Locking Issues: Operating systems lock files during writing to prevent concurrent modifications, potentially leading to blocked threads or exceptions.
     *     Data Inconsistency: Without proper synchronization, changes made by one thread may not be visible to others, risking data loss or inconsistency.
     *
     *     NEED TO BE FIXED: HANNA? OS MAY BE
     *
     *
     */

    private List<FlightDto> fetchFlightDataAmerican(String date, String origin, String destination, int numPassengers, boolean upperCabin , String maxStops) {
        String cacheKey = generateCacheKey(date, origin, destination, numPassengers);
        String cachedFlights = stringRedisTemplate.opsForValue().get(cacheKey);
        if(cachedFlights != null)
            return gson.fromJson(cachedFlights, new TypeToken<List<FlightDto>>(){});
        String json = requestHandlerAmerican(date, origin, destination, numPassengers, upperCabin, maxStops);
        if (json == null) {
            LOGGER.log(Level.SEVERE, "JSON is null, failed to fetch data.");
            return Collections.emptyList();
        } else if (json.startsWith("<")) {
            LOGGER.log(Level.SEVERE, "Blocked By American, received HTML response.");
            return Collections.emptyList();
        }

        FlightSlices jsonResponse = gson.fromJson(json, FlightSlices.class);
        if (jsonResponse == null) {
            return Collections.emptyList();
        }

        String error = jsonResponse.getError();
        if (error == null || error.isEmpty()) {
            List<FlightDto>  res = jsonResponse.getSlices().stream().map(FlightMapper::toDto).collect(Collectors.toList());
            stringRedisTemplate.opsForValue().set(cacheKey, gson.toJson(res));
            return res;
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
    private String generateCacheKey(String date, String origin, String destination, int numPassengers) {
        return String.format("AA:%s:%s:%s:%d", date, origin, destination, numPassengers);
    }


}
