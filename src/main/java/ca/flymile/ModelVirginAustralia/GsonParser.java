package ca.flymile.ModelVirginAustralia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static ca.flymile.API.RequestHandlerVirginAustralia.requestHandlerVirginAustralia;
;

public class GsonParser {
    public static void main(String[] args) {
        String json = requestHandlerVirginAustralia("LHR", "JFK", "2024-06-12", 1);
        System.out.println(json);

        Gson gson = new Gson();
        Root root = gson.fromJson(json, Root.class);

        // Use gson1 for pretty printing
        Gson gson1 = new GsonBuilder().create();
        System.out.println(root);
        String prettyJson = gson1.toJson(root);

        System.out.println(prettyJson);
        // Assuming the getter methods are correctly defined in the Root class and its nested classes
        System.out.println(root.getData().getBookingAirSearch().getOriginalResponse().getUnbundledOffers().size());
    }
}