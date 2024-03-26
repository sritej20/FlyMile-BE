package ca.flymile.controller;
import ca.flymile.DailyCheapest.DailyCheapest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.InputValidation.InputValidation.*;

/**
 * The DeltaControllerMonthly class handles HTTP requests related to flight data retrieval on a weekly basis from the American Airlines website.
 */
@RestController
@RequestMapping("/flights/delta/monthly")
@CrossOrigin(origins = "*")
public class DeltaControllerMonthly {

    private final ca.flymile.service.DeltaMonthly deltaMonthly;

    /**
     * Constructs a new DeltaControllerMonthly with the specified service for American Airlines weekly cheapest price & point combo.
     *
     * @param deltaMonthly The service responsible for retrieving flight data from American Airlines for the weekly cheapest price & point combo.
     */
    @Autowired
    public DeltaControllerMonthly(ca.flymile.service.DeltaMonthly deltaMonthly) {
        this.deltaMonthly = deltaMonthly;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @param startDate The start date of the travel period (format: "YYYY-MM-DD").
     * @return A list of DailyCheapest objects, each representing a date with available pricing details within a weekly period.
     */
    @GetMapping
    public CompletableFuture<List<DailyCheapest>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam (defaultValue = "false")boolean upperCabin
    ) {
        // Validate the search parameters
        validateOriginDestinationStartDatePassengers(departure.toUpperCase(), arrival.toUpperCase(), startDate, numPassengers);
        return deltaMonthly.getFlightDataListDeltaMonthly(departure, arrival, startDate, numPassengers, upperCabin);
    }
}
