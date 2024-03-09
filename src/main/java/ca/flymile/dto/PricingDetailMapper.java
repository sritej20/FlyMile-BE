package ca.flymile.dto;

import ca.flymile.Model.PricingDetail;

public class PricingDetailMapper {
    public static PricingDetailDto toDto(PricingDetail pricingDetail) {
        return new PricingDetailDto()
                .setPoints(pricingDetail.getPoints())
                .setCashPrice(pricingDetail.getCashPrice().getAmount())
                .setProductType(pricingDetail.getProductType())
                .setSeatsRemaining(pricingDetail.getSeatsRemaining());
    }
}
