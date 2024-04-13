package ca.flymile.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AircraftBestMatchFinder {
    private static final Logger logger = LoggerFactory.getLogger(AircraftBestMatchFinder.class);
    private static final String FILE_PATH = "src/main/java/ca/flymile/FlyMileAirportData/flyMileAircraftDataSeatGuru.json";
    public static final Map<String, String[]> AIRLINE_MAP = loadAirlineMap();

    private static Map<String, String[]> loadAirlineMap() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, List<Aircraft>>>() {}.getType();
            Map<String, List<Aircraft>> airlineMap = gson.fromJson(reader, mapType);
            return airlineMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> flattenAircraftDetails(e.getValue())));
        } catch (IOException e) {
            logger.error("Error reading the file: " + FILE_PATH, e);
            return Collections.emptyMap();
        }
    }

    private static String[] flattenAircraftDetails(List<Aircraft> aircrafts) {
        return aircrafts.stream()
                .flatMap(a -> Stream.of(a.name, a.url))
                .toArray(String[]::new);
    }

    static class Aircraft {
        String name;
        String url;
    }

    private static final double CRITICAL_MATCH_BONUS = 0.2;

    public static String aircraftBestMatchFinder(String carrierCode, int flightNumber, String target) {
        String[] candidates = AIRLINE_MAP.getOrDefault(carrierCode, new String[0]);
        if (candidates.length == 0) {
            logger.error("Carrier code not found or no candidates available.");
            return null;
        }
        if (target.contains("Airbus")) {
            String result = aircraftBestMatchFinderAirbus(carrierCode, target);
            if (result != null) {
                return String.format("%s?flightno=%d", result, flightNumber);
            }
        }

        if (target.contains(" (Winglets)")) {
            target = target.replace(" (Winglets)", "");
        }

        LevenshteinDistance levenshtein = new LevenshteinDistance();
        List<String> targetTokens = Arrays.asList(target.split("[ -]")); // Split by space and dash
        OptionalInt bestMatchIndex = IntStream.range(0, candidates.length / 2)
                .map(i -> 2 * i)
                .reduce((a, b) -> compareCandidates(a, b, targetTokens, candidates, levenshtein));

        return bestMatchIndex.isPresent() ?
                String.format("%s?flightno=%d", candidates[bestMatchIndex.getAsInt() + 1], flightNumber) : null;
    }

    private static int compareCandidates(int a, int b, List<String> targetTokens, String[] candidates, LevenshteinDistance levenshtein) {
        double scoreA = calculateTokenBasedScore(targetTokens, Arrays.asList(candidates[a].split("[ -]")), levenshtein);
        double scoreB = calculateTokenBasedScore(targetTokens, Arrays.asList(candidates[b].split("[ -]")), levenshtein);
        return scoreA > scoreB ? a : b;
    }
    public static String aircraftBestMatchFinderAirbus(String carrierCode, String target) {
        if(target.contains("neo")) {
            target = target.replace("neo", "");
        }

        String[] candidates = AIRLINE_MAP.get(carrierCode);
        double highestSimilarity = 0;
        int bestMatchIndex = -1; // Initialize the index of the best match

        for (int i = 0; i < candidates.length; i++) {
            double similarity = calculateNgramSimilarity(candidates[i], target);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatchIndex = i; // Update the index of the best match
            }
        }

        // Check if the best match was found and ensure the next index is within bounds
        if (bestMatchIndex != -1 && bestMatchIndex + 1 < candidates.length) {
            return candidates[bestMatchIndex + 1]; // Return the string at the next index
        } else {
            return null; // Return null if no best match or next index is out of bounds
        }
    }
    private static double calculateTokenBasedScore(List<String> targetTokens, List<String> candidateTokens, LevenshteinDistance levenshtein) {
        double score = 0.0;
        Map<String, Double> importanceMap = new HashMap<>();
        importanceMap.put("main", 1.0); // Main model number

        for (String targetToken : targetTokens) {
            for (String candidateToken : candidateTokens) {
                double tokenImportance = getTokenImportance(targetToken, importanceMap);
                int distance = levenshtein.apply(targetToken, candidateToken);
                double normalizedScore = (1 - (double) distance / Math.max(targetToken.length(), candidateToken.length())) * tokenImportance;
                if (targetToken.equals(candidateToken)) {
                    normalizedScore += CRITICAL_MATCH_BONUS; // Bonus for exact matches on tokens
                }
                score += normalizedScore;
            }
        }
        return score;
    }

    private static double calculateNgramSimilarity(String candidate, String target) {
        Set<String> candidateNgrams = generateNgrams(candidate);
        Set<String> targetNgrams = generateNgrams(target);

        Set<String> intersection = new HashSet<>(candidateNgrams);
        intersection.retainAll(targetNgrams);

        Set<String> union = new HashSet<>(candidateNgrams);
        union.addAll(targetNgrams);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    private static Set<String> generateNgrams(String input) {
        Set<String> ngrams = new HashSet<>();
        for (int i = 0; i <= input.length() - 3; i++) {
            ngrams.add(input.substring(i, i + 3));
        }
        return ngrams;
    }

    private static double getTokenImportance(String token, Map<String, Double> importanceMap) {
        // Check if the token matches any predefined importance rules
        if (token.matches("\\b\\d{3}\\b")) {
            return importanceMap.getOrDefault("main", 1.0);
        }
        return 1.0; // Default importance for other tokens
    }


}

