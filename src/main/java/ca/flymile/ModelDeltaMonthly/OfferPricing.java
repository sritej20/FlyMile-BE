package ca.flymile.ModelDeltaMonthly;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OfferPricing {

    @SerializedName("totalAmt")
    private TotalAmount totalAmt;


}
