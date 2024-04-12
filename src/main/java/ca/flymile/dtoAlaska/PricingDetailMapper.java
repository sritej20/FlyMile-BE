package ca.flymile.dtoAlaska;


import ca.flymile.Flight.PricingDetailDto;
import ca.flymile.ModelAlaska.pricingDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides functionality to map pricing details from a map structure to a list of data transfer objects (DTOs).
 */
public class PricingDetailMapper {
    private static final String ECONOMY = "ECONOMY";
    private static final Pattern CABIN_TYPE_PATTERN = Pattern.compile("_(BUSINESS|PREMIUM|FIRST|MAIN)$");

    /**
     * Converts a map of pricing details into a list of PricingDetailDto objects.
     * Each entry in the map represents a type of fare and its corresponding pricing details,
     * which are mapped to a PricingDetailDto object containing the fare's points, cash price,
     * product type, seats remaining, and mixed cabin information.
     *
     * @param fares A map where the key is the product type, and the value is the pricing details.
     * @return A list of PricingDetailDto objects representing the converted pricing details.
     */
    public static List<PricingDetailDto> toDtoList(Map<String, pricingDetails> fares) {
        List<PricingDetailDto> dtoList = new ArrayList<>();

        for (Map.Entry<String, pricingDetails> entry : fares.entrySet()) {
            String productType = entry.getKey();
            pricingDetails details = entry.getValue();

            PricingDetailDto dto = new PricingDetailDto()
                    .setPoints(details.getPoints())
                    .setCashPrice(details.getCashPrice())
                    .setProductType(extractCabinType(productType).charAt(0)=='M'?ECONOMY:extractCabinType(productType))
                    .setSeatsRemaining(details.getSeatsRemaining())
                    .setMixedCabin(details.isMixedCabin());

            dtoList.add(dto);
        }

        return dtoList;
    }
    /**
     * Extracts the cabin type from a given product type string.
     * <p>
     * This method uses a regular expression to identify and extract
     * the cabin type from the provided product type string. The expected
     * format of the string is to have the cabin type as a suffix preceded
     * by an underscore. The method supports extracting "BUSINESS", "PREMIUM",
     * "FIRST", and "MAIN" cabin types.
     * </p>
     *
     * @param productType The product type string from which the cabin type is to be extracted.
     *                    Expected to end with one of the supported cabin types ("BUSINESS",
     *                    "PREMIUM", "FIRST", or "MAIN"), preceded by an underscore.
     * @return The extracted cabin type if one of the supported types is found,
     *         otherwise returns "UNKNOWN".
     */
    public static String extractCabinType(String productType) {
        Matcher matcher = CABIN_TYPE_PATTERN.matcher(productType);;
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "UNKNOWN";
    }
}
