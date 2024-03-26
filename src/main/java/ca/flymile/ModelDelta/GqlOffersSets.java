package ca.flymile.ModelDelta;

import lombok.Data;

import java.util.List;
@Data
public class GqlOffersSets {
    private List<Trip> trips;
    private List<Offer> offers;
}
