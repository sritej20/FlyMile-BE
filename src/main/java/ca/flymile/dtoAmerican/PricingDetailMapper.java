package ca.flymile.dtoAmerican;

import ca.flymile.Flight.PricingDetailDto;
import ca.flymile.ModelAmerican.PricingDetail;



/**
 * Provides functionality to map data from domain models to data transfer objects (DTOs) for pricing details of flights.
 */
public class PricingDetailMapper {
    private static final String ECONOMY = "ECONOMY";
    private static final String PREMIUM = "PREMIUM";
    private static final String FIRST = "FIRST";
    private static final String BUSINESS = "BUSINESS";
    private static final String UNKNOWN = "UNKNOWN";

    /**
     * Converts a PricingDetail object to a PricingDetailDto object.
     * This method maps the points, cash price, product type, and seats remaining from the PricingDetail
     * domain model to the PricingDetailDto object.
     *
     * @param pricingDetail The PricingDetail object containing the pricing information to be mapped.
     * @return A PricingDetailDto object containing the mapped pricing data, including points, cash price,
     *         product type, and available seats information.
     */
    public static PricingDetailDto toDto(PricingDetail pricingDetail) {
        return new PricingDetailDto()
                .setPoints(pricingDetail.getPoints())
                .setCashPrice(pricingDetail.getCashPrice().getAmount())
                .setProductType(setProductType(pricingDetail.getProductType().charAt(0)))
                .setSeatsRemaining(pricingDetail.getSeatsRemaining());
    }
    private static String setProductType(char firstChar) {
        return switch (firstChar) {
            case 'C', 'M' -> ECONOMY;
            case 'P' -> PREMIUM;
            case 'F' -> FIRST;
            case 'B' -> BUSINESS;
            default -> UNKNOWN;
        };
    }
}