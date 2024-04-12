package ca.flymile.ModelVirginAustralia;

import lombok.Data;

@Data
public class OfferInformation {
    private boolean discounted;
    private boolean negotiated;
    private String negotiatedType;
}
