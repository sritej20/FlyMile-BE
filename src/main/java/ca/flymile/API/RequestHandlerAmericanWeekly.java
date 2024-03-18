package ca.flymile.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RequestHandlerAmericanWeekly {

    /**
     * Handles the request to the American Airlines weekly flight search API.
     *
     * @param departureDate      The departure date in "YYYY-MM-DD" format.
     * @param origin             The departure airport code.
     * @param destination        The destination airport code.
     * @param upperCabin         If true, set cabin to "BUSINESS,FIRST", else leave it empty.
     * @param numberOfPassengers The number of adult passengers.
     * @return A string containing the response from the API.
     */
    public static String requestHandlerAmericanWeekly(
            String origin,
            String destination,
            String departureDate,
            int numberOfPassengers,
            boolean upperCabin
            ) {

        String cabin = upperCabin ? "BUSINESS,FIRST" : "";
        String requestBody = String.format("""
                {
                    "metadata": {"selectedProducts": [], "tripType": "OneWay", "udo": {}},
                    "passengers": [{"type": "adult", "count": %d}],
                    "requestHeader": {"clientId": "AAcom"},
                    "slices": [{
                        "allCarriers": true,
                        "cabin": "%s",
                        "departureDate": "%s",
                        "destination": "%s",
                        "destinationNearbyAirports": false,
                        "maxStops": null,
                        "origin": "%s",
                        "originNearbyAirports": false
                    }],
                    "tripOptions": {"corporateBooking": false, "fareType": "Lowest", "locale": "en_US", "pointOfSale": null, "searchType": "Award"},
                    "loyaltyInfo": null,
                    "version": "",
                    "queryParams": {"sliceIndex": 0, "sessionId": "", "solutionSet": "", "solutionId": ""}
                }
                """, numberOfPassengers, cabin, departureDate, destination, origin);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.aa.com/booking/api/search/weekly"))
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.aa.com")
                .header("Referer", "https://www.aa.com/booking/choose-flights/1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        CompletableFuture<String> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

        try {
            return future.get(); // Wait and retrieve the future result
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt(); // handle interrupted status
            e.printStackTrace();
            return "Failed to execute request: " + e.getMessage();
        }
    }

}
