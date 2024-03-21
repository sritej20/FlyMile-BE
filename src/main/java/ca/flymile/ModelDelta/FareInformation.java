package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FareInformation {
    @SerializedName("priceCalendar")
    private PriceCalendar priceCalendar;
}
