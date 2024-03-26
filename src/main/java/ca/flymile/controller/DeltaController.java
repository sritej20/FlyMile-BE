package ca.flymile.controller;


import ca.flymile.Flight.FlightDto;
import ca.flymile.service.Delta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.InputValidation.InputValidation.validateCabinClassDelta;
import static ca.flymile.InputValidation.InputValidation.validateOriginDestinationStartDateZoneEndDatePassengers;

/**
 * The DeltaController class handles HTTP requests related to flight data retrieval from Delta Airlines.
 */
@RestController
@RequestMapping("/flights/delta")
@CrossOrigin(origins = "*")
public class DeltaController {

    private final Delta delta;

    /**
     * Constructs a new DeltaController with the specified DeltaService.
     *
     * @param delta The service responsible for retrieving flight data from Delta Airlines.
     */
    @Autowired
    public DeltaController(Delta delta) {
        this.delta = delta;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure     The departure airport code.
     * @param arrival       The arrival airport code.
     * @param startDate     The start date of the travel period (format: "YYYY-MM-DD").
     * @param endDate       The end date of the travel period (format: "YYYY-MM-DD").
     * @param numPassengers The number of passengers.
     * @param upperCabin    Indicates if upper cabin (Business/First) seats are preferred.
     * @return A list of Delta flight data objects.
     */
    @GetMapping
    public CompletableFuture<List<FlightDto>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin
    ) {
        // Validate the search parameters
        validateOriginDestinationStartDateZoneEndDatePassengers(departure.toUpperCase(), arrival.toUpperCase(), startDate, endDate, numPassengers);
        return delta.getFlightDataListDelta(departure, arrival, startDate, endDate, numPassengers, upperCabin);
    }
}

