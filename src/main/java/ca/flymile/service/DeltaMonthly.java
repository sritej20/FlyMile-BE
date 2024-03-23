package ca.flymile.service;
import ca.flymile.ModelDelta.GqlSearchOffers;
import ca.flymile.ModelDelta.Info;
import ca.flymile.ModelDelta.JsonResponse;
import ca.flymile.ModelDelta.OfferSet;
import ca.flymile.dtoDelta.DtoOffers;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static ca.flymile.API.RequestHandlerDeltaMonthly.requestHandlerDeltaMonthly;
import static ca.flymile.dtoDelta.DtoOffersMapper.toDto;
@Component
public class DeltaMonthly {
    private static final Gson gson = new Gson();

    public static List<DtoOffers> getDailyCheapestS(String origin, String destination, String start, int numPassengers) {
        String json = requestHandlerDeltaMonthly(origin, destination, start, numPassengers);
        if (json == null || json.startsWith("<")) {
            return new ArrayList<>();
        }

        JsonResponse jsonResponse = gson.fromJson(json, JsonResponse.class);
        if (jsonResponse == null) {
            return Collections.emptyList();
        }

        Info info = jsonResponse.getInfo();
        if (info == null) {
            return Collections.emptyList();
        }

        GqlSearchOffers gqlSearchOffers = info.getGqlSearchOffers();
        if (gqlSearchOffers == null) {
            return Collections.emptyList();
        }

        List<OfferSet> gqlOffersSets = gqlSearchOffers.getGqlOffersSets();
        if (gqlOffersSets == null) {
            return Collections.emptyList();
        }

        List<DtoOffers> dtoOffers = toDto(gqlOffersSets).getDtoOffers();
        return dtoOffers != null ? dtoOffers : Collections.emptyList();
    }


}
