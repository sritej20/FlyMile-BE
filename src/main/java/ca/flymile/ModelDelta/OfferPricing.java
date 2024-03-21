package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OfferPricing {

    @SerializedName("totalAmt")
    private TotalAmount totalAmt;


}
