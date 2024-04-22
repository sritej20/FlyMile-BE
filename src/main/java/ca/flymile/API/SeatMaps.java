package ca.flymile.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;

public class SeatMaps{
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String REQUEST_URL = "https://seatmaps.com/auth";


    // Header value constants
    private static final String ACCEPT_SEATMAPS = "application/json";
    private static final String ACCEPT_ENCODING_SEATMAPS = "gzip, deflate, br";
    private static final String ACCEPT_LANGUAGE_SEATMAPS = "en-US,en;q=0.9";
    private static final String DNT_SEATMAPS = "1";  // Do not track request header
    private static final String REFERER_SEATMAPS = "https://seatmaps.com/";
    private static final String SEC_CH_UA_SEATMAPS = "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"";
    private static final String SEC_CH_UA_MOBILE_SEATMAPS = "?1";
    private static final String SEC_CH_UA_PLATFORM_SEATMAPS = "\"Android\"";
    private static final String SEC_FETCH_DEST_SEATMAPS = "empty";
    private static final String SEC_FETCH_MODE_SEATMAPS = "cors";
    private static final String SEC_FETCH_SITE_SEATMAPS = "same-origin";
    private static final String USER_AGENT_SEATMAPS = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Mobile Safari/537.36";

    // Header key constants
    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_REFERER = "Referer";
    private static final String HEADER_ORIGIN = "Referer";
    private static final String HEADER_DNT = "Dnt";
    private static final String HEADER_SEC_CH_UA = "Sec-Ch-Ua";
    private static final String HEADER_SEC_CH_UA_MOBILE = "Sec-Ch-Ua-Mobile";
    private static final String HEADER_SEC_CH_UA_PLATFORM = "Sec-Ch-Ua-Platform";
    private static final String HEADER_SEC_FETCH_DEST = "Sec-Fetch-Dest";
    private static final String HEADER_SEC_FETCH_MODE = "Sec-Fetch-Mode";
    private static final String HEADER_SEC_FETCH_SITE = "Sec-Fetch-Site";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    public static String seatMapAccessToken() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REQUEST_URL))
                .header(HEADER_DNT, DNT_SEATMAPS)
                .header(HEADER_REFERER, REFERER_SEATMAPS)
                .header(HEADER_SEC_CH_UA, SEC_CH_UA_SEATMAPS)
                .header(HEADER_ACCEPT, ACCEPT_SEATMAPS)
                .header(HEADER_ACCEPT_ENCODING, ACCEPT_ENCODING_SEATMAPS)
                .header(HEADER_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_SEATMAPS)
                .header(HEADER_SEC_CH_UA_MOBILE, SEC_CH_UA_MOBILE_SEATMAPS)
                .header(HEADER_SEC_CH_UA_PLATFORM, SEC_CH_UA_PLATFORM_SEATMAPS)
                .header(HEADER_SEC_FETCH_DEST, SEC_FETCH_DEST_SEATMAPS)
                .header(HEADER_SEC_FETCH_MODE, SEC_FETCH_MODE_SEATMAPS)
                .header(HEADER_USER_AGENT, USER_AGENT_SEATMAPS)
                .header(HEADER_SEC_FETCH_SITE, SEC_FETCH_SITE_SEATMAPS)
                .GET()
                .build();
        return getJsonStringFromAirline(CLIENT, request);
    }
    public static String getAircraftLink(String airline, String date, int flightNumber, String accessToken)
    {
        String url = String.format("https://api.seatmaps.com/api/v1/schedule/for/flight/%s/%d/%s?lang=EN", airline, flightNumber, date);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(HEADER_ACCEPT,ACCEPT_SEATMAPS)
                .header(HEADER_ACCEPT_ENCODING, ACCEPT_ENCODING_SEATMAPS)
                .header(HEADER_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_SEATMAPS)
                .header(HEADER_AUTHORIZATION, "Bearer " + accessToken)
                .header(HEADER_DNT, DNT_SEATMAPS)
                .header(HEADER_ORIGIN, REFERER_SEATMAPS)
                .header(HEADER_REFERER, REFERER_SEATMAPS)
                .header(HEADER_SEC_CH_UA, SEC_CH_UA_SEATMAPS)
                .header(HEADER_SEC_CH_UA_MOBILE, SEC_CH_UA_MOBILE_SEATMAPS)
                .header(HEADER_SEC_CH_UA_PLATFORM, SEC_CH_UA_PLATFORM_SEATMAPS)
                .header(HEADER_SEC_FETCH_DEST, SEC_FETCH_DEST_SEATMAPS)
                .header(HEADER_SEC_FETCH_MODE, SEC_FETCH_MODE_SEATMAPS)
                .header(HEADER_USER_AGENT, USER_AGENT_SEATMAPS)
                .header(HEADER_SEC_FETCH_SITE, SEC_FETCH_SITE_SEATMAPS)
                .GET()
                .build();
        return getJsonStringFromAirline(CLIENT, request);
    }
}
