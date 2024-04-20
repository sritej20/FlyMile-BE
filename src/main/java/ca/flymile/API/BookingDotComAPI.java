package ca.flymile.API;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;


public class BookingDotComAPI {
    private static final Logger logger = LoggerFactory.getLogger(BookingDotComAPI.class);

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String BASE_URL = "https://flights.booking.com/api/flights/";
    private static final String QUERY_TEMPLATE = "type=ONEWAY&adults=%d&cabinClass=%s&from=%s.CITY&to=%s.CITY&stops=0&depart=%s&sort=FASTEST&airlines=%s";

    public static String getFlightDetails(int adults, String cabinClass, String from, String to, String departDate, String airline)
    {
        String encodedCabinClass = URLEncoder.encode(cabinClass, StandardCharsets.UTF_8);
        String encodedFrom = URLEncoder.encode(from, StandardCharsets.UTF_8);
        String encodedTo = URLEncoder.encode(to, StandardCharsets.UTF_8);
        String encodedDepartDate = URLEncoder.encode(departDate, StandardCharsets.UTF_8);
        String encodedAirline = URLEncoder.encode(airline, StandardCharsets.UTF_8);

        String queryParams = String.format(QUERY_TEMPLATE, adults, encodedCabinClass, encodedFrom, encodedTo, encodedDepartDate, encodedAirline);

        URI uri = URI.create(BASE_URL + "?" + queryParams);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br, zstd")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                .build();
        return getJsonStringFromAirline(httpClient, request);
    }
}

