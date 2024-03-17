package ca.flymile.service;

import ca.flymile.ModelAmerican.FlightSlices;
import ca.flymile.dtoAmerican.FlightDto;
import ca.flymile.dtoAmerican.FlightMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ca.flymile.API.RequestHandlerAmerican.requestHandlerAmerican;
import static ca.flymile.requestErrorFileLoggers.ErrorLoggers.save309ErrorToFile;
import static ca.flymile.requestErrorFileLoggers.ErrorLoggers.saveUnknownErrorToFile;

/**
 * The AAScrapperService class provides methods to retrieve flight data.
 */
@Component
public class American {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * Private final ExecutorService instance used for managing asynchronous tasks.
     *
     * An ExecutorService represents an asynchronous execution mechanism which is capable of
     * executing tasks concurrently. It provides a higher level of abstraction over managing
     * threads and executing tasks in a multithreaded environment.
     *
     * Executors.newFixedThreadPool(int n) creates a thread pool that reuses a fixed number of
     * threads operating off a shared unbounded queue. Here, the number of threads is determined
     * by Runtime.getRuntime().availableProcessors(), which returns the number of available
     * processors on the current system. This is often used as a reasonable default for the
     * number of threads in the pool, allowing efficient utilization of available CPU resources.
     *
     * Using a fixed thread pool is suitable when the number of tasks to execute is known in
     * advance and there is a need to limit the number of concurrent threads to prevent resource
     * exhaustion. It provides a balance between concurrency and resource management.
     *
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

    public List<List<FlightDto>> getFlightDataListAmerican(String origin, String destination, String start, String end, int numPassengers, boolean upperCabin) {
        // Parse start and end dates
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        // List to hold CompletableFuture for each day's flight data
        List<CompletableFuture<List<FlightDto>>> futures = new ArrayList<>();

        // Iterate over each day in the date range
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // Format date as string
            String stringDate = date.format(DATE_FORMATTER);
            // Fetch flight data asynchronously and add CompletableFuture to list
            CompletableFuture<List<FlightDto>> future = CompletableFuture.supplyAsync(() -> fetchFlightDataAmerican(stringDate, origin, destination, numPassengers, upperCabin), pool);
            futures.add(future);
        }

        // Join CompletableFuture and collect non-empty results into FlightSlicesByDate

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(list -> !list.isEmpty())
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
        // Perform HTTP request to obtain flight data in JSON format
        String json = requestHandlerAmerican(date, origin, destination, numPassengers, upperCabin);

        // Deserialize JSON response into FlightSlices object using Gson library
        Gson gson = new Gson();
        FlightSlices jsonResponse = gson.fromJson(json, FlightSlices.class);

        // Check if the response contains an error
        String error = jsonResponse.getError();
        switch (error) {
            // If no error, return the list of flight slices
            case "" -> {
                return jsonResponse.getSlices().stream().map(FlightMapper::toDto).collect(Collectors.toList());
            }
            // If error code 309 (example), save the error details to a file
            case "309" -> save309ErrorToFile("309", origin, destination, date, numPassengers, upperCabin);

            // For any other error, save the error details to a different file
            default ->
                    saveUnknownErrorToFile(jsonResponse.getError(), origin, destination, date, numPassengers, upperCabin);
        }

        // Return an empty list if there's an error
        return new ArrayList<>();
    }

}
