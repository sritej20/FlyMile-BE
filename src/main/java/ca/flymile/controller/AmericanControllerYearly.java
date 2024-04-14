package ca.flymile.controller;


import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.ModelAmericalMonthly.dailyCheapest;
import ca.flymile.service.AmericanYearly;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.InputValidation.InputValidation.*;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.AMERICAN_CODE;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.generateCacheKey;

/**
 * The AmericanControllerYearly class handles HTTP requests related to flight data retrieval on a.json Yearly basis from the American Airlines website.
 */

@RestController
@RequestMapping("/flights/american/yearly")
@CrossOrigin(origins = "*")
public class AmericanControllerYearly {

    private final AmericanYearly americanYearly;
    private final StringRedisTemplate stringRedisTemplate;
    private static final Gson gson = new Gson();

    /**
     * Constructs a.json new AmericanControllerYearly with the specified service for American Airlines Yearly cheapest price & point combo.
     *
     * @param americanYearly The service responsible for retrieving flight data from American Airlines for the Yearly cheapest price & point combo.
     */
    @Autowired
    public AmericanControllerYearly(AmericanYearly americanYearly, StringRedisTemplate stringRedisTemplate) {
        this.americanYearly = americanYearly;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * Retrieves a.json list of flight data based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @return A list of DailyCheapest objects, each representing a.json date with available pricing details within a.json Yearly period.
     */
    @GetMapping
    public CompletableFuture<List<DailyCheapest>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin,
            @RequestParam(required = false, defaultValue = "3") String maxStops
    ) {
        String origin = departure.toUpperCase();
        String destination = arrival.toUpperCase();
        // Validate the search parameters
        validateOriginDestinationPassengers(origin, destination,numPassengers);
        String stops = parseAndValidateStops(maxStops);

        // Check if the data is cached
        String cacheKey = generateCacheKey(AMERICAN_CODE,"1", origin, destination, String.valueOf(numPassengers), stops, upperCabin ? "1" : "0");
        String cachedFlights = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cachedFlights != null) {
            return CompletableFuture.completedFuture(gson.fromJson(cachedFlights, new TypeToken<List<DailyCheapest>>() {}.getType()));
        }

        // Retrieve and cache the daily Cheapest List for the year

        return americanYearly.getFlightDataListAmericanYearly(origin, destination, numPassengers, upperCabin, stops).thenApply(flights -> {
            stringRedisTemplate.opsForValue().set(cacheKey, gson.toJson(flights), Duration.ofHours(24));
            return flights;
        });
    }
}
