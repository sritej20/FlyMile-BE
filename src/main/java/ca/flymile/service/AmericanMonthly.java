package ca.flymile.service;

import ca.flymile.ModelAmericalMonthly.*;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.flymile.API.RequestHandlerAmericanMonthly.requestHandlerAmericanMonthly;

@Component
public class AmericanMonthly {

    private static final Gson gson = new Gson();

    public static List<dailyCheapest> getFlightDataListAmericanMonthly(String origin, String destination, String start, int numPassengers, boolean upperCabin) {
        String json = requestHandlerAmericanMonthly(origin, destination, start, numPassengers, upperCabin);
        if (json == null || json.trim().isEmpty() || json.charAt(0) == '<') {
            return new ArrayList<>(); // Early return on invalid or empty JSON
        }

        try {
            FlightData flightData = gson.fromJson(json, FlightData.class);
            if (flightData == null || flightData.getCalendarMonths() == null) {
                return new ArrayList<>(); // Early return on invalid flight data
            }

            return processFlightData(flightData);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static List<dailyCheapest> processFlightData(FlightData flightData) {
        List<dailyCheapest> cheapestList = new ArrayList<>();
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

    private static void processDay(List<dailyCheapest> cheapestList, Day day) {
        if (!day.isValidDay() || day.getDate() == null) {
            return;
        }

        dailyCheapest cheapest = new dailyCheapest();
        cheapest.setDate(day.getDate());

        // Set values from solution if available, otherwise use zero
        if (day.getSolution() != null) {
            cheapest.setPoints(day.getSolution().getPerPassengerAwardPoints());
            cheapest.setCashPrice(day.getSolution().getPerPassengerSaleTotal().getAmount());
        }

        cheapestList.add(cheapest);
    }

}
