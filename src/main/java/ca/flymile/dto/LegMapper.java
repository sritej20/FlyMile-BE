package ca.flymile.dto;

import ca.flymile.Model.Leg;

public class LegMapper {
    public static LegDto toDto(Leg leg){
        return new LegDto()
                .setAircraft(leg.getAircraft().getName())
                .setArrivalDateTime(leg.getArrivalDateTime())
                .setConnectionTimeInMinutes(leg.getConnectionTimeInMinutes())
                .setDepartureDateTime(leg.getDepartureDateTime())
                .setLieFlat(leg.isLieFlat())
                .setDestination(leg.getDestination().getCode())
                .setDurationInMinutes(leg.getDurationInMinutes())
                .setOrigin(leg.getOrigin().getCode());
    }
}
