package ca.flymile.ModelDelta;

import lombok.Data;

import java.util.List;
@Data
public class FareInformation {
    private List<BrandByFlightLegs> brandByFlightLegs;
    private int availableSeatCnt;
    private List<FarePrice> farePrice;
}
