package ca.flymile.controller;

import ca.flymile.Flight.FlightDto;
import ca.flymile.RedisKeyFactory.RedisKeyFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/promiseLink")
public class FlightController {

    private final StringRedisTemplate stringRedisTemplate;
    private static final Gson gson = new Gson();

    public FlightController( StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping
    public ResponseEntity<FlightDto> getFlightById(@RequestParam String airline,
                                                   @RequestParam String flightID,
                                                   @RequestParam String origin,
                                                   @RequestParam String destination,
                                                   @RequestParam int numPassengers,
                                                   @RequestParam String date,
                                                   @RequestParam(required = false) String maxStops,
                                                   @RequestParam(required = false) boolean upperCabin,
                                                   @RequestParam(required = false) boolean nonStopOnly) {
        origin = origin.toUpperCase();
        destination = destination.toUpperCase();

        String cacheKey = generateCacheKey(airline, date, origin, destination, numPassengers, maxStops, upperCabin, nonStopOnly);
        if(cacheKey == null)
        {
            return ResponseEntity.badRequest().build();
        }
        try {
            String cachedFlights = stringRedisTemplate.opsForValue().get(cacheKey);
            if (cachedFlights != null) {
                List<FlightDto> flights = gson.fromJson(cachedFlights, new TypeToken<List<FlightDto>>() {}.getType());

                return flights.stream()
                        .filter(f -> f.getFlightID().equals(flightID))
                        .findFirst()
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    private String generateCacheKey(String airline, String date, String origin, String destination,
                                    int numPassengers, String maxStops, boolean upperCabin, boolean nonStopOnly) {
        return switch (airline.toUpperCase()) {
            case "AS" -> RedisKeyFactory.generateCacheKey("AL","0", date, origin, destination, String.valueOf(numPassengers));
            case "AA" -> RedisKeyFactory.generateCacheKey("AA","0", date, origin, destination, String.valueOf(numPassengers), maxStops, upperCabin ? "1" : "0");
            case "DL" -> RedisKeyFactory.generateCacheKey("DL","0", date, origin, destination, String.valueOf(numPassengers), nonStopOnly ? "1":"0", upperCabin ? "1" : "0");
            default -> null;
        };
    }
}

