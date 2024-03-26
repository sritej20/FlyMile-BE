package ca.flymile.DailyCheapest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class  DailyCheapest {
    @SerializedName(value = "awardPoints", alternate = {"points"})
    private int points;
    @SerializedName(value = "price", alternate = {"cashPrice"})
    private double cashPrice;
    private String date;
}
