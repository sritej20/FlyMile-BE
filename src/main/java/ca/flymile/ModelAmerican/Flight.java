package ca.flymile.ModelAmerican;

import lombok.Data;

/**
 * The Flight class represents a flight.
 * Example : QR 464              QR = carrierCode   464 = flightNumber
 */
@Data
public class Flight {
    /**
     * The carrier code of the flight.
     */
    private String carrierCode;

    /**
     * The flight number.
     */
    private String flightNumber;
}
