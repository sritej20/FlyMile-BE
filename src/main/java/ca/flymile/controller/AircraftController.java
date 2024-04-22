package ca.flymile.controller;

import ca.flymile.LinkModels.ResponseLink;
import ca.flymile.service.AircraftBestMatchFinder;
import ca.flymile.service.SeatMapsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AircraftController {

    private final AircraftBestMatchFinder aircraftBestMatchFinder = new AircraftBestMatchFinder();
    private final SeatMapsService seatMapsService = new SeatMapsService();
    private static final String FLYMILE_URL = "https://www.flymile.pro/";

    @GetMapping("/findAircraft/v1")
    public ResponseLink findBestAircraftMatch(
            @RequestParam String carrierCode,
            @RequestParam int flightNumber,
            @RequestParam String aircraft) {

        String result = aircraftBestMatchFinder.aircraftBestMatchFinder(carrierCode, flightNumber, aircraft);
        return new ResponseLink(result != null ? result : FLYMILE_URL);
    }
    @GetMapping("/findAircraft/v2")
    public ResponseLink findBestAircraftSeatMap(
            @RequestParam String carrierCode,
            @RequestParam int flightNumber,
            @RequestParam String departureDate) {

        Optional<String> result = seatMapsService.getSeatMap(carrierCode, flightNumber, departureDate);
        return new ResponseLink(result.orElse(FLYMILE_URL));
    }
}

