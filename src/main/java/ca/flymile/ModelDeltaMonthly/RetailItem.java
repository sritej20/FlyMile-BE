package ca.flymile.ModelDeltaMonthly;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public  class RetailItem {
    @SerializedName("retailItemMetaData")
    private RetailItemMetaData retailItemMetaData;
}
