package ca.flymile.ModelVirginAustralia;

import lombok.Data;

import java.util.Map;
@Data
public class BrandOffer {
    private int shoppingBasketHashCode;
    private String brandId;
    private boolean soldout;
    private SeatsRemaining seatsRemaining;
    private String cabinClass;
    private OfferInformation offerInformation;
    private Total total;
    private Fare fare;
    private Map<String, Object> totalMandatoryObFees;
    private RequiredMinThreshold requiredMinThreshold;
    private Taxes taxes;
}