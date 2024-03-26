package ca.flymile.ModelDeltaMonthly;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TotalAmount {
    @SerializedName("currencyEquivalentPrice")
    private CurrencyEquivalentPrice currencyEquivalentPrice;

    @SerializedName("milesEquivalentPrice")
    private MilesEquivalentPrice milesEquivalentPrice;
}
