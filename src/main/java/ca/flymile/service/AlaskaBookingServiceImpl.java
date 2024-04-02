package ca.flymile.service;

import org.springframework.stereotype.Service;

@Service
public class AlaskaBookingServiceImpl implements BookingService{
    private static final String BASE_URL = "https://www.alaskaair.com/search/results";
    private static final String TEMPLATE_URL = BASE_URL + "?O=%s&D=%s&OD=%s&A=%d&C=0&L=0&RT=false&ShoppingMethod=onlineaward";

    @Override
    public String getBookingLink(String origin, String destination, String date, int numPassengers) {
        return TEMPLATE_URL.formatted(origin, destination, date, numPassengers);
    }
}
