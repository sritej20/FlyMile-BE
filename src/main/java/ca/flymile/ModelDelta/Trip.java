package ca.flymile.ModelDelta;

import lombok.Data;

import java.util.List;
@Data
public class Trip {
    private List<FlightSegment> flightSegment;
    private TotalTripTime totalTripTime;
}
