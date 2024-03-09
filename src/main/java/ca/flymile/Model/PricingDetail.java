package ca.flymile.Model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The PricingDetail class represents pricing details for a flight.
 */
@Data
public class PricingDetail {
    /**
     * The number of award points per passenger.
     */
    @SerializedName("perPassengerAwardPoints")
    private int points;

    /**
     * The taxes and fees price per passenger.
     */
    @SerializedName("perPassengerTaxesAndFees")
    private Price cashPrice;

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
