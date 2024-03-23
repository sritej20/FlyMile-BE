package ca.flymile.controller;

import ca.flymile.ModelAlaskaMonthly.dailyCheapest;
import ca.flymile.service.AlaskaMonthly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ca.flymile.InputValidation.InputValidation.validateOriginDestinationStartDate;

/**
 * The AlaskaController30Days class handles HTTP requests related to flight data retrieval for a 30-day period from the Alaska Airlines website.
 */
@RestController
@RequestMapping("/flights/alaska/monthly")
@CrossOrigin(origins = "*")
public class AlaskaControllerMonthly {

    private final AlaskaMonthly alaskaMonthly;

    /**
     * Constructs a new AlaskaController30Days with the specified service for Alaska Airlines 30-day cheapest price & point combo.
     *
     * @param alaskaMonthly The service responsible for retrieving flight data from the Alaska Airlines for 30-day cheapest price & point combo.
     */
    @Autowired
    public AlaskaControllerMonthly(AlaskaMonthly alaskaMonthly) {
        this.alaskaMonthly = alaskaMonthly;
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
        validateOriginDestinationStartDate(departure.toUpperCase(), arrival.toUpperCase(), startDate);

        // Retrieve and return the daily Cheapest List
        return alaskaMonthly.getFlightDataListAlaskaMonthly(departure, arrival, startDate);
    }
}
