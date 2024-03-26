package ca.flymile.ModelAlaska;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The Slice class represents a slice of a flight itinerary.
 */
@Data
public class Slice {

    /**
     * The total duration of the slice in minutes.
     */
    private int duration;

    /**
     * Indicates whether the arrival is on the next day. It is always set to 0 for Alaska Airlines as the
     * concept of next-day arrival is not applicable or used. Typically, 0 indicates arrival on the same day,
     * while 1 would indicate next-day arrival.
     */
    private int arrivesNextDay = 0;

    /**
     * A map of fare details associated with this slice, where the key is the product type, and the value
     * is the pricingDetails object containing the fare information.
     */
    private Map<String, pricingDetails> fares;

    /**
     * The list of legs (individual flight segments) that comprise this slice. Each leg represents a distinct
     * portion of the journey.
     */
    @SerializedName("segments")
    private List<Leg> legs;
}
