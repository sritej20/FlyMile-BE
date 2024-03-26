package ca.flymile.ModelDelta;

import lombok.Data;

import java.util.List;
@Data
public class Offer {
    private String offerId;
    private List<OfferItem> offerItems;
}