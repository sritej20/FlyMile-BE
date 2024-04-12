package ca.flymile.controller;

import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.service.AmericanMonthly;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
public class AmericanControllerMonthly {
    private AmericanMonthly americanMonthly;
@Autowired
    public AmericanControllerMonthly(AmericanMonthly americanMonthly) {this.americanMonthly = americanMonthly;}


    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @param startDate The start date of the travel period (format: "YYYY-MM-DD").
     * @return A list of dailyCheapest objects, each representing a date with available pricing details within a monthly period.
     */
    @GetMapping
    public List<DailyCheapest> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin,
            @RequestParam(required = false, defaultValue = "3") String maxStops
    ) {
        // Validate the search parameters
        validateOriginDestinationStartDatePassengers(departure.toUpperCase(), arrival.toUpperCase(), startDate,numPassengers);
        // Retrieve and return the daily Cheapest List
        return americanMonthly.getFlightDataListAmericanMonthly(departure, arrival, startDate,numPassengers, upperCabin, parseAndValidateStops(maxStops));
    }
}
