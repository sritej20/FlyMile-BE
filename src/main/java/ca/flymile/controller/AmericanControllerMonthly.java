package ca.flymile.controller;

import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.service.AmericanMonthly;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.InputValidation.InputValidation.*;

/**
 * The AmericanControllerMonthly class handles HTTP requests related to flight data retrieval for a monthly period from the American Airlines website.
 */
@RestController
@RequestMapping("/flights/american/monthly")
@CrossOrigin(origins = "*")
public class AmericanControllerMonthly {


    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @param startDate The start date of the travel period (format: "YYYY-MM-DD").
     * @return A list of dailyCheapest objects, each representing a date with available pricing details within a monthly period.
     */
    @GetMapping
    public CompletableFuture<List<DailyCheapest>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin
    ) {
        // Validate the search parameters
        validateOriginDestinationStartDatePassengers(departure.toUpperCase(), arrival.toUpperCase(), startDate,numPassengers);

        // Retrieve and return the daily Cheapest List
        return AmericanMonthly.getFlightDataListAmericanMonthly(departure, arrival, startDate,numPassengers, upperCabin);
    }
}
