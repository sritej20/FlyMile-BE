package ca.flymile.dtoAlaska;

import ca.flymile.Flight.LegDto;
import ca.flymile.ModelAlaska.Leg;



/**
 * Provides functionality to map data from domain models to data transfer objects (DTOs) for flight legs.
 */
public class LegMapper {

    /**
     * Converts a Leg object to a LegDto object.
     * This method maps various flight leg details such as aircraft type, arrival and departure times,
     * connection time, carrier code, flight number, amenities (including a seat type), destination,
     * duration, and origin from the Leg domain model to the LegDto object.
     *
     * @param leg The Leg object containing the flight leg information to be mapped.
     * @return A LegDto object containing the mapped flight leg data.
     */
    public static LegDto toDto(Leg leg) {
        return new LegDto()
                .setAircraft(leg.getAircraft())
                .setArrivalDateTime(leg.getArrivalDateTime().substring(0,16))
                .setConnectionTimeInMinutes(leg.getConnectionTimeInMinutes())
                .setDepartureDateTime(leg.getDepartureDateTime().substring(0,16))
                .setCarrierCode(leg.getFlight().getCarrierCode())
                .setFlightNumber(leg.getFlight().getFlightNumber())
                .setDestination(leg.getDestination())
                .setDurationInMinutes(leg.getDurationInMinutes())
                .setOrigin(leg.getOrigin());
    }
}
