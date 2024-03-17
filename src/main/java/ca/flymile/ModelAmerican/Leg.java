package ca.flymile.ModelAmerican;

import lombok.Data;

/**
 * The Leg class represents a leg of a flight journey.
 */
@Data
public class Leg {
    /**
     * The aircraft used for this leg of the journey.
     */
    private Aircraft aircraft;

    /**
     * The date and time of arrival at the destination for this leg.
     */
    private String arrivalDateTime;

    /**
     * The connection time in minutes for this leg.
     */
    private int connectionTimeInMinutes;

    /**
     * The date and time of departure for this leg.
     */
    private String departureDateTime;

    /**
     * Indicates if the seating is lie-flat for this leg.
     */
    private boolean lieFlat;

    /**
     * The destination location for this leg.
     */
    private Location destination;

    /**
     * The duration of the leg in minutes.
     */
    private int durationInMinutes;

    /**
     * The origin location for this leg.
     */
    private Location origin;
}
