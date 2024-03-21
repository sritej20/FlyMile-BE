package ca.flymile.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;

public class RequestHandlerAmericanMonthly {

    public static String requestHandlerAmericanMonthly(
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
                .uri(URI.create("https://www.aa.com/booking/api/search/calendar"))
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.aa.com")
                .header("Referer", "https://www.aa.com/booking/choose-flights/1")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")

                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return getJsonStringFromAirline(client, request);
    }
}
