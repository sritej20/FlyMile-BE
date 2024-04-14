package ca.flymile.controller;
import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.service.DeltaYearly;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.InputValidation.InputValidation.validateOriginDestinationPassengers;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.DELTA_CODE;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.generateCacheKey;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;


/**
 * The DeltaControllerYearly class handles HTTP requests related to flight data retrieval on a yearly basis.
 */
@RestController
@RequestMapping("/flights/delta/yearly")
@CrossOrigin(origins = "*")
public class DeltaControllerYearly {

    private final DeltaYearly deltaYearly;
    private final StringRedisTemplate stringRedisTemplate;
    private static final Gson gson = new Gson();

    /**
     * Constructs a new DeltaControllerYearly with the specified service for retrieving flight data on a yearly basis.
     *
     * @param deltaYearly The service responsible for retrieving flight data on a yearly basis.
     */
    @Autowired
    public DeltaControllerYearly(DeltaYearly deltaYearly, StringRedisTemplate stringRedisTemplate) {
        this.deltaYearly = deltaYearly;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters for the entire year.
     *
     * @param departure The departure airport code.
     * @param arrival The arrival airport code.
     * @param numPassengers The number of passengers (default is 1 if not specified).
     * @return A CompletableFuture of a list of DailyCheapest objects, each representing aggregated flight data over a year.
     */
    @GetMapping
    public CompletableFuture<List<DailyCheapest>> getFlightDataListYearly(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin,
            @RequestParam(defaultValue = "false") boolean nonStopOnly

    ) {
        String origin = departure.toUpperCase();
        String destination = arrival.toUpperCase();
        // Validate the search parameters
        validateOriginDestinationPassengers(origin,destination, numPassengers);
        String cacheKey = generateCacheKey(DELTA_CODE,"1", origin, destination, String.valueOf(numPassengers), nonStopOnly ? "1" : "0", upperCabin ? "1" : "0");
        String cachedFlights = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cachedFlights != null) {
            return CompletableFuture.completedFuture(gson.fromJson(cachedFlights, new TypeToken<List<DailyCheapest>>() {}.getType()));
        }

        // Retrieve and cache the daily Cheapest List for the year

        return deltaYearly.getFlightDataListDeltaYearly(origin, destination, numPassengers, upperCabin, nonStopOnly).thenApply(flights -> {
            stringRedisTemplate.opsForValue().set(cacheKey, gson.toJson(flights), Duration.ofHours(24));
            return flights;
        });
    }
}
