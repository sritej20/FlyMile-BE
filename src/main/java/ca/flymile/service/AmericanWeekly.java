package ca.flymile.service;

import ca.flymile.ModelAmericanWeekly.WeeklyData;
import ca.flymile.ModelAmericanWeekly.dailyCheapest;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ca.flymile.API.RequestHandlerAmericanWeekly.requestHandlerAmericanWeekly;

/**
 * Provides services related to retrieving weekly flight data for American Airlines.
 * This service class handles the communication with the American Airlines API
 * and processes the flight data for a weekly period.
 *
 * <p>It utilizes the RequestHandlerAmericanWeekly for making API requests and
 * parses the JSON response to convert it into model objects that are easier to work with
 * in the application. The main functionality is encapsulated in the
 * getFlightDataListAmericanWeekly method which fetches flight data based on the
 * provided parameters.</p>
 *
 * <p>Errors during the data fetch or parsing process are handled gracefully, returning an empty list,
 * ensuring the service's robustness and stability.
 *
 * NUmber format error is likely due to error being 309,
 * when error is 309, both cashPrice and points are null (It is a string in their API (lol))
 * if error is not 309, means cashPrice and points are not null , and can be parsed properly.
 * This save us converting json respone to string then checking for erroe and then parsing to as our requirement
 *  No more double parsing
 * </p>
 *
 * @see ca.flymile.ModelAmericanWeekly.WeeklyData
 * @see ca.flymile.ModelAmericanWeekly.dailyCheapest
 */
@Component
public class AmericanWeekly {

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     * The method fetches data for the week around the specified start date (7 days before and after).
     *
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param start          The start date of the travel period (format: "YYYY-MM-DD").
     * @param numPassengers  The number of passengers (should be between 1 and 9).
     * @return A list of {@link dailyCheapest} objects, each representing the cheapest daily flight option,
     *         including price in points, price in cash, and date. Returns an empty list if no data is found
     *         or an error occurs during data fetching or parsing.
     */
    public List<dailyCheapest> getFlightDataListAmericanWeekly(String origin, String destination, String start, int numPassengers) {
        String json = requestHandlerAmericanWeekly(origin, destination, start, numPassengers);

        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<WeeklyData>() {}.getType();
        WeeklyData weeklyData;

        try {
            weeklyData = gson.fromJson(json, type);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }

        if (weeklyData == null || "309".equals(weeklyData.getError())) {
            return Collections.emptyList();
        }

        return Optional.ofNullable(weeklyData.getDays()).orElse(Collections.emptyList());
    }

}
