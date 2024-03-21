package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public  class PriceCalendar {
    @SerializedName("priceCalendarDate")
    private String priceCalendarDate;
}