package ca.flymile.controller;


import ca.flymile.dtoAmerican.AmericanDailyCheapestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static ca.flymile.InputValidation.InputValidation.validateOriginDestinationStartDatePassengers;

/**
 * The AmericanControllerWeekly class handles HTTP requests related to flight data retrieval on a weekly basis from the American Airlines website.
 */
@RestController
@RequestMapping("/flights/american/weekly")
@CrossOrigin(origins = "*")
public class AmericanControllerWeekly {

    private final ca.flymile.service.AmericanWeekly americanWeekly;

    /**
     * Constructs a new AmericanControllerWeekly with the specified service for American Airlines weekly cheapest price & point combo.
     *
     * @param americanWeekly The service responsible for retrieving flight data from American Airlines for weekly cheapest price & point combo.
     */
    @Autowired
    public AmericanControllerWeekly(ca.flymile.service.AmericanWeekly americanWeekly) {
        this.americanWeekly = americanWeekly;
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
    public List<AmericanDailyCheapestDto> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin
    ) {
        // Validate the search parameters
        validateOriginDestinationStartDatePassengers(departure.toUpperCase(), arrival.toUpperCase(), startDate,numPassengers);

        // Retrieve and return the daily Cheapest List
        return americanWeekly.getFlightDataListAmericanWeekly(departure, arrival, startDate, numPassengers, upperCabin);
    }
}
