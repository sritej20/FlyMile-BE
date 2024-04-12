package ca.flymile.ModelVirginAustralia_1;



import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static ca.flymile.API.RequestHandlerVirginAustralia.requestHandlerVirginAustralia;

public class FlightMapper {

    private static final Gson gson = new Gson();

    public static FlightDto mapToFlightDto(String jsonData) {
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
        JsonObject data = jsonObject.getAsJsonObject("data");
        if (data == null) {
            throw new IllegalStateException("Data section is missing");
        }

        JsonObject bookingAirSearch = data.getAsJsonObject("bookingAirSearch");
        if (bookingAirSearch == null) {
            throw new IllegalStateException("BookingAirSearch section is missing");
        }

        JsonObject originalResponse = bookingAirSearch.getAsJsonObject("originalResponse");
        if (originalResponse == null) {
            throw new IllegalStateException("OriginalResponse section is missing");
        }

        JsonArray unbundledOffers = originalResponse.getAsJsonArray("unbundledOffers");
        if (unbundledOffers == null || unbundledOffers.size() == 0) {
            throw new IllegalStateException("No unbundled offers available");
        }

        JsonObject offer = unbundledOffers.get(0).getAsJsonArray().size() > 0 ? unbundledOffers.get(0).getAsJsonArray().get(0).getAsJsonObject() : null;
        if (offer == null) {
            throw new IllegalStateException("No offer available in unbundled offers");
        }

        FlightDto flight = new FlightDto();
        JsonElement totalDurationElement = offer.get("totalDuration");
        if (totalDurationElement != null) {
            flight.setDuration(totalDurationElement.getAsInt());
        }

        flight.setSourceAirline("AC"); // Assuming the airline code is constant for simplification

        List<LegDto> legs = new ArrayList<>();
        JsonArray itineraryParts = offer.getAsJsonArray("itineraryPart");
        if (itineraryParts != null && !itineraryParts.isEmpty()) {
            for (JsonElement partElement : itineraryParts) {
                JsonObject part = partElement.getAsJsonObject();
                JsonArray segments = part.getAsJsonArray("segments");
                if (segments != null && !segments.isEmpty()) {
                    for (JsonElement segmentElement : segments) {
                        LegDto leg = gson.fromJson(segmentElement, LegDto.class);
                        legs.add(leg);
                    }
                }
            }
        }
        flight.setLegs(legs);

        // Map pricing details
        JsonObject brandedResults = originalResponse.getAsJsonObject("brandedResults");
        if (brandedResults == null) {
            throw new IllegalStateException("BrandedResults section is missing");
        }

        JsonArray itineraryPartBrands = brandedResults.getAsJsonArray("itineraryPartBrands");
        if (itineraryPartBrands == null || itineraryPartBrands.size() == 0) {
            throw new IllegalStateException("No itinerary part brands available");
        }

        JsonObject brandOffer = itineraryPartBrands.get(0).getAsJsonArray().size() > 0 ? itineraryPartBrands.get(0).getAsJsonArray().get(0).getAsJsonObject() : null;
        if (brandOffer == null) {
            throw new IllegalStateException("No brand offer available in itinerary part brands");
        }

        List<PricingDetailDto> pricingDetails = new ArrayList<>();
        PricingDetailDto pricingDetail = gson.fromJson(brandOffer, PricingDetailDto.class);
        pricingDetails.add(pricingDetail);

        flight.setPricingDetail(pricingDetails);

        return flight;
    }

    public static void main(String[] args) {
        String jsonData = requestHandlerVirginAustralia("DOH", "JED", "2024-04-18", 1);;
        System.out.println(jsonData);
        FlightDto flightDto = mapToFlightDto(jsonData);
        System.out.println(flightDto);
    }
}

@Data
 class LegDto {
    @SerializedName("equipment")
    private String aircraft;

    @SerializedName("airlineCode")
    private String carrierCode;

    @SerializedName("flightNumber")
    private String flightNumber;

    @SerializedName("arrival")
    private String arrivalDateTime;

    // Assuming connection time needs to be calculated or provided differently
    private int connectionTimeInMinutes;

    @SerializedName("departure")
    private String departureDateTime;

    @SerializedName("destination")
    private String destination;

    @SerializedName("duration")
    private int durationInMinutes;

    @SerializedName("origin")
    private String origin;

    // Getters and setters...
}
@Data
class PricingDetailDto {
    @SerializedName("points")
    private int points;

    @SerializedName("cashPrice")
    private double cashPrice;

    // Assuming this needs to match with a field in your JSON
    @SerializedName("productType")
    private String productType;

    private boolean mixedCabin = false;

    @SerializedName("seatsRemaining")
    private int seatsRemaining;

    // Getters and setters...
}

@Data
class FlightDto {

    private int duration;
    private String sourceAirline;

    @SerializedName("arrivesNextDay")
    private int arrivesNextDay;

    @SerializedName("Legs")
    private List<LegDto> legs;

    @SerializedName("pricingDetail")
    private List<PricingDetailDto> pricingDetail;}

