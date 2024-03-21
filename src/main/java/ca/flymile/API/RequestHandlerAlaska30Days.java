package ca.flymile.API;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;


/**
 * Handles HTTP requests for the Flymile API, specifically for retrieving 30 day Range cheapest Prices
 * for Alaska Airlines. It interacts with the API endpoint to search for available dates based
 * on the date, origin, and destination.
 * it return -15 days and +15 days from provided date
 */
public class RequestHandlerAlaska30Days {

    /**
     * Handles the request to the Alaska booking API for available flight dates in a given month [-15 + start , start + 15].
     *
     * @param date         The month for which available flight dates are requested in "YYYY-MM" format.
     * @param origin        The departure airport code.
     * @param destination   The destination airport code.
     * @return A string containing the response from the API with available flight dates.
     */
    public static String requestHandlerAlaska30Days( String origin, String destination,String date) {
        String requestBody = String.format("""
                            {
                                "origins": ["%s"],
                                "destinations": ["%s"],
                                "dates": ["%s"],
                                "onba": false,
                                "dnba": false,
                                "numADTs": 1,
                                "sliceToSearch": 0,
                                "selectedSegments": [],
                                "fareView": "as_awards",
                                "discount": {
                                    "code": "",
                                    "status": 0,
                                    "expirationDate": "",
                                    "message": "",
                                    "memo": "",
                                    "type": 0,
                                    "searchContainsDiscountedFare": false,
                                    "campaignName": "",
                                    "campaignCode": "",
                                    "distribution": 0
                                },
                                "isAlaska": false
                            }
                  """, origin, destination, date);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.alaskaair.com/search/api/shoulderDates"))
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "en-CA,en-US;q=0.9,en;q=0.8")
                .header("Content-Type", " text/plain;charset=UTF-8")
                .header("Origin", "https://www.alaskaair.com")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.1 Safari/605.1.15")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return getJsonStringFromAirline(client, request);
    }
}

