package ca.flymile.service;

import ca.flymile.ModelAmerican.FlightSlices;
import ca.flymile.dtoAmerican.FlightDto;
import ca.flymile.dtoAmerican.FlightMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import static ca.flymile.API.RequestHandlerAmerican.requestHandlerAmerican;


/**
 * The AAScrapperService class provides methods to retrieve flight data.
 */
@Component
public class American {
    private static final Gson gson = new Gson();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
     * advance and there is a need to limit the number of concurrent threads to prevent resource
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
     * @return A list of flight data, each containing a list of slices representing different legs of the journey.
     *             <p>Slice represents a single flight.</p>
     *            <p>The outer list contains flights grouped by date, where each inner list represents flights for a particular date.</p>
     */

    public List<FlightDto> getFlightDataListAmerican(String origin, String destination, String start, String end, int numPassengers, boolean upperCabin) {
        LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);
        List<CompletableFuture<List<FlightDto>>> futures = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String stringDate = date.format(DATE_FORMATTER);
            CompletableFuture<List<FlightDto>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return fetchFlightDataAmerican(stringDate, origin, destination, numPassengers, upperCabin);
                } catch (Exception e) {
                    System.err.println("Error fetching flight data for " + stringDate + ": " + e.getMessage());
                    return new ArrayList<>();
                }
            }, pool);
            futures.add(future);
        }

        return futures.stream()
                .map(future -> {
                    try {
                        return future.join();
                    } catch (CompletionException e) {
                        System.err.println("Error during asynchronous operation: " + e.getCause().getMessage());
                        return new ArrayList<FlightDto>();
                    }
                })
                .filter(list -> !list.isEmpty())
                .flatMap(List::stream)  // This will flatten the list of lists into a single list
                .collect(Collectors.toList());
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
     *
     * When multiple threads execute fetchFlightData and encounter errors, leading to calls to save309ErrorToFile or saveUnknownErrorToFile,
     * several issues can occur if these methods write to the same files:
     *
     *     Race Conditions: Concurrent writes may interleave, corrupting data as threads overwrite each other's output.
     *
     *     File Locking Issues: Operating systems lock files during writing to prevent concurrent modifications, potentially leading to blocked threads or exceptions.
     *
     *     Data Inconsistency: Without proper synchronization, changes made by one thread may not be visible to others, risking data loss or inconsistency.
     *
     *     NEED TO BE FIXED  : HANNA ? OS MAY BE
     *
     *
     */

    private List<FlightDto> fetchFlightDataAmerican(String date, String origin, String destination, int numPassengers, boolean upperCabin) {

        String json = requestHandlerAmerican(date, origin, destination, numPassengers, upperCabin);
        if (json == null || json.startsWith("<")) {
            return new ArrayList<>();
        }
        FlightSlices jsonResponse = gson.fromJson(json, FlightSlices.class);
        if (jsonResponse == null) {
            return new ArrayList<>();
        }

        String error = jsonResponse.getError();
        //Can ERRor be NULL  ???????????????
        if (error == null || error.isEmpty()) {
            return jsonResponse.getSlices().stream().map(FlightMapper::toDto).collect(Collectors.toList());
        }


        return new ArrayList<>();
    }


}
