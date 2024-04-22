package ca.flymile.service;

import ca.flymile.API.SeatMaps;
import ca.flymile.SeatMaps.SeatMapsToken;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;

public class SeatMapsService {
    private static final Logger LOGGER = Logger.getLogger(SeatMapsService.class.getName());
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "https://seatmaps.com";

    public Optional<String> getSeatMap(String carrierCode, int flightNumber, String date) {
        try {
            String accessToken = SeatMapsToken.getAccessToken();
            if (accessToken == null) {
                LOGGER.severe("Access token is null");
                return Optional.empty();
            }
            String json = SeatMaps.getAircraftLink(carrierCode, date, flightNumber, accessToken);

            if (json == null || json.trim().isEmpty()) {
                LOGGER.severe("Received empty JSON response");
                return Optional.empty();
            }
            if (json.charAt(0)=='<') {
                LOGGER.severe("Blocked by Seat Maps");
                return Optional.empty();
            }

            Data data = gson.fromJson(json, new TypeToken<Data>() {}.getType());
            if (data != null && data.getUrl() != null) {
                return Optional.of(BASE_URL + data.getUrl());
            } else {
                LOGGER.severe("No URL found in the JSON response");
                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while getting seat map", e);
            return Optional.empty();
        }
    }

    @Getter
    private static class Data {
        @SerializedName("mobileLink")
        private String url;
    }
}
