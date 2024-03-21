package ca.flymile.ModelDelta;

import com.google.gson.Gson;

import static ca.flymile.API.RequestHandlerDeltaMonthly.requestHandlerDeltaMonthly;
import static ca.flymile.dtoDelta.DtoOffersMapper.toDto;

public class jsonParsor {
    public static void main(String[] args) {
        // Example JSON string (should be replaced with actual JSON string)
        String jsonString = requestHandlerDeltaMonthly("atl", "lhr", "2024-04-01", 1);
        Gson gson = new Gson();
        JsonResponse jsonResponse = gson.fromJson(jsonString, JsonResponse.class);

        // Now you can work with `jsonResponse` object
        //System.out.println(jsonResponse);
       // System.out.println(toDto(jsonResponse.getInfo().getGqlSearchOffers().getGqlOffersSets()));
        // Add more print statements or logic as needed to use the data
String jsonOutput = gson.toJson(toDto(jsonResponse.getInfo().getGqlSearchOffers().getGqlOffersSets()).getDtoOffers());

    }
}
