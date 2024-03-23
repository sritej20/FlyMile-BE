package ca.flymile.ModelDelta;

import com.google.gson.Gson;

import static ca.flymile.API.RequestHandlerDeltaMonthly.requestHandlerDeltaMonthly;
import static ca.flymile.dtoDelta.DtoOffersMapper.toDto;

public class jsonParsor {
    public static void main(String[] args) {

        String jsonString = requestHandlerDeltaMonthly("atl", "lhr", "2024-04-01", 1);
        Gson gson = new Gson();
        JsonResponse jsonResponse = gson.fromJson(jsonString, JsonResponse.class);

        String jsonOutput = gson.toJson(toDto(jsonResponse.getInfo().getGqlSearchOffers().getGqlOffersSets()).getDtoOffers());

    }
}
