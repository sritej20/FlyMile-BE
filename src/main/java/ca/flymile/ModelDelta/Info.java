package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public  class Info
{
    @SerializedName("gqlSearchOffers")
    private GqlSearchOffers gqlSearchOffers;

}
