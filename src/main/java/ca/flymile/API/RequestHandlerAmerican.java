package ca.flymile.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;



  /**
   * Handles HTTP requests for the Flymile API, specifically for retrieving flight information.
   * It is configured to interact with a predefined API endpoint to search for flights based on
   * the provided criteria such as date, origin, destination, number of passengers, and cabin class.
   * Needs to ADD Proxy configuration in HttpClient client = HttpClient.newHttpClient();
   */

public class RequestHandlerAmerican {

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
    public static String requestHandlerAmerican (String date, String origin, String destination, int numberOfPassengers, boolean upperCabin)
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

        return getJsonStringFromAirline(client, request);
    }


}
