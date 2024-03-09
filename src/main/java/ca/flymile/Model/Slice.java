package ca.flymile.Model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The Slice class represents a slice of a flight itinerary.
 */
@Data
public class Slice {
    /**
     * The duration of the slice in minutes.
     */
    @SerializedName("durationInMinutes")
    private int duration;

    /**
     * The date and time of arrival for this slice.
     */
    private String arrivalDateTime;

    /**
     * The date and time of departure for this slice.
     */
    private String departureDateTime;

    /**
     * Indicates whether the arrival is on the next day.
     * 0: Arrival is on the same day.
     * 1: Arrival is on the next day.
     * and So On...
     * could be byte ???
     */
    private int arrivesNextDay;

    /**
     * The segments comprising this slice.
     */
    private List<Segment> segments;

    /**
     * The pricing details for this slice.
     */
    private List<PricingDetail> pricingDetail;
}
