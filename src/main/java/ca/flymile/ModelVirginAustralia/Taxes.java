package ca.flymile.ModelVirginAustralia;

import lombok.Data;

import java.util.List;
@Data
public class Taxes {
    private List<List<Alternative>> alternatives;
}
