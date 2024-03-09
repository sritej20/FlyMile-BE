package ca.flymile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
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
    @SerializedName("durationInMinutes")
    private int duration;

    /**
     * The date and time of arrival for this slice.
     */
    private String arrivalDateTime;

    /**
     * The date and time of departure for this slice.
     */
    private String departureDateTime;

    /**
     * Indicates whether the arrival is on the next day.
     * 0: Arrival is on the same day.
     * 1: Arrival is on the next day.
     * and So On...
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