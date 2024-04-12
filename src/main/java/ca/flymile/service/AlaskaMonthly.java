package ca.flymile.service;

import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.ModelAlaskaMonthly.MonthlyDetails;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ca.flymile.API.RequestHandlerAlaskaMonthly.requestHandlerAlaskaMonthly;
import static ca.flymile.service.DateHandler.currentDate;
import static ca.flymile.service.DateHandler.limitDate;

@Component
public class AlaskaMonthly {
    private static final Gson gson = new Gson();
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(AlaskaMonthly.class.getName());

    public CompletableFuture<List<DailyCheapest>> getFlightDataListAlaskaMonthly(String origin, String destination, String start, int numPassengers) {
        return CompletableFuture.supplyAsync(() -> getDailyCheapests(origin, destination, start, numPassengers));
    }

    public List<DailyCheapest> getDailyCheapests(String origin, String destination, String start, int numPassengers) {
        LocalDate startDate = LocalDate.parse(start);
        int startYear = startDate.getYear();
        int startMonth = startDate.getMonthValue();
        String modifiedStart = start.replaceAll("-\\d{2}$", "-16");
        String json = requestHandlerAlaskaMonthly(origin, destination, modifiedStart, numPassengers);

        if (json != null && !json.isEmpty()) {
            if (json.charAt(0) == '<') {
                LOGGER.log(Level.SEVERE, "Blocked By Alaska Monthly");
                return Collections.emptyList();
            }

            Type type = new TypeToken<MonthlyDetails>() {}.getType();
            MonthlyDetails monthlyDetails = gson.fromJson(json, type);

            if (monthlyDetails != null) {
                List<DailyCheapest> dailyCheapests = monthlyDetails.getShoulderDates();
                if (dailyCheapests != null) {
                    dailyCheapests = adjustDatesBasedOnMonth(startYear, startMonth, dailyCheapests);
                    dailyCheapests = filterDatesWithinRange(startYear, startMonth, dailyCheapests);
                    return dailyCheapests;
                }
            }
        } else {
            LOGGER.log(Level.WARNING, "Received null or empty JSON response");
        }

        return new ArrayList<>();
    }

    private static List<DailyCheapest> adjustDatesBasedOnMonth(int startYear, int startMonth, List<DailyCheapest> DailyCheapests) {
        if (startMonth == 2) {
            int febDays = YearMonth.of(startYear, startMonth).lengthOfMonth();
            return DailyCheapests.size() >= febDays ? DailyCheapests.subList(0, febDays) : new ArrayList<>(DailyCheapests);
        } else if (Arrays.asList(4, 6, 9, 11).contains(startMonth)) {
            return DailyCheapests.size() >= 30 ? DailyCheapests.subList(0, 30) : new ArrayList<>(DailyCheapests);
        }
        return DailyCheapests;
    }

    private static List<DailyCheapest> filterDatesWithinRange(int startYear, int startMonth, List<DailyCheapest> DailyCheapests) {
        if (startYear == currentDate.getYear() && startMonth == currentDate.getMonthValue()) {
            int startIndex = Math.max(currentDate.getDayOfMonth() - 1, 0);
            return DailyCheapests.size() > startIndex ? DailyCheapests.subList(startIndex, DailyCheapests.size()) : new ArrayList<>();
        } else if (startYear == limitDate.getYear() && startMonth == limitDate.getMonthValue()) {
            for (int i = 0; i < DailyCheapests.size(); i++) {
                if (LocalDate.parse(DailyCheapests.get(i).getDate()).isAfter(limitDate)) {
                    return new ArrayList<>(DailyCheapests.subList(0, i));
                }
            }
        }
        return DailyCheapests;
    }
    private String generateCacheKey(String date, String origin, String destination, int numPassengers) {
        return String.format("ASMM:%s:%s:%s:%d", date, origin, destination, numPassengers);
    }
}
