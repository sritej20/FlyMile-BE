package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public  class RetailItem {
    @SerializedName("retailItemMetaData")
    private RetailItemMetaData retailItemMetaData;
}
