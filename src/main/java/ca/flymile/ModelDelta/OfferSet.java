package ca.flymile.ModelDelta;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
@Data
public  class OfferSet {
    @SerializedName("offers")
    private List<Offer> offers;

}
