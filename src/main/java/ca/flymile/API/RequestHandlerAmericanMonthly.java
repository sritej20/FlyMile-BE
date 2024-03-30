package ca.flymile.API;

import java.net.http.HttpClient;
import static ca.flymile.API.RequestBuilder.buildRequestBodyAmerican;

public class RequestHandlerAmericanMonthly {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();


    private static final String REQUEST_URL = "https://www.aa.com/booking/api/search/calendar";
    private static final String REQUEST_TEMPLATE = """
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
                    "maxStops": "%s",
                    "origin": "%s",
                    "originNearbyAirports": false
                }],
                "tripOptions": {"corporateBooking": false, "fareType": "Lowest", "locale": "en_US", "pointOfSale": null, "searchType": "Award"},
                "loyaltyInfo": null,
                "version": "",
                "queryParams": {"sliceIndex": 0, "sessionId": "", "solutionSet": "", "solutionId": ""}
            }
            """;


    public static String requestHandlerAmericanMonthly(
            String origin,
            String destination,
            String departureDate,
            int numberOfPassengers,
            boolean upperCabin,
            String maxStops
    ) {



        return buildRequestBodyAmerican(origin, destination, departureDate, numberOfPassengers, upperCabin, maxStops, REQUEST_TEMPLATE, REQUEST_URL, CLIENT);
    }


}
