package ca.flymile.ModelVirginAustralia;

import lombok.Data;

import java.util.List;
@Data
public class OriginalResponse {
    private List<List<UnbundledOffer>> unbundledOffers;
    private List<List<UnbundledAlternateDateOffer>> unbundledAlternateDateOffers;
    private BrandedResults brandedResults;
}
