package ca.flymile.controller;

import ca.flymile.Flight.FlightDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.InputValidation.InputValidation.validateOriginDestinationStartDateZoneEndDateNumPassengersAlaska;

/**
 * The AlaskaController class handles HTTP requests related to flight data retrieval from the Alaska Airlines website.
 */
@RestController
@RequestMapping("/flights/alaska")
@CrossOrigin(origins = "*")
public class AlaskaController {

    private final ca.flymile.service.Alaska alaska;

    /**
     * Constructs a new AlaskaController with the specified Alaska service.
     *
     * @param alaska The service responsible for retrieving flight data from the Alaska Airlines website.
     */
    @Autowired
    public AlaskaController(ca.flymile.service.Alaska alaska) {
        this.alaska = alaska;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure     The departure airport code.
     * @param arrival       The arrival airport code.
     * @param startDate     The start date of the travel period (format: "YYYY-MM-DD").
     * @param endDate       The end date of the travel period (format: "YYYY-MM-DD").
     * @param  numPassengers The number of passengers, constrained between one and seven for alaska.
     * @return A list of flight data, each containing a list of slices representing the different legs of the journey.
     *         <p>Slice represents a single flight.</p>
     *         <p>The outer list contains flights grouped by date, where each inner list represents flights for a particular date.</p>
     */
    @GetMapping
    public CompletableFuture<List<FlightDto>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "1") int numPassengers
    ) {
        String origin = departure.toUpperCase();
        String destination = arrival.toUpperCase();

        validateOriginDestinationStartDateZoneEndDateNumPassengersAlaska(origin, destination, startDate, endDate, numPassengers);
        // Retrieve and return the flight data list
        return alaska.getFlightDataListAlaska(origin, destination, startDate, endDate, numPassengers);
    }
}
