package ca.flymile.controller;
import ca.flymile.dtoDelta.DtoOffers;
import ca.flymile.service.DeltaYearly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import static ca.flymile.InputValidation.InputValidation.validateOriginDestinationPassengers;

/**
 * The DeltaControllerYearly class handles HTTP requests related to flight data retrieval on a yearly basis.
 */
@RestController
@RequestMapping("/flights/delta/yearly")
@CrossOrigin(origins = "*")
public class DeltaControllerYearly {

    private final DeltaYearly deltaYearly;

    /**
     * Constructs a new DeltaControllerYearly with the specified service for retrieving flight data on a yearly basis.
     *
     * @param deltaYearly The service responsible for retrieving flight data on a yearly basis.
     */
    @Autowired
    public DeltaControllerYearly(DeltaYearly deltaYearly) {
        this.deltaYearly = deltaYearly;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters for the entire year.
     *
     * @param departure The departure airport code.
     * @param arrival The arrival airport code.
     * @param numPassengers The number of passengers (default is 1 if not specified).
     * @return A CompletableFuture of a list of DtoOffers objects, each representing aggregated flight data over a year.
     */
    @GetMapping
    public CompletableFuture<List<DtoOffers>> getFlightDataListYearly(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam(defaultValue = "1") int numPassengers
    ) {
        // Validate the search parameters
        validateOriginDestinationPassengers(departure.toUpperCase(), arrival.toUpperCase(), numPassengers);

        // Retrieve and return the flight data for the year
        return deltaYearly.getFlightDataListDeltaYearly(departure, arrival, numPassengers);
    }
}
