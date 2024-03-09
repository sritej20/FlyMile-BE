package ca.flymile.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


import static ca.flymile.DecompressorLogic.Decompressor.decompress;


  /**
   * Handles HTTP requests for the Flymile API, specifically for retrieving flight information.
   * It is configured to interact with a predefined API endpoint to search for flights based on
   * the provided criteria such as date, origin, destination, number of passengers, and cabin class.
   * Needs to ADD Proxy configuration in HttpClient client = HttpClient.newHttpClient();
   */

public class RequestHandler {
    private static final String proxyUserName = "spqou3ok02";
    private static final String proxyPassword = "iepIc29DuAX5a7upyh";

    /**
     * Handles the request to the AA booking API.
     *
     * @param date              The departure date in "YYYY-MM-DD" format.
     * @param origin            The departure airport code.
     * @param destination       The destination airport code.
     * @param numberOfPassengers The number of passengers.
     * @param upperCabin        Indicates if upper cabin (Business/First) seats are preferred.
     * @return A string containing the response from the API.
     */
    public static String requestHandler(String date, String origin, String destination, int numberOfPassengers, boolean upperCabin)
    {
        String businessFirstCabinOnly = upperCabin ? "BUSINESS,FIRST" : "";
        String requestBody = String.format("""
        {
            "metadata": {
                "selectedProducts": [],
                "tripType": "OneWay",
                "udo": {}
            },
            "passengers": [
                {"type": "adult", "count": %d}
            ],
            "requestHeader": {
                "clientId": "AAcom"
            },
            "slices": [
                {
                    "allCarriers": true,
                    "cabin": "%s",
                    "departureDate": "%s",
                    "destination": "%s",
                    "destinationNearbyAirports": false,
                    "maxStops": null,
                    "origin": "%s",
                    "originNearbyAirports": false
                }
            ],
            "tripOptions": {
                "corporateBooking": false,
                "fareType": "Lowest",
                "locale": "en_US",
                "pointOfSale": null,
                "searchType": "Award"
            },
            "version": "",
            "queryParams": {
                "sliceIndex": 0,
                "sessionId": "",
                "solutionSet": "",
                "solutionId": ""
            }
        }
        """, numberOfPassengers, businessFirstCabinOnly, date, destination, origin);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.aa.com/booking/api/search/itinerary"))
                .header("Accept", "application/json, text/plain, */*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.aa.com")
                .header("Referer", "https://www.aa.com/booking/choose-flights/1")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        CompletableFuture<String> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(response -> {
                    // Dynamically get the content encoding from the response headers
                    String contentEncoding = response.headers().firstValue("Content-Encoding").orElse("");
                    return decompress(response.body(), contentEncoding);
                })
                .thenApply(decompressedBody -> {
                    if (decompressedBody == null) {
                        return "Decompression error or no data";
                    }
                    return new String(decompressedBody);
                });

        try {
            return future.get(); // Wait and retrieve the future result
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt(); // handle interrupted status
            e.printStackTrace();
            return "Failed to execute request: " + e.getMessage();
        }
    }


}
