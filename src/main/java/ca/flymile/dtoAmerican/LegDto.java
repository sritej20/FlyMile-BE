package ca.flymile.dtoAmerican;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * The Leg class represents a leg of a flight journey.
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegDto {
    /**
     * The aircraft used for this leg of the journey.
     */
    private String aircraft;

    /**
     * The carrier code of the flight.
     */
    private String carrierCode;

    /**
     * The flight number.
     */
    private String flightNumber;

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
     * The destination location for this leg.
     */
    private String destination;

    /**
     * The duration of the leg in minutes.
     */
    private int durationInMinutes;

    /**
     * The origin location for this leg.
     */
    private String origin;
}
