package ca.flymile.controller;

import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.service.AlaskaYearly;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.google.gson.reflect.TypeToken;

import static ca.flymile.InputValidation.InputValidation.validateOriginDestinationNumPassengersAlaska;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.generateCacheKey;

@RestController
@RequestMapping("/flights/alaska/yearly")
@CrossOrigin(origins = "*")
public class AlaskaControllerYearly {

    private final AlaskaYearly alaskaYearly;
    private final StringRedisTemplate stringRedisTemplate;
    private static final Gson gson = new Gson();

    @Autowired
    public AlaskaControllerYearly(AlaskaYearly alaskaYearly, StringRedisTemplate stringRedisTemplate) {
        this.alaskaYearly = alaskaYearly;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping
    public CompletableFuture<List<DailyCheapest>> getFlightDataListYearly(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam(defaultValue = "1") int numPassengers
    ) {
        String origin = departure.toUpperCase();
        String destination = arrival.toUpperCase();

        // Validate the search parameters
        validateOriginDestinationNumPassengersAlaska(origin, destination, numPassengers);

        // Check if the data is cached
        String cacheKey = generateCacheKey("AL","1", origin, destination, String.valueOf(numPassengers));
        String cachedFlights = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cachedFlights != null) {
            return CompletableFuture.completedFuture(gson.fromJson(cachedFlights, new TypeToken<List<DailyCheapest>>() {}.getType()));
        }

        // Retrieve and cache the daily Cheapest List for the year
        return alaskaYearly.getFlightDataListAlaskaYearly(origin, destination, numPassengers).thenApply(flights -> {
            stringRedisTemplate.opsForValue().set(cacheKey, gson.toJson(flights), Duration.ofHours(24));
            return flights;
        });
    }
}
