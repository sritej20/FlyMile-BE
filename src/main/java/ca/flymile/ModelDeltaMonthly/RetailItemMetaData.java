package ca.flymile.ModelDeltaMonthly;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
@Data
public class RetailItemMetaData {
    @SerializedName("fareInformation")
    private List<FareInformation> fareInformation;

}
