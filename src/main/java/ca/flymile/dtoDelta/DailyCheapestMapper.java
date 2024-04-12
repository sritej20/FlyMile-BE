package ca.flymile.dtoDelta;

import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.ModelDeltaMonthly.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyCheapestMapper {

    public static DtoDeltaDaily toDto(List<OfferSet> offerSets) {
        DtoDeltaDaily dtoDeltaDaily = new DtoDeltaDaily();
        List<DailyCheapest> dtoOffersList = offerSets.stream()
                .flatMap(offerSet -> offerSet.getOffers().stream())
                .map(DailyCheapestMapper::toDto)
                .collect(Collectors.toList());
        dtoDeltaDaily.setDailyCheapest(dtoOffersList);
        return dtoDeltaDaily;
    }

    public static DailyCheapest toDto(Offer offer) {
        DailyCheapest dto = new DailyCheapest();

        if (offer.getOfferItems().isEmpty() || offer.getOfferPricing().isEmpty()) {
            return dto;  // Return empty DTO if the necessary lists are empty
        }

        OfferItem offerItem = offer.getOfferItems().get(0);  // Assuming the first item is what we want
        OfferPricing offerPricing = offer.getOfferPricing().get(0);

        if (offerItem.getRetailItems().isEmpty() || offerPricing.getTotalAmt() == null) {
            return dto;  // Return empty DTO if any required information is missing
        }

        RetailItem retailItem = offerItem.getRetailItems().get(0);
        if (retailItem.getRetailItemMetaData() == null) {
            return dto;  // Return empty DTO if metadata is missing
        }

        RetailItemMetaData metaData = retailItem.getRetailItemMetaData();
        if (metaData.getFareInformation().isEmpty()) {
            return dto;  // Return empty DTO if fare information is missing
        }

        FareInformation fareInformation = metaData.getFareInformation().get(0);
        if (fareInformation.getPriceCalendar() == null) {
            return dto;  // Return empty DTO if price calendar is missing
        }

        PriceCalendar priceCalendar = fareInformation.getPriceCalendar();
        String date = priceCalendar.getPriceCalendarDate();
        if(date.length() != 10) {
            StringBuilder sb = new StringBuilder(date)
            .insert(5,'0');
            dto.setDate(sb.toString());
        }
        else
            dto.setDate(priceCalendar.getPriceCalendarDate());


        TotalAmount totalAmount = offerPricing.getTotalAmt();
        if (totalAmount.getMilesEquivalentPrice() != null) {
            dto.setPoints(totalAmount.getMilesEquivalentPrice().getMileCnt());
            dto.setCashPrice(totalAmount.getCurrencyEquivalentPrice().getRoundedCurrencyAmt());
        }

        return dto;
    }
}
