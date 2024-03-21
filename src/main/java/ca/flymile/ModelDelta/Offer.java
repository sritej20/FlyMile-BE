package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
@Data
public  class Offer {


    @SerializedName("offerItems")
    private List<OfferItem> offerItems;




    @SerializedName("offerPricing")
    private List<OfferPricing> offerPricing;

    // Getters
}