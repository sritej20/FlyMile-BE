package ca.flymile.FlyMileAirportData;

import ca.flymile.simpleAirport.TimezoneAirport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirportTimeZoneMap {
    public static final Map<String, String> AIRPORT_TIMEZONE_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        Gson gson = new Gson();

        try {
            FileReader reader = new FileReader("src/main/java/ca/flymile/FlyMileAirportData/airportDataWithTimeZone.json");
            Type listType = new TypeToken<List<TimezoneAirport>>(){}.getType();
            List<TimezoneAirport> airports = gson.fromJson(reader, listType);

            for (TimezoneAirport airport : airports) {
                map.put(airport.getAirportCode(), airport.getTimezone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AIRPORT_TIMEZONE_MAP = Collections.unmodifiableMap(map);
    }


}