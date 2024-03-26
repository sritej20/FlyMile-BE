package ca.flymile.FlyMileAirportData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyRetriever {

    private static final Logger LOGGER = Logger.getLogger(CurrencyRetriever.class.getName());
    private static final String AIRPORT_JSON_FILE_PATH = "src/main/java/ca/flymile/FlyMileAirportData/DeltaAirportCountry.json";
    private static final String COUNTRY_CURRENCY_JSON_FILE_PATH = "src/main/java/ca/flymile/FlyMileAirportData/DeltaCountryCurrency.json";
    private static final String CURRENCY_FILE_PATH = "src/main/java/ca/flymile/FlyMileAirportData/deltaCurrency";
    private static final Map<String, String> airportCountryMap = new HashMap<>();
    private static final Map<String, String> countryCurrencyMap = new HashMap<>();
    private static final Set<String> allowedCurrencies = new HashSet<>();

    static {
        loadAirportData();
        loadCountryCurrencyData();
        loadAllowedCurrencies();
    }

    private static void loadAirportData() {
        try (FileReader reader = new FileReader(AIRPORT_JSON_FILE_PATH)) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() {}.getType();
            airportCountryMap.putAll(gson.fromJson(reader, type));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading airport data from JSON file", e);
        }
    }


    private static void loadCountryCurrencyData() {
        try (FileReader reader = new FileReader(COUNTRY_CURRENCY_JSON_FILE_PATH)) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>() {}.getType();
            countryCurrencyMap.putAll(gson.fromJson(reader, type));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading country currency data from JSON file", e);
        }
    }

    private static void loadAllowedCurrencies() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CURRENCY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allowedCurrencies.add(line.trim());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading allowed currencies from file", e);
        }
    }

    public static String getValidCurrencyForAirport(String airportCode) {
        String country = airportCountryMap.get(airportCode);
        if (country == null) {
            LOGGER.warning("Country not found for airport code: " + airportCode);
            return "USD";
        }

        String currency = countryCurrencyMap.get(country);
        if (currency == null) {
            LOGGER.warning("Currency not found for country: " + country);
            return "USD";
        }

        return allowedCurrencies.contains(currency) ? currency : "USD";
    }

}
