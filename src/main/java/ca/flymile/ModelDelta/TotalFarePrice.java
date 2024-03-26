package ca.flymile.ModelDelta;

import lombok.Data;

@Data
public class TotalFarePrice {
    private CurrencyEquivalentPrice currencyEquivalentPrice;
    private MilesEquivalentPrice milesEquivalentPrice;
}
