package ca.flymile.ModelBookingDotCom;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
@Data
public class MinPriceRound
{
    @SerializedName("currencyCode")
    private String currency;
    @SerializedName("units")
    private int amount;
}
