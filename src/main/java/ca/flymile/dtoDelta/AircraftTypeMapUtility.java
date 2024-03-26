package ca.flymile.dtoDelta;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AircraftTypeMapUtility {

    private static final Logger LOGGER = Logger.getLogger(AircraftTypeMapUtility.class.getName());
    private static final Map<String, String> AIRCRAFT_TYPE_MAP = initMap();

    private static Map<String, String> initMap() {
        String filePath = "src/main/java/ca/flymile/dtoDelta/DeltaAircraftCodes.json";
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
        Map<String, String> map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Map<String, Map<String, String>> tempMap = gson.fromJson(reader, type);
            tempMap.forEach((key, value) -> map.put(key, value.get("IndustryStandardAircraftTypeName")));
            return Collections.unmodifiableMap(map);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load aircraft type map from JSON, using empty map as fallback", e);
            return Collections.emptyMap();
        }
    }

    public static String getAircraftTypeName(String code) {
        return AIRCRAFT_TYPE_MAP.getOrDefault(code, "UNKNOWN");
    }

}
