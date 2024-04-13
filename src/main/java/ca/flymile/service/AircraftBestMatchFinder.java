package ca.flymile.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class AircraftBestMatchFinder
{
    private static final String FILE_PATH = "src/main/java/ca/flymile/FlyMileAirportData/flyMileAircraftDataSeatGuru.json";
    public static final Map<String, String[]> AIRLINE_MAP;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AircraftBestMatchFinder.class);
    private static final int N_GRAM_SIZE = 4;
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

        private static final double CRITICAL_MATCH_BONUS = 0.2;

        public static String aircraftBestMatchFinder(String carrierCode, int flightNumber , String target) {
            String[] candidates = AIRLINE_MAP.get(carrierCode);
            LevenshteinDistance levenshtein = new LevenshteinDistance();
            double maxScore = 0.0;
            int bestMatchIndex = -1;

            List<String> targetTokens = Arrays.asList(target.split("[ -]")); // Split by space and dash
            for (int i = 0; i < candidates.length; i += 2) {
                List<String> candidateTokens = Arrays.asList(candidates[i].split("[ -]"));
                double score = calculateTokenBasedScore(targetTokens, candidateTokens, levenshtein);

                if (score > maxScore) {
                    maxScore = score;
                    bestMatchIndex = i;
                }
            }

            if (bestMatchIndex == -1) return null;
            return String.format("%s?flightno=%d",candidates[bestMatchIndex + 1],flightNumber);
        }

        private static double calculateTokenBasedScore(List<String> targetTokens, List<String> candidateTokens, LevenshteinDistance levenshtein) {
            double score = 0.0;
            for (String targetToken : targetTokens) {
                for (String candidateToken : candidateTokens) {
                    int distance = levenshtein.apply(targetToken, candidateToken);
                    double normalizedScore = 1 - (double) distance / Math.max(targetToken.length(), candidateToken.length());
                    if (targetToken.equals(candidateToken)) normalizedScore += CRITICAL_MATCH_BONUS; // Bonus for exact matches on tokens
                    score += normalizedScore;
                }
            }
            return score;
        }

}

