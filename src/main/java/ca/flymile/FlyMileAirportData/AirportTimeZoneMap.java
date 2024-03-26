package ca.flymile.FlyMileAirportData;

import ca.flymile.simpleAirport.TimezoneAirport;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AirportTimeZoneMap {
    private static final Logger LOGGER = Logger.getLogger(AirportTimeZoneMap.class.getName());
    public static final Map<String, String> AIRPORT_TIMEZONE_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        Gson gson = new Gson();
        String filePath = "src/main/java/ca/flymile/FlyMileAirportData/TimeZone.json";

        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<TimezoneAirport>>(){}.getType();
            List<TimezoneAirport> airports = gson.fromJson(reader, listType);
            for (TimezoneAirport airport : airports) {
                map.put(airport.getAirportCode(), airport.getTimezone());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load airport data from URL: " + filePath, e);
        } catch (JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, "Failed to parse airport data from file: " + filePath, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred while loading airport data: " + filePath, e);
        }

        AIRPORT_TIMEZONE_MAP = Collections.unmodifiableMap(map);
    }
}
