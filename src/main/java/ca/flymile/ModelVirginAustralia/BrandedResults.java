package ca.flymile.ModelVirginAustralia;

import lombok.Data;

import java.util.List;
@Data
public class BrandedResults {
    private List<ItineraryPart> itineraryParts;
    private List<List<ItineraryPartBrand>> itineraryPartBrands;
}