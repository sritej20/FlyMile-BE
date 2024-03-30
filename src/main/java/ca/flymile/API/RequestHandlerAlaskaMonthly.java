package ca.flymile.API;

import java.net.http.HttpClient;
import static ca.flymile.API.RequestBuilder.buildRequestBodyAlaska;


/**
 * Handles HTTP requests for the Flymile API, specifically for retrieving 30 day Range cheapest Prices
 * for Alaska Airlines. It interacts with the API endpoint to search for available dates based
 * on the date, origin, and destination.
 * It returns -15 days and +15 days from the provided date
 */
public class RequestHandlerAlaskaMonthly {
    private static final  HttpClient CLIENT = HttpClient.newHttpClient();

    private static final String REQUEST_URL = "https://www.alaskaair.com/search/api/shoulderDates";
    private static final String REQUEST_TEMPLATE = """
                {
                    "origins": ["%s"],
                    "destinations": ["%s"],
                    "dates": ["%s"],
                    "onba": false,
                    "dnba": false,
                    "numADTs": %d,
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
            """;


    /**
     * Handles the request to the Alaska booking API for available flight dates in a given month [-15 + start,
     * start + 15].
     *
     * @param date        The month for which available flight dates are requested in "YYYY-MM" format.
     * @param origin      The departure airport code.
     * @param destination The destination airport code.
     * @return A string containing the response from the API with available flight dates.
     */
    public static String requestHandlerAlaskaMonthly(String origin, String destination, String date, int numberOfPassengers) {

        return buildRequestBodyAlaska(origin, destination, date, numberOfPassengers, REQUEST_TEMPLATE, REQUEST_URL, CLIENT);

    }
}

