package ca.flymile.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AircraftBestMatchFinder
{
    private static final String FILE_PATH = "src/main/java/ca/flymile/FlyMileAirportData/flyMileAircraftDataSeatGuru.json";
    public static final Map<String, String[]> AIRLINE_MAP;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AircraftBestMatchFinder.class);

    static {
        Map<String, String[]> tempMap = new HashMap<>();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, List<Aircraft>>>(){}.getType();
            Map<String, List<Aircraft>> airlineMap = gson.fromJson(reader, mapType);

            for (Map.Entry<String, List<Aircraft>> entry : airlineMap.entrySet()) {
                List<Aircraft> aircraftList = entry.getValue();
                String[] details = new String[aircraftList.size() * 2];
                int index = 0;
                for (Aircraft aircraft : aircraftList) {
                    details[index++] = aircraft.name;
                    details[index++] = aircraft.url;
                }
                tempMap.put(entry.getKey(), details);
            }
        } catch (IOException e) {
            logger.error("Error reading the file: " + FILE_PATH);
        }
        AIRLINE_MAP = tempMap;
    }
    static class Aircraft {
        String name;
        String url;
    }

    public String aircraftBestMatchFinder(String carrierCode, int flightNumber, String aircraft) {
        String[] candidates = AIRLINE_MAP.get(carrierCode);
        if (candidates == null) {
            return null; // Return null if no candidates found for the carrier code
        }

        String bestMatch = findBestMatch(candidates, aircraft);
        if (bestMatch == null) {
            return null; // Return null if no match found
        }

        return bestMatch + "?flightno=" + flightNumber;
    }
    public static String findBestMatch(String[] candidates, String target) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        int minDistance = Integer.MAX_VALUE;
        int bestMatchIndex = -1;

        // Iterate through the array, considering only aircraft names (i.e., even indices)
        for (int i = 0; i < candidates.length; i += 2) {
            int distance = levenshteinDistance.apply(candidates[i], target);
            if (distance < minDistance) {
                minDistance = distance;
                bestMatchIndex = i;
            }
        }

        // Return the URL associated with the best match, which is the next element after the aircraft name
        return (bestMatchIndex == -1) ? null : candidates[bestMatchIndex + 1];
    }

}

