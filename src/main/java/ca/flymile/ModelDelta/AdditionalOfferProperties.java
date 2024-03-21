package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AdditionalOfferProperties {
    @SerializedName("offered")
    private boolean offered;

    @SerializedName("totalTripStopCnt")
    private int totalTripStopCnt;

    @SerializedName("dayOfMonthText")
    private String dayOfMonthText;

    @SerializedName("monthText")
    private String monthText;

    @SerializedName("lowestFareInCalendar")
    private Boolean lowestFareInCalendar;

}
