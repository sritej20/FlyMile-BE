package ca.flymile.controller;


import ca.flymile.dtoAmerican.AmericanDailyCheapestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.InputValidation.InputValidation.*;

/**
 * The AmericanControllerYearly class handles HTTP requests related to flight data retrieval on a Yearly basis from the American Airlines website.
 */
@RestController
@RequestMapping("/flights/american/Yearly")
@CrossOrigin(origins = "*")
public class AmericanControllerYearly {

    private final ca.flymile.service.AmericanYearly americanYearly;

    /**
     * Constructs a new AmericanControllerYearly with the specified service for American Airlines Yearly cheapest price & point combo.
     *
     * @param americanYearly The service responsible for retrieving flight data from American Airlines for Yearly cheapest price & point combo.
     */
    @Autowired
    public AmericanControllerYearly(ca.flymile.service.AmericanYearly americanYearly) {
        this.americanYearly = americanYearly;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @return A list of DailyCheapest objects, each representing a date with available pricing details within a Yearly period.
     */
    @GetMapping
    public CompletableFuture<List<AmericanDailyCheapestDto>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin
    ) {
        // Validate the search parameters
        validateOriginDestinationPassengers(departure.toUpperCase(), arrival.toUpperCase(),numPassengers);

        // Retrieve and return the daily Cheapest List
        return americanYearly.getFlightDataListAmericanYearly(departure, arrival, numPassengers, upperCabin);
    }
}
