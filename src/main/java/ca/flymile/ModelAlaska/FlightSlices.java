package ca.flymile.ModelAlaska;

import lombok.Data;
import java.util.List;

/**
 * The FlightSlices class represents a collection of flight slices.
 */
@Data
public class FlightSlices {

    /**
     * The list of flight slices.
     */
    private List<Slice> slices;
}
