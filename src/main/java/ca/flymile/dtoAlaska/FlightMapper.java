package ca.flymile.dtoAlaska;


import ca.flymile.Flight.FlightDto;
import ca.flymile.ModelAlaska.Slice;

import java.util.UUID;
import java.util.stream.Collectors;

import static ca.flymile.dtoAlaska.PricingDetailMapper.toDtoList;

/**
 * Provides functionality to toDto data from domain models to data transfer objects (DTOs) for flights.
 */
public class FlightMapper {

    /**
     * Converts a Slice object to a FlightDto object.
     * This method maps the duration, arrival indicator, legs, and pricing details
     * from the Slice domain model to the FlightDto object.
     *
     * @param slice The Slice object containing the flight information to be mapped.
     * @return A FlightDto object containing the mapped flight data.
     */
    public static FlightDto toDto(Slice slice) {
        return new FlightDto()
                .setDuration(slice.getDuration())
                .setSourceAirline("AS")
                .setFlightID(UUID.randomUUID().toString())
                .setArrivesNextDay(slice.getArrivesNextDay())
                .setLegs(slice.getLegs().stream()
                        .map(LegMapper::toDto)
                        .collect(Collectors.toList()))
                .setPricingDetail(toDtoList(slice.getFares()));
    }
}