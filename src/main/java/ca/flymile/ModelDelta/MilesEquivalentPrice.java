package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class MilesEquivalentPrice {
    @SerializedName("mileCnt")
    private int mileCnt;

}
