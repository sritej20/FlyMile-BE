package ca.flymile.ModelAmericanWeekly;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class dailyCheapest {
    private String date;
    @SerializedName("perPassengerAwardPointsTotal")
    private String points;
    @SerializedName("price")
    private String cashPrice;
}
