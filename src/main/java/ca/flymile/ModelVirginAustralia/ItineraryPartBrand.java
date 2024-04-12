package ca.flymile.ModelVirginAustralia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class ItineraryPartBrand {
    private ItineraryPart itineraryPart;
    private List<BrandOffer> brandOffers;
    private int duration;
    private String departure;
    private String arrival;
}

