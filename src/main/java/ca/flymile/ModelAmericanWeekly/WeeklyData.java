package ca.flymile.ModelAmericanWeekly;

import lombok.Data;

import java.util.List;


@Data
public class WeeklyData {
    private String error;
    private List<dailyCheapest> days;
}
