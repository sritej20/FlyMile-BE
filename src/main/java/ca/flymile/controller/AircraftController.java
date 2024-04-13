package ca.flymile.controller;

import ca.flymile.service.AircraftBestMatchFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AircraftController {

    private final AircraftBestMatchFinder aircraftBestMatchFinder = new AircraftBestMatchFinder();

    @GetMapping("/findAircraft")
    public ResponseEntity<String> findBestAircraftMatch(
            @RequestParam String carrierCode,
            @RequestParam int flightNumber,
            @RequestParam String aircraft) {

        String result = aircraftBestMatchFinder.aircraftBestMatchFinder(carrierCode.toUpperCase(), flightNumber, aircraft);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}

