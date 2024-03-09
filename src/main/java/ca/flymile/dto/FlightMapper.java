package ca.flymile.dto;

import ca.flymile.Model.Slice;

import java.util.stream.Collectors;

public class FlightMapper {
    public static FlightDto toDto(Slice slice)
    {
        return new FlightDto()
                .setDuration(slice.getDuration())
                .setArrivalDateTime(slice.getArrivalDateTime())
                .setDepartureDateTime(slice.getDepartureDateTime())
                .setArrivesNextDay(slice.getArrivesNextDay())
                .setLegs(slice.getSegments().stream()
                        .flatMap(segment -> segment.getLegs().stream()
                                .map(leg -> LegMapper.toDto(leg)
                                        .setCarrierCode(segment.getFlight().getCarrierCode())
                                        .setFlightNumber(segment.getFlight().getFlightNumber()))).collect(Collectors.toList()))
                .setPricingDetail(slice.getPricingDetail().stream().map(PricingDetailMapper::toDto).collect(Collectors.toList()));
    }
}