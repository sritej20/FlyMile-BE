package ca.flymile.service;

public interface BookingService {
    String getBookingLink(String origin, String destination, String date, int numPassengers);
}
