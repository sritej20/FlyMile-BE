package ca.flymile.ModelAmericalMonthly;

import lombok.Data;

import java.util.List;
@Data
public class CalendarMonth {
    private List<Week> weeks;
}