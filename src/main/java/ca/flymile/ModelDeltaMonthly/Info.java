package ca.flymile.ModelDeltaMonthly;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public  class Info
{
    @SerializedName("gqlSearchOffers")
    private GqlSearchOffers gqlSearchOffers;

}
