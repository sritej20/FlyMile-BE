package ca.flymile.dtoAmerican;

import ca.flymile.Flight.LegDto;
import ca.flymile.ModelAmerican.Leg;


/**
 * Provides functionality to map data from domain models to data transfer objects (DTOs) for flight legs.
 */
public class LegMapper {

    /**
     * Converts a Leg object to a LegDto object.
     * This method maps various flight leg details, including the aircraft name, arrival and departure times,
     * connection time, lie-flat seat availability, destination, duration, and origin from the Leg domain model
     * to the LegDto object.
     *
     * @param leg The Leg object containing the flight leg information to be mapped.
     * @return A LegDto object containing the mapped flight leg data, with detailed information about the leg.
     */
    public static LegDto toDto(Leg leg) {
        return new LegDto()
                .setAircraft(leg.getAircraft().getName())
                .setArrivalDateTime(leg.getArrivalDateTime().substring(0,16))
                .setConnectionTimeInMinutes(leg.getConnectionTimeInMinutes())
                .setDepartureDateTime(leg.getDepartureDateTime().substring(0,16))
                .setDestination(leg.getDestination().getCode())
                .setDurationInMinutes(leg.getDurationInMinutes())
                .setOrigin(leg.getOrigin().getCode());
    }
}
