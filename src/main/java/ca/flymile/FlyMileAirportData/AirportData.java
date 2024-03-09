package ca.flymile.FlyMileAirportData;

import ca.flymile.simpleAirport.Airport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages airport data, providing a read-only set of airport codes loaded from a JSON file.
 * <p>
 * This class is designed to load airport information from a specified JSON file at startup
 * and provide global access to the set of airport codes. The set is unmodifiable to ensure
 * data integrity throughout the application lifecycle.
 * </p>
 */
public class AirportData {
    /**
     * A temporary set used during the initialization phase to store airport codes.
     */
    private static final Set<String> tempSet = new HashSet<>();

    /**
     * An unmodifiable set of airport codes available for access throughout the application.
     * <p>
     * This set is populated at class loading time from a JSON file containing airport data.
     * It provides a fast, read-only view of airport codes to ensure consistent data access
     * and integrity.
     * </p>
     */
    public static final Set<String> airportSet;

    // Static initializer block
    static {
        String filePath = "src/main/java/ca/flymile/FlyMileAirportData/flymileAirportData.json";
        loadAirportData(filePath);
        airportSet = Collections.unmodifiableSet(tempSet);
    }

    /**
     * Loads airport data from the specified JSON file and populates the temporary set of airport codes.
     * <p>
     * This method reads the JSON file using a {@link FileReader}, parses it into a list of {@link Airport}
     * objects using Gson, and then extracts and stores each airport code in {@code tempSet}. After loading,
     * {@code tempSet} is used to initialize the {@link #airportSet}.
     * </p>
     *
     * @param filePath The file path to the JSON file containing airport data.
     */
    private static void loadAirportData(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<Airport>>(){}.getType();
            List<Airport> airports = new Gson().fromJson(reader, listType);
            for (Airport airport : airports) {
                tempSet.add(airport.getAirportCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


