package ca.flymile.dtoAlaska;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Represents the data transfer object for flight information, encapsulating details
 * such as flight duration, arrival and departure times, segments, and pricing details.
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightDto {

    /**
     * The total duration of the flight in minutes.
     */
    private int duration;

    /**
     * The date and time when the flight arrives.
     */
    private String arrivalDateTime;

    /**
     * The date and time when the flight departs.
     */
    private String departureDateTime;

    /**
     * Indicates if the flight arrives the next day. It is always set to 0 for Alaska flights.
     */
    private int arrivesNextDay;

    /**
     * A list of LegDto objects representing the legs of the flight.
     */
    private List<LegDto> Legs;

    /**
     * A list of PricingDetailDto objects representing the pricing details for the flight.
     */
    private List<PricingDetailDto> pricingDetail;
}
