package ca.flymile.dtoDelta;

import ca.flymile.Flight.LegDto;
import ca.flymile.ModelDelta.FlightSegment;
import ca.flymile.ModelDelta.GqlOffersSets;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.flymile.dtoDelta.AircraftTypeMapUtility.getAircraftTypeName;
import static ca.flymile.dtoDelta.FlightMapper.calculateDuration;

public class LegMapper {
    public static List<LegDto> mapLegs(GqlOffersSets gqlOffersSets) {
        return gqlOffersSets.getTrips().stream()
                .flatMap(trip -> trip.getFlightSegment().stream())
                .map(LegMapper::mapToLeg)
                .collect(Collectors.toList());
    }

    private static LegDto mapToLeg(FlightSegment segment) {
        LegDto leg = new LegDto()
                .setAircraft(getAircraftTypeName(segment.getAircraftTypeCode()))
                .setCarrierCode(segment.getOperatingCarrier().getCarrierCode())
                .setFlightNumber(segment.getOperatingCarrier().getCarrierNum())
                .setArrivalDateTime(segment.getScheduledArrivalLocalTs())
                .setDepartureDateTime(segment.getScheduledDepartureLocalTs())
                .setDestination(segment.getDestinationAirportCode())
                .setOrigin(segment.getOriginAirportCode())
                .setDurationInMinutes(calculateDuration(segment.getFlightLeg().get(0).getDuration()));

        Optional.ofNullable(segment.getLayover())
                .ifPresent(layover -> leg.setConnectionTimeInMinutes(calculateDuration(layover.getLayoverDuration())));

        return leg;
    }
}
