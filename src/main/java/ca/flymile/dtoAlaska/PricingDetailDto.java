package ca.flymile.dtoAlaska;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * The PricingDetail class represents pricing details for a flight.
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingDetailDto {
    /**
     * The number of award points per passenger.
     */
    private int points;

    /**
     * The taxes and fees price per passenger.
     */
    private double cashPrice;

    /**
     * The product type of the flight.
     * Should be changed to ENUM
     */
    private String productType;
    /**
     * If this flight is mixed cabin : some part is first , and some id business but priced as first
     * Combination of Higher and Lower Class Cabin with Higher cabin PRICING.
     *
     */
    private boolean mixedCabin;

    /**
     * The number of seats remaining.
     * Note: A value of 0 does not necessarily mean there are no seats available.
     * However, A specific number indicates the exact number of available seats.
     */
    private int seatsRemaining;
}
