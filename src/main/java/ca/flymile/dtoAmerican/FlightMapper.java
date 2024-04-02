package ca.flymile.dtoAmerican;

import ca.flymile.Flight.FlightDto;
import ca.flymile.ModelAmerican.Slice;

import java.util.stream.Collectors;

/**
 * Provides functionality to toDto data from domain models to data transfer objects (DTOs) for flights.
 */
public class FlightMapper {

    /**
     * Converts a Slice object to a FlightDto object.
     * This method maps the duration, arrival and departure times, next day arrival indicator,
     * legs, and pricing details from the Slice domain model to the FlightDto object.
     * Each leg of the flight is mapped using the LegMapper, and pricing details are converted using the PricingDetailMapper.
     *
     * @param slice The Slice object containing the flight information to be mapped.
     * @return A FlightDto object containing the mapped flight data, including details of each segment and pricing information.
     */
    public static FlightDto toDto(Slice slice) {
        return new FlightDto()
                .setDuration(slice.getDuration())
                .setSourceAirline("AA")
                .setArrivesNextDay(slice.getArrivesNextDay())
                .setLegs(slice.getSegments().stream()
                        .flatMap(segment -> segment.getLegs().stream()
                                .map(leg -> LegMapper.toDto(leg)
                                        .setCarrierCode(segment.getFlight().getCarrierCode())
                                        .setFlightNumber(segment.getFlight().getFlightNumber())))
                        .collect(Collectors.toList()))
                .setPricingDetail(slice.getPricingDetail().stream()
                        .map(PricingDetailMapper::toDto)
                        .collect(Collectors.toList()));
    }
}