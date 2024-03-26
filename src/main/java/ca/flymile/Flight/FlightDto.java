package ca.flymile.Flight;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightDto
{
    private int duration;

    /**
     * Indicates whether the arrival is on the next day.
     * 0: Arrival is on the same day.
     * 1: Arrival is on the next day.
     * And So On...
     * could be byte ???
     */
    private int arrivesNextDay;

    /**
     * The segments comprising this slice.
     */
    private List<LegDto> Legs;

    /**
     * The pricing details for this slice.
     */
    private List<PricingDetailDto> pricingDetail;

}
