package ca.flymile.service;

import ca.flymile.ModelAlaska.FlightSlices;
import ca.flymile.dtoAlaska.FlightDto;
import ca.flymile.dtoAlaska.FlightMapper;
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

import static ca.flymile.API.RequestHandlerAlaska.requestHandlerAlaska;

@Component
public class Alaska {
    private static final Gson gson = new Gson();
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
     * @return A list of flight data, each containing a list of slices representing different legs of the journey.
     *             <p>Slice represents a single flight.</p>
     *            <p>The outer list contains flights grouped by date, where each inner list represents flights for a particular date.</p>
     */

    public List<FlightDto> getFlightDataListAlaska(String origin, String destination, String start, String end, int numPassengers) {
        LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);
        List<CompletableFuture<List<FlightDto>>> futures = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String stringDate = date.format(DATE_FORMATTER);
            CompletableFuture<List<FlightDto>> future = CompletableFuture.supplyAsync(() -> fetchFlightDataAlaska(stringDate, origin, destination, numPassengers), pool);
            futures.add(future);
        }

        // Use flatMap to flatten the list of lists into a single list
        return futures.stream()
                .map(CompletableFuture::join)
                .filter(list -> !list.isEmpty())
                .flatMap(List::stream) // Flattens the nested lists
                .collect(Collectors.toList());
    }


    /**
     * Fetches flight data for a given date, origin, destination, and number of passengers.
     *
     * @param date          The date for which flight data is requested.
     * @param origin        The origin airport code.
     * @param destination   The destination airport code.
     * @param numPassengers The number of passengers for the flight.
     * @return A list of flight slices representing the available flights for the given parameters.
    */

    /**
     * Fetches flight data for a specific route on a given date.
     *
     * @param date The date of the flight.
     * @param origin The origin airport code.
     * @param destination The destination airport code.
     * @param numPassengers The number of passengers.
     * @return A list of FlightDto objects representing the available flights, or an empty list if no flights are available or an error occurs.
     */
    private List<FlightDto> fetchFlightDataAlaska(String date, String origin, String destination, int numPassengers) {
        try {
            String json = requestHandlerAlaska(date, origin, destination, numPassengers);
            if (json == null || json.startsWith("<")) {
                return new ArrayList<>();
            }

            FlightSlices jsonResponse = gson.fromJson(json, FlightSlices.class);
            if (jsonResponse != null && jsonResponse.getSlices() != null) {
                return jsonResponse.getSlices().stream()
                        .map(FlightMapper::toDto)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new ArrayList<>();
    }
}
