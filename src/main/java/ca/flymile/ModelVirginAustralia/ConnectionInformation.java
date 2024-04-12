package ca.flymile.ModelVirginAustralia;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ConnectionInformation {
    @SerializedName("@id")
    private String id;
    private int duration;
    private boolean changeOfAirport;
}