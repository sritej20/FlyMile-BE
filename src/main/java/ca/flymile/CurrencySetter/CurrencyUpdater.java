package ca.flymile.CurrencySetter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

public class CurrencyUpdater {
    static class CurrencyResponse {
        List<Currency> response;
    }

    static class Currency {
        String currencyCode;
    }

    private static final Logger logger = LoggerFactory.getLogger(CurrencyUpdater.class);
    private static final String API_KEY = "SDySgqoB7NcZuLxC1XcDvqXsqGDabJsk";
    private static final String API_KEY1 = "cbdrsggW4OBKWnesdde0rsKobBwhR4Yz";
    private static final String API_KEY2 = "HWKZykzPk6Pqnnqxq1LF9oSXJE1wC8kG";
    
    private static final String API_KEY3 = "JmbSckDLTBmW1oZZG1Jzn3bCJU4FVrrR";
    
    private static volatile Map<String, Double> currentCurrencyValues = new ConcurrentHashMap<>();
    private static Map<String, Double> updateCurrencyValues = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    static {
        loadCurrencyList();
        scheduler.scheduleAtFixedRate(CurrencyUpdater::updateCurrencyValues, 0, 24, TimeUnit.HOURS);
    }

    public static Double getCURRENCY_VALUE_TO_USD(String currency) {
        return currentCurrencyValues.get(currency);
    }

    private static void loadCurrencyList() {
        String path = "src/main/java/ca/flymile/CurrencySetter/currencies.json";
        try {
            String json = Files.readString(Paths.get(path));
            Gson gson = new Gson();
            CurrencyResponse currencyResponse = gson.fromJson(json, CurrencyResponse.class);
            currencyResponse.response.forEach(currency -> currentCurrencyValues.put(currency.currencyCode, 0.0));
            updateCurrencyValues = new ConcurrentHashMap<>(currentCurrencyValues);
            logger.info("Currencies loaded and initialized.");
        } catch (IOException e) {
            logger.error("Error reading the file", e);
        }
    }

    private static void updateCurrencyValues() {
        updateCurrencyValues.keySet().forEach(CurrencyUpdater::convertCurrencyToUSD);
        currentCurrencyValues = new ConcurrentHashMap<>(updateCurrencyValues);
    }

    public static void convertCurrencyToUSD(String toCurrency) {
        String baseUrl = "https://api.currencybeacon.com/v1/convert?from=" + toCurrency + "&to=USD&amount=1&api_key=" + API_KEY3;
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.info(url.toString());
                logger.error("HTTP error code: {}", responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream(), StandardCharsets.UTF_8);
                String response = scanner.useDelimiter("\\A").next();
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                double rate = jsonResponse.getAsJsonObject("response").get("value").getAsDouble();
                updateCurrencyValues.put(toCurrency, rate);
                scanner.close();
            }
            connection.disconnect();
        } catch (IOException e) {
            logger.error("Error converting currency", e);
        }
    }
}
