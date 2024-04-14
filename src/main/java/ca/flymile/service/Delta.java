package ca.flymile.service;

import ca.flymile.Flight.FlightDto;
import ca.flymile.ModelDelta.Root;

import ca.flymile.dtoDelta.FlightMapper;
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
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.flymile.API.RequestHandlerDelta.requestHandlerDelta;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.DELTA_CODE;
import static ca.flymile.RedisKeyFactory.RedisKeyFactory.generateCacheKey;

@Component
@RequiredArgsConstructor
public class Delta {
    private final StringRedisTemplate stringRedisTemplate;
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(Delta.class.getName());

    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Gson gson = new Gson();
    private List<FlightDto> fetchFlightDataDelta(String date, String origin, String destination, int numPassengers, boolean upperCabin, boolean nonStopOnly) {
        try {
            String cacheKey = generateCacheKey(DELTA_CODE,"0",date, origin, destination,String.valueOf(numPassengers), nonStopOnly ? "1":"0", upperCabin ? "1" : "0");
            String cachedFlights = stringRedisTemplate.opsForValue().get(cacheKey);
            if(cachedFlights != null)
                return gson.fromJson(cachedFlights, new TypeToken<List<FlightDto>>(){});
            String json = requestHandlerDelta(origin, destination, date, numPassengers, upperCabin, nonStopOnly);
            if (json == null) {
                LOGGER.log(Level.SEVERE, "Failed to fetch Delta flight data: JSON is null.");
                return Collections.emptyList();
            } else if (json.startsWith("<")) {
                LOGGER.log(Level.SEVERE, "Blocked By Delta, received HTML response.");
                return Collections.emptyList();
            }
            Root root = gson.fromJson(json, Root.class);
            if (root != null && root.getData() != null) {
                List<FlightDto> res =  root.getData().getGqlSearchOffers().getGqlOffersSets().stream()
                        .map(FlightMapper::toDto)
                        .collect(Collectors.toList());
                stringRedisTemplate.opsForValue().set(cacheKey, gson.toJson(res), Duration.ofHours(2));
                return res;
            }
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "Error fetching Delta flight data: Error fetching Delta flight data: Cannot invoke ca.flymile.ModelDelta.GqlSearchOffers.getGqlOffersSets() because the return value of ca.flymile.ModelDelta.Data.getGqlSearchOffers() is null");
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching Delta flight data: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
    public CompletableFuture<List<FlightDto>> getFlightDataListDelta(
            String origin, String destination, String start, String end, int numPassengers, boolean upperCabin, boolean nonStopOnly) {

        LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);
        List<CompletableFuture<List<FlightDto>>> futures = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            final String stringDate = date.format(DATE_FORMATTER);
            CompletableFuture<List<FlightDto>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return fetchFlightDataDelta(stringDate, origin, destination, numPassengers, upperCabin, nonStopOnly);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error fetching flight data for " + stringDate + ": " + e.getMessage(), e);
                    return Collections.emptyList();
                }
            }, pool);
            futures.add(future);
        }

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allDone.thenApply(v -> {
            // Use Stream.concat to create a.json Stream<List<FlightDto>> and collect into a.json List<FlightDto>
            Stream<List<FlightDto>> listStream = futures.stream()
                    .map(future -> {
                        try {
                            return future.join();
                        } catch (CompletionException e) {
                            LOGGER.log(Level.SEVERE, "Unexpected error during asynchronous operation: " + e.getCause().getMessage(), e);
                            return Collections.emptyList();
                        }
                    });
            return listStream.flatMap(List::stream)
                    .collect(Collectors.toList());
        });
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
