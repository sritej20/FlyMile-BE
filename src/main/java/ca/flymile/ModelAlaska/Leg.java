package ca.flymile.ModelAlaska;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The Segment class represents a segment of a flight journey.
 */
@Data
public class Leg {
    /**
     * The aircraft used for this leg of the journey.
     */
    private String aircraft;
    /**
     * The flight associated with this segment.
     */
    @SerializedName("displayCarrier")
    private Flight flight;


    /**
     * The date and time of arrival at the destination for this leg.
     */
    @SerializedName("arrivalTime")
    private String arrivalDateTime;

    /**
     * The connection time in minutes for this leg.
     */

    @SerializedName("stopoverDuration")
    private int connectionTimeInMinutes;

    /**
     * The date and time of departure for this leg.
     */
    @SerializedName("departureTime")
    private String departureDateTime;

    /**
     * Indicates if the seating is lie-flat for this leg.
     */
    List<String> amenities;

    /**
     * The destination location for this leg.
     */
    @SerializedName("arrivalStation")
    private String destination;

    /**
     * The duration of the leg in minutes.
     */

    @SerializedName("duration")
    private int durationInMinutes;

    /**
     * The origin location for this leg.
     */
    @SerializedName("departureStation")
    private String origin;

}
