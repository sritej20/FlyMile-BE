package ca.flymile.controller;


import ca.flymile.ModelAlaska30Days.dailyCheapest;
import ca.flymile.service.AlaskaYearly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static ca.flymile.InputValidation.InputValidation.validateOriginDestination;

/**
 * The AlaskaControllerYearly class handles HTTP requests related to flight data retrieval for a yearly period from the Alaska Airlines website.
 */
@RestController
@RequestMapping("/flights/alaska/yearly")
@CrossOrigin(origins = "*")
public class AlaskaControllerYearly {

    private final AlaskaYearly alaskaYearly;

    /**
     * Constructs a new AlaskaControllerYearly with the specified service for Alaska Airlines yearly cheapest price & point combo.
     *
     * @param alaskaYearly The service responsible for retrieving flight data from the Alaska Airlines for yearly cheapest price & point combo.
     */
    @Autowired
    public AlaskaControllerYearly(AlaskaYearly alaskaYearly) {
        this.alaskaYearly = alaskaYearly;
    }

    /**
     * Retrieves a list of flight data for an entire year based on the provided search parameters.
     *
     * @param departure The departure airport code.
     * @param arrival   The arrival airport code.
     * @return A CompletableFuture of a list of dailyCheapest objects, each representing a date with available pricing details within a yearly period.
     */
    @GetMapping
    public CompletableFuture<List<dailyCheapest>> getFlightDataListYearly(
            @RequestParam String departure,
            @RequestParam String arrival
    ) {
        // Validate the search parameters
        validateOriginDestination(departure.toUpperCase(), arrival.toUpperCase());

        // Retrieve and return the daily Cheapest List for the year
        return alaskaYearly.getFlightDataListAlaskaYearly(departure, arrival);
    }
}

