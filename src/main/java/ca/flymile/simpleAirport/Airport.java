package ca.flymile.simpleAirport;

import lombok.Data;

/**
 * Represents an airport with a unique code and the city it is located in.
 * The {@link Data} annotation from Lombok automatically generates getter and setter methods,
 * as well as {@code toString}, {@code equals}, and {@code hashCode} methods.
 *
 * @author Your Name
 * @version 1.0
 */
@Data
public class Airport {

    /**
     * The IATA airport code, a three-letter code designating many airports around the world,
     * defined by the International Air Transport Association (IATA).
     */
    private String airportCode;

    /**
     * The name of the city where the airport is located. This can be useful for identifying
     * airports in larger metropolitan areas that may have multiple airports.
     */
    private String cityName;
}

