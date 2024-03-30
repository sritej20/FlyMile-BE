package ca.flymile.API;

import lombok.Data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.UUID;

import static ca.flymile.API.BaseRequestHandler.getJsonStringFromAirline;
import static ca.flymile.API.PassengerJsonProvider.getPassengersJson;
@Data
public class RequestBuilder {

    // American Airlines constants
    private static final String ACCEPT_AMERICAN = "application/json, text/plain, */*";
    private static final String ACCEPT_ENCODING_AMERICAN = "gzip, deflate";
    private static final String ACCEPT_LANGUAGE_AMERICAN = "en-US,en;q=0.9";
    private static final String CONTENT_TYPE_AMERICAN = "application/json";
    private static final String ORIGIN_AMERICAN = "https://www.aa.com";
    private static final String REFERER_AMERICAN = "https://www.aa.com/booking/choose-flights/1";
    private static final String USER_AGENT_AMERICAN = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36";
    private static final String UPPER_CLASS_AMERICAN = "BUSINESS,FIRST";
    private static final String LOWER_CLASS_AMERICAN = "";

    // Alaska Airlines constants
    private static final String ACCEPT_ALASKA = "*/*";
    private static final String ACCEPT_ENCODING_ALASKA = "gzip, deflate, br";
    private static final String ACCEPT_LANGUAGE_ALASKA = "en-CA,en-US;q=0.9,en;q=0.8";
    private static final String CONTENT_TYPE_ALASKA = "text/plain;charset=UTF-8";
    private static final String ORIGIN_ALASKA = "https://www.alaskaair.com";
    private static final String USER_AGENT_ALASKA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.1 Safari/605.1.15";

    // Delta Airlines constants
    private static final String ACCEPT_DELTA = "application/json, text/plain, */*";
    private static final String ACCEPT_ENCODING_DELTA = "gzip, deflate, br";
    private static final String ACCEPT_LANGUAGE_DELTA = "en-CA,en-US;q=0.9,en;q=0.8";
    private static final String CONTENT_TYPE_DELTA = "application/json";
    private static final String ORIGIN_DELTA = "https://www.delta.com";
    private static final String REFERER_DELTA = "https://www.delta.com/";
    private static final String USER_AGENT_DELTA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.1 Safari/605.1.15";
    private static final String UPPER_CLASS_DELTA = "D1";
    private static final String LOWER_CLASS_DELTA = "BE";
    private static final String AUTHORIZATION_HEADER_DELTA = "Authorization";
    private static final String AUTHORIZATION_DELTA = "GUEST";
    private static final String TRANSACTION_ID_HEADER_DELTA = "TransactionId";
    private static final String APPLICATION_ID_HEADER_DELTA = "applicationId";
    private static final String CHANNEL_ID_HEADER_DELTA = "channelId";
    private static final String AIRLINE_DELTA_HEADER = "Airline";
    private static final String APPLICATION_ID_VALUE_DELTA = "DC";
    private static final String CHANNEL_ID_VALUE_DELTA = "DCOM";
    private static final String AIRLINE_VALUE_DELTA = "DL";

    // Common header keys
    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_ORIGIN = "Origin";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_REFERER = "Referer";


    static String buildRequestBodyAmerican(String origin, String destination, String departureDate, int numberOfPassengers, boolean upperCabin, String maxStops, String requestTemplate, String requestUrl, HttpClient client) {
        String requestBody = String.format(requestTemplate, numberOfPassengers, upperCabin ? UPPER_CLASS_AMERICAN : LOWER_CLASS_AMERICAN, departureDate, destination, maxStops, origin);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header(HEADER_ACCEPT_ENCODING, ACCEPT_ENCODING_AMERICAN)
                .header(HEADER_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_AMERICAN)
                .header(HEADER_ACCEPT, ACCEPT_AMERICAN)
                .header(HEADER_CONTENT_TYPE, CONTENT_TYPE_AMERICAN)
                .header(HEADER_ORIGIN, ORIGIN_AMERICAN)
                .header(HEADER_REFERER, REFERER_AMERICAN)
                .header(HEADER_USER_AGENT, USER_AGENT_AMERICAN)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        return getJsonStringFromAirline(client, request);
    }

    static String buildRequestBodyAlaska(String origin, String destination, String departureDate, int numberOfPassengers, String requestTemplate, String requestUrl, HttpClient client) {
        String requestBody = String.format(requestTemplate, origin, destination, departureDate, numberOfPassengers);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header(HEADER_ACCEPT_ENCODING, ACCEPT_ENCODING_ALASKA)
                .header(HEADER_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_ALASKA)
                .header(HEADER_ACCEPT, ACCEPT_ALASKA)
                .header(HEADER_CONTENT_TYPE, CONTENT_TYPE_ALASKA)
                .header(HEADER_ORIGIN, ORIGIN_ALASKA)
                .header(HEADER_USER_AGENT, USER_AGENT_ALASKA)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return getJsonStringFromAirline(client, request);
    }

    static String buildRequestBodyDeltaMonthly(String origin, String destination, boolean cabin, String departureDate, int numPassengers, String requestTemplate, boolean nonStopOnly, String requestUrl, HttpClient client) {
        String requestBody = String.format(requestTemplate, getPassengersJson(numPassengers), nonStopOnly, cabin ? UPPER_CLASS_DELTA : LOWER_CLASS_DELTA, departureDate, destination, origin);
        return executeHttpRequestDelta(requestUrl, client, requestBody);
    }

    static String buildRequestBodyDelta(String origin, String destination, boolean cabin, String departureDate, int numPassengers, boolean nonStopOnly, String requestTemplate, String requestUrl, HttpClient client) {
        String requestBody = String.format(requestTemplate, nonStopOnly, departureDate, destination, origin, cabin ? UPPER_CLASS_DELTA : LOWER_CLASS_DELTA, getPassengersJson(numPassengers));
        return executeHttpRequestDelta(requestUrl, client, requestBody);
    }

    private static String executeHttpRequestDelta(String requestUrl, HttpClient client, String requestBody) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header(HEADER_ACCEPT_ENCODING, ACCEPT_ENCODING_DELTA)
                .header(HEADER_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_DELTA)
                .header(HEADER_ACCEPT, ACCEPT_DELTA)
                .header(HEADER_CONTENT_TYPE, CONTENT_TYPE_DELTA)
                .header(HEADER_ORIGIN, ORIGIN_DELTA)
                .header(HEADER_REFERER, REFERER_DELTA)
                .header(AUTHORIZATION_HEADER_DELTA, AUTHORIZATION_DELTA)
                .header(TRANSACTION_ID_HEADER_DELTA, UUID.randomUUID().toString())
                .header(APPLICATION_ID_HEADER_DELTA, APPLICATION_ID_VALUE_DELTA)
                .header(CHANNEL_ID_HEADER_DELTA, CHANNEL_ID_VALUE_DELTA)
                .header(AIRLINE_DELTA_HEADER, AIRLINE_VALUE_DELTA)
                .header(HEADER_USER_AGENT, USER_AGENT_DELTA)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return getJsonStringFromAirline(client, request);
    }
}
