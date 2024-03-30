package ca.flymile.service;

import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.ModelAmericalMonthly.*;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ca.flymile.API.RequestHandlerAmericanMonthly.requestHandlerAmericanMonthly;

@Component
public class AmericanMonthly {

    private static final Gson gson = new Gson();
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(AmericanMonthly.class.getName());


    public static CompletableFuture<List<DailyCheapest>> getFlightDataListAmericanMonthly(String origin, String destination, String start, int numPassengers, boolean upperCabin, String maxStops) {
        return CompletableFuture.supplyAsync(() -> {
            String json = requestHandlerAmericanMonthly(origin, destination, start, numPassengers, upperCabin, maxStops);
            if (json == null) {
                LOGGER.log(Level.SEVERE, "JSON is null, failed to fetch data.");
                return new ArrayList<>();
            } else if (json.trim().isEmpty()) {
                LOGGER.log(Level.SEVERE, "Received empty JSON response.");
                return new ArrayList<>();
            } else if (json.charAt(0) == '<') {
                LOGGER.log(Level.SEVERE, "Blocked By American Monthly, received HTML response.");
                return new ArrayList<>();
            }

            try {
                FlightData flightData = gson.fromJson(json, FlightData.class);
                if (flightData == null || flightData.getCalendarMonths() == null) {
                    return new ArrayList<>();
                }

                return processFlightData(flightData);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing flight data: " + e.getMessage(), e);
                return new ArrayList<>();
            }
        });
    }

    private static List<DailyCheapest> processFlightData(FlightData flightData) {
        List<DailyCheapest> cheapestList = new ArrayList<>();
        if (Optional.ofNullable(flightData.getError()).orElse("").isEmpty()) {
            for (CalendarMonth month : flightData.getCalendarMonths()) {
                for (Week week : month.getWeeks()) {
                    for (Day day : week.getDays()) {
                        processDay(cheapestList, day);
                    }
                }
            }
        }
        return cheapestList;
    }

    private static void processDay(List<DailyCheapest> cheapestList, Day day) {
        if (!day.isValidDay() || day.getDate() == null) {
            return;
        }

        DailyCheapest cheapest = new DailyCheapest();
        cheapest.setDate(day.getDate());

        if (day.getSolution() != null) {
            cheapest.setPoints(day.getSolution().getPerPassengerAwardPoints());
            cheapest.setCashPrice(day.getSolution().getPerPassengerSaleTotal().getAmount());
        }

        cheapestList.add(cheapest);
    }
}
