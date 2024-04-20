package ca.flymile.ModelBookingDotCom;

import lombok.Data;

import java.util.List;
@Data
public class BookingDotComModel {
    private Aggregation aggregation;
    public MinPriceRound getMinPriceRound() {
        for (Stop stop : aggregation.getStops())
            if(stop.getMinPriceRound() != null)
                return stop.getMinPriceRound();
        return null;
    }
}
