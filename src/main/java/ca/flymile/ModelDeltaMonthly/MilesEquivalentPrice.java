package ca.flymile.ModelDeltaMonthly;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class MilesEquivalentPrice {
    @SerializedName("mileCnt")
    private int mileCnt;

}
