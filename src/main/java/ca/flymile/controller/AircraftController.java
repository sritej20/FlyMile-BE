package ca.flymile.controller;

import ca.flymile.LinkModels.ResponseLink;
import ca.flymile.service.AircraftBestMatchFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AircraftController {

    private final AircraftBestMatchFinder aircraftBestMatchFinder = new AircraftBestMatchFinder();
    private static final String FLYMILE_URL = "https://www.flymile.pro/";

    @GetMapping("/findAircraft")
    public ResponseLink findBestAircraftMatch(
            @RequestParam String carrierCode,
            @RequestParam int flightNumber,
            @RequestParam String aircraft) {

        String result = aircraftBestMatchFinder.aircraftBestMatchFinder(carrierCode.toUpperCase(), flightNumber, aircraft);
        return new ResponseLink(result != null ? result : FLYMILE_URL);
    }
}

