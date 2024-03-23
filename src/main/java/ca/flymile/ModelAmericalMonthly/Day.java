package ca.flymile.ModelAmericalMonthly;


import lombok.Data;

@Data
public class Day {
    private String date;
    private Solution solution;
    private boolean validDay;
}