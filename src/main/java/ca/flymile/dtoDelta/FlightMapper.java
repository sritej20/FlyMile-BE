package ca.flymile.dtoDelta;
import ca.flymile.Flight.FlightDto;
import ca.flymile.FlyMileAirportData.CurrencyRetriever;
import ca.flymile.ModelDelta.*;

import java.util.UUID;

import static ca.flymile.dtoDelta.LegMapper.mapLegs;
import static ca.flymile.dtoDelta.PricingDetailMapper.mapPricingDetails;

public class FlightMapper {

    public static FlightDto toDto(GqlOffersSets gqlOffersSets, String origin) {
        FlightDto flightDto = new FlightDto();
        String currency = CurrencyRetriever.getValidCurrencyForAirport(origin);
        flightDto.setPricingDetail(mapPricingDetails(gqlOffersSets, currency));

        // Map legs and set total trip details
        flightDto.setLegs(mapLegs(gqlOffersSets));
        gqlOffersSets.getTrips().stream().findFirst().ifPresent(trip -> {
            flightDto.setSourceAirline("DL");
            flightDto.setFlightID(UUID.randomUUID().toString());
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
