package ca.flymile.ModelAmerican;

import lombok.Data;

import java.util.List;

/**
 * The Segment class represents a segment of a flight journey.
 */
@Data
public class Segment {
    /**
     * The flight associated with this segment.
     */
    private Flight flight;

    /**
     * The legs of this segment.
     */
    private List<Leg> legs;
}
