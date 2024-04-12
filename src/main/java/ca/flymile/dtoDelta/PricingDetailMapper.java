package ca.flymile.dtoDelta;

import ca.flymile.Flight.PricingDetailDto;
import ca.flymile.ModelDelta.*;
import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.logging.Logger;

@Slf4j
public class PricingDetailMapper {
    private static final String ECONOMY = "ECONOMY";
    private static final String PREMIUM = "PREMIUM";
    private static final String BUSINESS = "BUSINESS";
    private static final String UNKNOWN = "UNKNOWN";
    public static List<PricingDetailDto> mapPricingDetails(GqlOffersSets gqlOffersSets) {
        Map<String, PricingDetailDto> selectedProductTypes = new HashMap<>();

        gqlOffersSets.getOffers().stream()
                .filter(offer -> offer.getOfferId() != null && !offer.getOfferId().isEmpty())
                .flatMap(offer -> offer.getOfferItems().stream())
                .flatMap(item -> item.getRetailItems().stream())
                .map(PricingDetailMapper::mapToPricingDetail)
                .forEach(detail -> {
                    String productType = detail.getProductType();
                    // Check if this product type is already selected and if the current option has fewer points
                    if (!selectedProductTypes.containsKey(productType) ||
                            selectedProductTypes.get(productType).getPoints() > detail.getPoints()) {
                        selectedProductTypes.put(productType, detail);
                    }
                });

        return new ArrayList<>(selectedProductTypes.values());
    }

    public static PricingDetailDto mapToPricingDetail(RetailItem retailItem) {
        PricingDetailDto detail = new PricingDetailDto();
        FareInformation fareInfo = retailItem.getRetailItemMetaData().getFareInformation().get(0);
        FarePrice farePrice = fareInfo.getFarePrice().get(0);

        detail.setPoints(farePrice.getTotalFarePrice().getMilesEquivalentPrice().getMileCnt())
                .setCashPrice(farePrice.getTotalFarePrice().getCurrencyEquivalentPrice().getRoundedCurrencyAmt())
                .setProductType(determineProductType(fareInfo.getBrandByFlightLegs().get(0).getCosCode()))
                .setSeatsRemaining(fareInfo.getAvailableSeatCnt());

        return detail;
    }

    private static String determineProductType(String cosCode) {
        if (cosCode == null || cosCode.isEmpty()) {
            log.info("cosCode is null or empty.");
            return UNKNOWN;
        }

        return switch (cosCode.charAt(0)) {
            case 'X', 'N', 'T', 'A', 'F', 'V' -> ECONOMY;
            case 'S', 'R', 'P' -> PREMIUM;
            case 'O', 'U', 'G', 'Z' -> BUSINESS;
            default -> {
                log.info("Unknown cosCode encountered - {}", cosCode);
                yield UNKNOWN;
            }
        };
    }
}
