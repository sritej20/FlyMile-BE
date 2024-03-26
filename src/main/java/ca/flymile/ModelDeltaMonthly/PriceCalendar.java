package ca.flymile.ModelDeltaMonthly;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public  class PriceCalendar {
    @SerializedName("priceCalendarDate")
    private String priceCalendarDate;
}