package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CurrencyEquivalentPrice {
    @SerializedName("roundedCurrencyAmt")
    private int roundedCurrencyAmt;
}