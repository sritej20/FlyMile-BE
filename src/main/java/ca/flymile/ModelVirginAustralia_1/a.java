package ca.flymile.ModelVirginAustralia_1;
import com.google.gson.Gson;

import static ca.flymile.API.RequestHandlerVirginAustralia.requestHandlerVirginAustralia;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Objects;


class Root {
    Data data;
}

class Data {
    BookingAirSearch bookingAirSearch;
}

class BookingAirSearch {
    OriginalResponse originalResponse;
}

class OriginalResponse {
    List<List<UnbundledOffer>> unbundledOffers;
    BrandedResults brandedResults;
}

class UnbundledOffer {
    boolean soldout;
    SeatsRemaining seatsRemaining;
    List<ItineraryPart> itineraryPart;
    String cabinClass;
    Fare fare;
    Taxes taxes;
}

class SeatsRemaining {
    int count;
}

class ItineraryPart {
    @SerializedName("@type")
    String type;
    @SerializedName("@id")
    String id;
    private List<Advisory> advisories;
    List<Object> segments; // Use Object to accommodate both Segment and SegmentReference
    int stops;
    int totalDuration;
    List<ConnectionInformation> connectionInformations;

}
class Advisory {
  String code;
}

class Segment {
    @SerializedName("@type")
    String type;
    @SerializedName("@id")
    String id;
    int duration;
    String cabinClass;
    String equipment;
    Flight flight;
    String origin;
    String destination;
    String departure;
    String arrival;
    int layoverDuration;
}


class Flight {
    int operatingFlightNumber;
    String operatingAirlineCode;
}


class ConnectionInformation {
    @SerializedName("@id")
    String id;
    int duration;
}

class OfferInformation {
    boolean discounted;
    boolean negotiated;
    String negotiatedType;
}

class Amount {
    double amount;
}

class Fare {
    List<List<Amount>> alternatives;
}

class Taxes {
    List<List<Amount>> alternatives;
}

class BrandedResults {
    List<List<ItineraryPartBrand>> itineraryPartBrands;
}

class ItineraryPartBrand {
    ItineraryPartReference itineraryPart;
    List<BrandOffer> brandOffers;
}

class ItineraryPartReference {
    @SerializedName("@ref")
    String ref;
}

class BrandOffer {

    String brandId;
    boolean soldout;
    SeatsRemaining seatsRemaining;
    String cabinClass;
    Fare fare;
    Taxes taxes;
}

class SegmentReference {
    @SerializedName("@ref")
    String ref;
}

// The JsonParser class remains unchanged.


// Example of parsing JSON into Java objects
class JsonParser {
    public static void main(String[] args) throws Exception {

        //String json = requestHandlerVirginAustralia("LHR", "JFK", "2024-06-12", 1);
        String json = requestHandlerVirginAustralia("ATL", "LGA", "2024-04-18", 1);

        System.out.println(json);
        Gson gson = new Gson();

        // Deserialize JSON to Java object
        Root root = gson.fromJson(json, Root.class);

        // Now you can access the data through the 'root' object
        System.out.println(root);

        // Serialize Java object back to JSON
        String jsonBack = gson.toJson(root);

        // Print the converted JSON string
        System.out.println(jsonBack);
    }

}



// Any other nested or complex types should be defined here...

// For example, if there are additional complex types inside BrandOffer or other parts, 
// define their classes here with properties and JsonProperty annotations as done above.

