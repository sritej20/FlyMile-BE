package ca.flymile.ModelDelta;

import lombok.Data;

import java.util.List;
@Data
public class FlightSegment {
    private String aircraftTypeCode;
    private String destinationAirportCode;
    private List<FlightLeg> flightLeg;
    private Layover layover;
    private OperatingCarrier operatingCarrier;
    private String originAirportCode;
    private String scheduledArrivalLocalTs;
    private String scheduledDepartureLocalTs;
}