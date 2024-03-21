package ca.flymile.API;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;



import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;



/**
 * Handles HTTP requests for the Flymile API, specifically for retrieving flight information from Alaska.
 * It is configured to interact with a predefined API endpoint to search for flights based on
 * the provided criteria such as date, origin, destination and number of passengers.
 * Needs to ADD Proxy configuration in HttpClient client = HttpClient.newHttpClient();
 */

public class RequestHandlerAlaska {


    /**
     * Handles the request to the Alaska booking API.
     *
     * @param date              The departure date in "YYYY-MM-DD" format.
     * @param origin            The departure airport code.
     * @param destination       The destination airport code.
     * @param numberOfPassengers The number of passengers.
     * @return A string containing the response from the API.
     */
    public static String requestHandlerAlaska(String date, String origin, String destination, int numberOfPassengers)
    {
        String requestBody = String.format("""
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
                  """, origin, destination, date, numberOfPassengers);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.alaskaair.com/search/api/flightresults"))
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
