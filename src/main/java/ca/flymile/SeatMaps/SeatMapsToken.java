package ca.flymile.SeatMaps;

import ca.flymile.API.SeatMaps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeatMapsToken {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(SeatMapsToken.class.getName());
    private static ScheduledExecutorService scheduler;
    @Getter
    private static String accessToken;
    static{
        scheduleTask(); // Ensure scheduler is started when the service is initialized
    }
    public static void scheduleTask() {
        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
            Runnable task = () -> {
                try {
                    accessToken = parseAccessToken(SeatMaps.seatMapAccessToken());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Issue Getting Token For SeatMaps: " + e.getMessage(), e);
                }
            };
            scheduler.scheduleAtFixedRate(task, 0, 12, TimeUnit.MINUTES);
        }
    }

    private static String parseAccessToken(String response) {
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        return jsonResponse.get("accessToken").getAsString();
    }

}
