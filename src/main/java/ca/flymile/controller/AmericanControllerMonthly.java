package ca.flymile.controller;

import ca.flymile.ModelAmericalMonthly.dailyCheapest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ca.flymile.InputValidation.InputValidation.*;

/**
 * The AmericanControllerMonthly class handles HTTP requests related to flight data retrieval for a monthly period from the American Airlines website.
 */
@RestController
@RequestMapping("/flights/american/monthly")
@CrossOrigin(origins = "*")
public class AmericanControllerMonthly {

    private final ca.flymile.service.AmericanMonthly americanMonthly;

    /**
     * Constructs a new AmericanControllerMonthly with the specified service for American Airlines monthly cheapest price & point combo.
     *
     * @param americanMonthly The service responsible for retrieving flight data from the American Airlines for a monthly cheapest price & point combo.
     */
    @Autowired
    public AmericanControllerMonthly(ca.flymile.service.AmericanMonthly americanMonthly) {
        this.americanMonthly = americanMonthly;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @param startDate The start date of the travel period (format: "YYYY-MM-DD").
     * @return A list of dailyCheapest objects, each representing a date with available pricing details within a monthly period.
     */
    @GetMapping
    public List<dailyCheapest> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin
    ) {
        // Validate the search parameters
        validateOriginDestinationStartDatePassengers(departure.toUpperCase(), arrival.toUpperCase(), startDate,numPassengers);

        // Retrieve and return the daily Cheapest List
        return americanMonthly.getFlightDataListAmericanMonthly(departure, arrival, startDate,numPassengers, upperCabin);
    }
}
