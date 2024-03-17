package ca.flymile.ModelAmerican;

import lombok.Data;
import java.util.List;

/**
 * The FlightSlices class represents a collection of flight slices.
 * Error codes:
 * - "" (empty string): Indicates no error.
 * - "309": Indicates no flights found for the given parameters.
 * - Other errors: Possible errors that are logged for future knowledge.
 */
@Data
public class FlightSlices {
    /**
     * Any error message associated with the flight slices.
     */
    private String error;

    /**
     * The list of flight slices.
     */
    private List<Slice> slices;
}
