package ca.flymile.ModelVirginAustralia;

import lombok.Data;

import java.util.List;

@Data
public class UnbundledOffer {
    private int shoppingBasketHashCode;
    private String brandId;
    private boolean soldout;
    private SeatsRemaining seatsRemaining;
    private List<ItineraryPart> itineraryPart;
    private String cabinClass;
    private OfferInformation offerInformation;
    private Total total;
    private Fare fare;
    private RequiredMinThreshold requiredMinThreshold;
    private Taxes taxes;
}