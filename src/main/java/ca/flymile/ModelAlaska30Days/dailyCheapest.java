package ca.flymile.ModelAlaska30Days;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class dailyCheapest
{
    @SerializedName("awardPoints")
    private int points;
    @SerializedName("price")
    private double cashPrice;
    private String date;

}
