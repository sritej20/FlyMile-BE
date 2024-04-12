package ca.flymile.ModelVirginAustralia;

import lombok.Data;

import java.util.List;

@Data
 public class UnbundledAlternateDateOffer {
        public String brandId;
        public boolean soldout;
        public SeatsRemaining seatsRemaining;
        public List<ItineraryPart> itineraryPart;
        public String cabinClass;
        public OfferInformation offerInformation;
        public String status;
        public List<String> departureDates;
        public Total total;
        public Fare fare;
        public RequiredMinThreshold requiredMinThreshold;
        public Taxes taxes;
    }




