package ca.flymile.dtoDelta;
import ca.flymile.Flight.FlightDto;
import ca.flymile.ModelDelta.*;
import static ca.flymile.dtoDelta.LegMapper.mapLegs;
import static ca.flymile.dtoDelta.PricingDetailMapper.mapPricingDetails;

public class FlightMapper {

    public static FlightDto toDto(GqlOffersSets gqlOffersSets) {
        FlightDto flightDto = new FlightDto();

        // Map pricing details
        flightDto.setPricingDetail(mapPricingDetails(gqlOffersSets));

        // Map legs and set total trip details
        flightDto.setLegs(mapLegs(gqlOffersSets));
        gqlOffersSets.getTrips().stream().findFirst().ifPresent(trip -> {
            flightDto.setSourceAirline("DL");
            flightDto.setDuration(calculateTotalDuration(trip.getTotalTripTime()));
            flightDto.setArrivesNextDay(trip.getTotalTripTime().getDayCnt());
        });

        return flightDto;
    }




    public static int calculateTotalDuration(TotalTripTime totalTripTime) {
        return totalTripTime.getHourCnt() * 60 + totalTripTime.getMinuteCnt();
    }

    public static int calculateDuration(Duration duration) {
        return duration.getHourCnt() * 60 + duration.getMinuteCnt();
    }
}
