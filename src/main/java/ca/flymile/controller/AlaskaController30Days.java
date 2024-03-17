package ca.flymile.controller;

import ca.flymile.ModelAlaska30Days.dailyCheapest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static ca.flymile.InputValidation.InputValidation.validateFlightSearchParamsForAlaska30Days;

/**
 * The AlaskaController30Days class handles HTTP requests related to flight data retrieval for a 30-day period from the Alaska Airlines website.
 */
@RestController
@RequestMapping("/flights/alaska/30Day")
@CrossOrigin(origins = "*")
public class AlaskaController30Days {

    private final ca.flymile.service.Alaska30Days alaska30Days;

    /**
     * Constructs a new AlaskaController30Days with the specified service for Alaska Airlines 30-day cheapest price & point combo.
     *
     * @param alaska30Days The service responsible for retrieving flight data from the Alaska Airlines for 30-day cheapest price & point combo.
     *
     */
    @Autowired
    public AlaskaController30Days(ca.flymile.service.Alaska30Days alaska30Days) {
        this.alaska30Days = alaska30Days;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @param startDate The start date of the travel period (format: "YYYY-MM-DD").
     * @return A list of dailyCheapest objects, each representing a date with available pricing details within a 30-day period.
     * [-15 + TODAY , TODAY + 15]
     */
    @GetMapping
    public List<dailyCheapest> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate
    ) {
        // Validate the search parameters
        validateFlightSearchParamsForAlaska30Days(departure.toUpperCase(), arrival.toUpperCase(), startDate);

        // Retrieve and return the daily Cheapest List
        return alaska30Days.getFlightDataListAlaska30Days(departure, arrival, startDate);
    }
}
