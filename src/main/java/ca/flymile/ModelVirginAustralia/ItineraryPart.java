package ca.flymile.ModelVirginAustralia;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
@Data
public class ItineraryPart {
    @SerializedName("@type")
    private String type;

    @SerializedName("@id")
    private String id;
    private List<Segment> segments;
    private int stops;
    private int totalDuration;
    private List<ConnectionInformation> connectionInformations;
    private String bookingClass;
    private List<String> programIDs;
    private List<String> programCodes;
    private List<Advisory> advisories;
}
