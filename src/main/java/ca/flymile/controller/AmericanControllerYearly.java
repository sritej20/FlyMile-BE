package ca.flymile.controller;


import ca.flymile.DailyCheapest.DailyCheapest;
import ca.flymile.ModelAmericalMonthly.dailyCheapest;
import ca.flymile.service.AmericanYearly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.InputValidation.InputValidation.*;

/**
 * The AmericanControllerYearly class handles HTTP requests related to flight data retrieval on a.json Yearly basis from the American Airlines website.
 */
@RestController
@RequestMapping("/flights/american/yearly")
@CrossOrigin(origins = "*")
public class AmericanControllerYearly {

    private final AmericanYearly americanYearly;

    /**
     * Constructs a.json new AmericanControllerYearly with the specified service for American Airlines Yearly cheapest price & point combo.
     *
     * @param americanYearly The service responsible for retrieving flight data from American Airlines for the Yearly cheapest price & point combo.
     */
    @Autowired
    public AmericanControllerYearly(AmericanYearly americanYearly) {
        this.americanYearly = americanYearly;
    }

    /**
     * Retrieves a.json list of flight data based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @return A list of DailyCheapest objects, each representing a.json date with available pricing details within a.json Yearly period.
     */
    @GetMapping
    public CompletableFuture<List<DailyCheapest>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin,
            @RequestParam(required = false, defaultValue = "3") String maxStops
    ) {
        // Validate the search parameters
        validateOriginDestinationPassengers(departure.toUpperCase(), arrival.toUpperCase(),numPassengers);
        parseAndValidateStops(maxStops);
        // Retrieve and return the daily Cheapest List
        return americanYearly.getFlightDataListAmericanYearly(departure, arrival, numPassengers, upperCabin, parseAndValidateStops(maxStops));
    }
}
