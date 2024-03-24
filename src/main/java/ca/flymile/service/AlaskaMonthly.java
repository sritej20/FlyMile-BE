package ca.flymile.service;

import ca.flymile.ModelAlaskaMonthly.MonthlyDetails;
import ca.flymile.ModelAlaskaMonthly.dailyCheapest;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static ca.flymile.API.RequestHandlerAlaskaMonthly.requestHandlerAlaskaMonthly;
import static ca.flymile.service.DateHandler.currentDate;
import static ca.flymile.service.DateHandler.limitDate;


@Component
public class AlaskaMonthly {
    private static final Gson gson = new Gson();


    /**
     * Retrieves a list of flight data for Alaska Airlines over a 30-day period
     * centered around the specified start date.*
     * This method adjusts the start date to ensure it falls within a valid range:
     * not less than 15 days from today and not more than 316 days ahead.
     *
     * @param origin      The origin airport code.
     * @param destination The destination airport code.
     * @param start       The intended start date of the travel period (format: "YYYY-MM-DD").
     * @return A list of {@link dailyCheapest} objects, each representing the cheapest flight option
     * for a day in the specified period, including price in points, price in cash, and date.
     */
    public List<dailyCheapest> getFlightDataListAlaskaMonthly(String origin, String destination, String start, int numPassengers) {

        return getDailyCheapests(origin, destination, start, numPassengers);
    }

    public static List<dailyCheapest> getDailyCheapests(String origin, String destination, String start, int numPassengers) {
        LocalDate startDate = LocalDate.parse(start);

        int startYear = startDate.getYear();
        int startMonth = startDate.getMonthValue();

        String modifiedStart = start.replaceAll("-\\d{2}$", "-16");

        String json = requestHandlerAlaskaMonthly(origin, destination, modifiedStart, numPassengers);
        if (json != null && json.charAt(0) != '<') {
            Type type = new TypeToken<MonthlyDetails>() {
            }.getType();
            MonthlyDetails monthlyDetails = gson.fromJson(json, type);

            if (monthlyDetails != null) {
                List<dailyCheapest> dailyCheapests = monthlyDetails.shoulderDates();
                if (dailyCheapests != null) {
                    if (startMonth == 2) {
                        int febDays = YearMonth.of(startYear, startMonth).lengthOfMonth();
                        dailyCheapests = dailyCheapests.size() >= febDays ? dailyCheapests.subList(0, febDays) : new ArrayList<>(dailyCheapests);
                    } else if (Arrays.asList(4, 6, 9, 11).contains(startMonth)) {
                        dailyCheapests = dailyCheapests.size() >= 30 ? dailyCheapests.subList(0, 30) : new ArrayList<>(dailyCheapests);
                    }

                    if (startYear == currentDate.getYear() && startMonth == currentDate.getMonthValue()) {
                        int startIndex = Math.max(currentDate.getDayOfMonth() - 1, 0);
                        dailyCheapests = dailyCheapests.size() > startIndex ? dailyCheapests.subList(startIndex, dailyCheapests.size()) : new ArrayList<>();
                    } else if (startYear == limitDate.getYear() && startMonth == limitDate.getMonthValue()) {
                        for (int i = 0; i < dailyCheapests.size(); i++) {
                            if (LocalDate.parse(dailyCheapests.get(i).getDate()).isAfter(limitDate)) {
                                dailyCheapests = new ArrayList<>(dailyCheapests.subList(0, i));
                                break;
                            }
                        }
                    }

                    return dailyCheapests;
                }
            }
        }
        return new ArrayList<>();
    }


}

