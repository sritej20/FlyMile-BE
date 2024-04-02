package ca.flymile.service;

import org.springframework.stereotype.Service;

@Service
public class DeltaBookingServiceImpl implements BookingService{
    private static final String BASE_URL = "https://www.delta.com/flight-search/search";
    private static final String TEMPLATE_URL = BASE_URL
            + "?action=findFlights&searchByCabin=true&deltaOnlySearch=false&deltaOnly=off&go=Find%%20Flights"
            + "&tripType=ONE_WAY&passengerInfo=ADT:%d&priceSchedule=price&awardTravel=true"
            + "&originCity=%s&destinationCity=%s&departureDate=%s&returnDate=&forceMiles=true";
    @Override
    public String getBookingLink(String origin, String destination, String date, int numPassengers) {
        return TEMPLATE_URL.formatted(numPassengers, origin, destination, date);
    }

}
