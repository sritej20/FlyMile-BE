package ca.flymile.API;

import java.net.http.HttpClient;
import static ca.flymile.API.RequestBuilder.buildRequestBodyAlaska;

public class RequestHandlerAlaska {
    private static final  HttpClient CLIENT = HttpClient.newHttpClient();

    private static final String REQUEST_URL = "https://www.alaskaair.com/search/api/flightresults";
    private static final String REQUEST_TEMPLATE = """
                {
                    "origins": [
                        "%s"
                    ],
                    "destinations": [
                        "%s"
                    ],
                    "dates": [
                        "%s"
                    ],
                    "numADTs": %d,
                    "numINFs": 0,
                    "fareView": "as_awards",
                    "onba": false,
                    "dnba": false,
                    "sessionID": "",
                    "solutionSetIDs": [],
                    "solutionIDs": [],
                    "qpxcVersion": "",
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
                    "lockFare": false,
                    "isAlaska": false
                }
            """;


    /**
     * Handles the request to the Alaska booking API.
     *
     * @param date               The departure date in "YYYY-MM-DD" format.
     * @param origin             The departure airport code.
     * @param destination        The destination airport code.
     * @param numberOfPassengers The number of passengers.
     * @return A string containing the response from the API.
     */
    public static String requestHandlerAlaska(String date, String origin, String destination, int numberOfPassengers) {
        return buildRequestBodyAlaska(origin, destination, date, numberOfPassengers, REQUEST_TEMPLATE, REQUEST_URL, CLIENT);

    }

}

