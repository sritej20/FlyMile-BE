package ca.flymile.ModelVirginAustralia;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Segment {

    @SerializedName("@type")
    private String type;

    @SerializedName("@id")
    private String id;

    private SegmentOfferInformation segmentOfferInformation;
    private int duration;
    private String cabinClass;
    private String equipment;
    private Flight flight;
    private String origin;
    private String destination;
    private String departure;
    private String arrival;
    private String bookingClass;
    private int layoverDuration;
    private String fareBasis;
    private boolean subjectToGovernmentApproval;
    private String fareComponentBeginAirport;
    private String fareComponentEndAirport;
    private String fareComponentDirectionality;
    private String segmentRef;
}
