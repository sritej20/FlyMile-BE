package ca.flymile.ModelAmerican;

import lombok.Data;

/**
 * The Location class represents a location.
 * Location Holds something like JFK or YUL or BEY
 */
@Data
public class Location {
    /**
     * The code of the location.
     */
    private String code;
}
