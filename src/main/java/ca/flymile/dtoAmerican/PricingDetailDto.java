package ca.flymile.dtoAmerican;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.*;
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
    @SerializedName("perPassengerAwardPoints")
    private int points;

    /**
     * The taxes and fees price per passenger.
     */
    @SerializedName("perPassengerTaxesAndFees")
    private double cashPrice;

    /**
     * The product type of the flight.
     * Possible values: ECONOMY, CABIN, MAIN, PREMIUM_ECONOMY, BUSINESS, FIRST.
     * Should be changed to ENUM
     */
    private String productType;

    /**
     * The number of seats remaining.
     * Note: A value of 0 does not necessarily mean there are no seats available.
     * However, A specific number indicates the exact number of available seats.
     */
    private int seatsRemaining;
}
