package ca.flymile.API;

import java.net.http.HttpClient;
import static ca.flymile.API.RequestBuilder.buildRequestBodyAmerican;


/**
 * Handles HTTP requests for the Flymile API, specifically for retrieving flight information.
 * It is configured to interact with a predefined API endpoint to search for flights based on
 * the provided criteria such as date, origin, destination, number of passengers, and cabin class.
 */

public class RequestHandlerAmerican {
    private static final  HttpClient CLIENT = HttpClient.newHttpClient();


    private static final String REQUEST_TEMPLATE = """
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
                        "maxStops": "%s",
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
            """;
    private static final String REQUEST_URL = "https://www.aa.com/booking/api/search/itinerary";

    /**
     * Handles the request to the AA booking API.
     *
     * @param date               The departure date in "YYYY-MM-DD" format.
     * @param origin             The departure airport code.
     * @param destination        The destination airport code.
     * @param numberOfPassengers The number of passengers.
     * @param upperCabin         Indicates if upper cabin (Business/First) seats are preferred.
     * @return A string containing the response from the API.
     */
    public static String requestHandlerAmerican(String date, String origin, String destination, int numberOfPassengers, boolean upperCabin, String maxStops) {

        return buildRequestBodyAmerican(origin, destination, date, numberOfPassengers, upperCabin, maxStops, REQUEST_TEMPLATE, REQUEST_URL, CLIENT);
    }


}
