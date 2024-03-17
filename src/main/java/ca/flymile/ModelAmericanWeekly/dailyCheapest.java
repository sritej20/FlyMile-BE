package ca.flymile.ModelAmericanWeekly;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class dailyCheapest {
    private String date;
    @SerializedName("perPassengerAwardPointsTotal")
    private int points;
    @SerializedName("price")
    private double cashPrice;
}
