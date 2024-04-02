package ca.flymile.service;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AmericanBookingServiceImpl implements BookingService{
    private static final String BASE_URL = "https://www.aa.com/booking/search?locale=en_US";
    @Override
    public String getBookingLink(String origin, String destination, String date, int numPassengers) {
        return generateUrl(origin, destination, date, numPassengers);
    }

    private static String generateUrl(String origin, String destination, String date,  int numPassengers) {
        final String slicePattern = "[{\"orig\":\"%s\",\"origNearby\":false,\"dest\":\"%s\",\"destNearby\":false,\"date\":\"%s\"}]";

        String slice = String.format(slicePattern, origin, destination, date);
        String encodedSlice = URLEncoder.encode(slice, StandardCharsets.UTF_8);

        return String.format("%s&pax=%d&adult=%d&type=OneWay&searchType=Award&cabin=&carriers=ALL&slices=%s",
                BASE_URL, numPassengers, numPassengers, encodedSlice);
    }
}
