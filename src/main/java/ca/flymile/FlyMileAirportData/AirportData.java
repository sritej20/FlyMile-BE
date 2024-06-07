package ca.flymile.FlyMileAirportData;

import ca.flymile.simpleAirport.Airport;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AirportData {
    private static final Logger LOGGER = Logger.getLogger(AirportData.class.getName());
    private static final Set<String> tempSet = new HashSet<>();
    private static final String LOCAL_FILE_PATH = "src/main/java/ca/flymile/FlyMileAirportData/flymileAirportData.json";

    public static final Set<String> airportSet;

    static {
        loadAirportData();
        airportSet = Collections.unmodifiableSet(tempSet);
    }

    private static void loadAirportData() {
        try (FileReader reader = new FileReader(LOCAL_FILE_PATH)) {
            loadFromReader(reader);
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, "Failed to load airport data from local file: " + LOCAL_FILE_PATH, e);
        }
    }

    private static void loadFromReader(FileReader reader) throws IOException {
        Type listType = new TypeToken<List<Airport>>() {}.getType();
        List<Airport> airports = new Gson().fromJson(reader, listType);
        for (Airport airport : airports) {
            tempSet.add(airport.getAirportCode());
        }
    }
}
