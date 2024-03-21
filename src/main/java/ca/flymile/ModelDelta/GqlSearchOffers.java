package ca.flymile.ModelDelta;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
@Data
public  class GqlSearchOffers
{
    @SerializedName("gqlOffersSets")
    private List<OfferSet> gqlOffersSets;

    @SerializedName("offerDataList")
    private OfferDataList offerDataList;

}
