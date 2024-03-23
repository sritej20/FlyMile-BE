package ca.flymile.ModelAmericalMonthly;

import lombok.Data;

import java.util.List;
@Data
public class FlightData {
    private List<CalendarMonth> calendarMonths;
    private String error;
}
