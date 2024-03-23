package ca.flymile.service;

import ca.flymile.ModelAlaskaMonthly.MonthlyDetails;
import ca.flymile.ModelAlaskaMonthly.dailyCheapest;
import com.google.gson.Gson;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import static ca.flymile.API.RequestHandlerAlaskaMonthly.requestHandlerAlaskaMonthly;


@Component
public class AlaskaMonthly {
    private static final Gson gson = new Gson();

    /**
     * Retrieves a list of flight data for Alaska Airlines over a 30-day period
     * centered around the specified start date.*
     * This method adjusts the start date to ensure it falls within a valid range:
     * not less than 15 days from today and not more than 316 days ahead.
     *
     * @param origin        The origin airport code.
     * @param destination   The destination airport code.
     * @param start         The intended start date of the travel period (format: "YYYY-MM-DD").
     * @return A list of {@link dailyCheapest} objects, each representing the cheapest flight option
     *         for a day in the specified period, including price in points, price in cash, and date.
     */
    public List<dailyCheapest> getFlightDataListAlaskaMonthly(String origin, String destination, String start,int numPassengers) {
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate today = LocalDate.now();

        // Adjust startDate if it is less than 15 days from today or more than 316 days ahead
        if (startDate.isBefore(today.plusDays(15))) {
            startDate = today.plusDays(15); // Set to 15 days from today if earlier than that
        } else if (startDate.isAfter(today.plusDays(316))) {
            startDate = today.plusDays(331).minusDays(15); // Set to 316 days ahead if later than that
        }

        // Convert the adjusted startDate back to a string for the API request
        String adjustedStart = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // Make the API request with the adjusted start date
        return getDailyCheapests(origin, destination, adjustedStart, numPassengers);
    }
    public static List<dailyCheapest> getDailyCheapests(String origin, String destination, String start, int numPassengers) {
        String json = requestHandlerAlaskaMonthly(origin, destination, start, numPassengers);
        if (json != null && json.charAt(0) != '<')
        {
            Type type = new TypeToken<MonthlyDetails>() {}.getType();

            // Deserialize the JSON response into a MonthlyDetails object
            MonthlyDetails monthlyDetails = gson.fromJson(json, type);

            if (monthlyDetails != null) {
                return monthlyDetails.shoulderDates();
            }
        }
        return new ArrayList<>();
    }
}
