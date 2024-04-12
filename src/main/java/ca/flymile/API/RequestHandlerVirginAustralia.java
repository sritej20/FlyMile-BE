package ca.flymile.API;

import java.net.http.HttpClient;

import static ca.flymile.API.RequestBuilder.buildRequestBodyDelta;
import static ca.flymile.API.RequestBuilder.buildRequestBodyVirginAustralia;

public class RequestHandlerVirginAustralia {
    private static final String REQUEST_URL ="https://book.virginaustralia.com/api/graphql";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String REQUEST_TEMPLATE = """
{
    "operationName": "bookingAirSearch",
    "variables": {
        "airSearchInput": {
            "cabinClass": "First",
            "awardBooking": true,
            "promoCodes": [],
            "searchType": "BRANDED",
            "itineraryParts": [
                {
                    "from": {
                        "useNearbyLocations": false,
                        "code": "%s"
                    },
                    "to": {
                        "useNearbyLocations": false,
                        "code": "%s"
                    },
                    "when": {
                        "date": "%s"
                    }
                }
            ],
            "passengers": {
                "ADT": %d
            }
        }
    },
    "extensions": {},
    "query": "query bookingAirSearch($airSearchInput: CustomAirSearchInput) {\\n  bookingAirSearch(airSearchInput: $airSearchInput) {\\n    originalResponse\\n    __typename\\n  }\\n}\\n"
}
""";
    public static String requestHandlerVirginAustralia(String origin, String destination, String departureDate, int numPassengers) {
        return buildRequestBodyVirginAustralia(origin, destination, departureDate, numPassengers, REQUEST_TEMPLATE, REQUEST_URL, CLIENT);
    }
}
