package ca.flymile.FlyMileAirportData;

import ca.flymile.simpleAirport.Airport;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
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
        String filePath = "https://drive.google.com/uc?export=download&id=1vGJDClEx26MSAWQ9HSYyK0_85uzrZcs1";
        loadAirportData(filePath);
        airportSet = Collections.unmodifiableSet(tempSet);
    }

    private static void loadAirportData(String filePath) {
        try {
            URL url = new URL(filePath);
            try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
                loadFromReader(reader);
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found at URL: " + filePath + ", attempting to read from local file", e);
            try (FileReader reader = new FileReader(LOCAL_FILE_PATH)) {
                loadFromReader(reader);
            } catch (IOException | JsonSyntaxException ex) {
                LOGGER.log(Level.SEVERE, "Failed to load airport data from local file: " + LOCAL_FILE_PATH, ex);
            }
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Malformed URL: " + filePath, e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "I/O error occurred while loading airport data from URL: " + filePath, e);
        } catch (JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, "Failed to parse airport data", e);
        }
    }

    private static void loadFromReader(InputStreamReader reader) throws IOException {
        Type listType = new TypeToken<List<Airport>>() {}.getType();
        List<Airport> airports = new Gson().fromJson(reader, listType);
        for (Airport airport : airports) {
            tempSet.add(airport.getAirportCode());
        }
    }
}
