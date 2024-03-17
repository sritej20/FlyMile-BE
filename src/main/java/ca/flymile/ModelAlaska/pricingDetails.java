package ca.flymile.ModelAlaska;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class pricingDetails {
    @SerializedName("milesPoints")
    private int points;
    @SerializedName("grandTotal")
    private double cashPrice;
    private String productType;
    private int seatsRemaining;
    private boolean mixedCabin;



}