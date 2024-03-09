package ca.flymile.controller;

import ca.flymile.Model.Slice;
import ca.flymile.dto.FlightDto;
import ca.flymile.service.AAScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static ca.flymile.InputValidation.InputValidation.validateFlightSearchParams;

/**
 * The AAScrapperController class handles HTTP requests related to flight data retrieval from the American Airlines (AA) website.
 */
@RestController
@RequestMapping("/flights")
@CrossOrigin(origins = "*")
public class AAScrapperController {

    private final AAScrapperService aaScrapperService;

    /**
     * Constructs a new AAScrapperController with the specified AAScrapperService.
     *
     * @param aaScrapperService The service responsible for scraping flight data from the AA website.
     */
    @Autowired
    public AAScrapperController(AAScrapperService aaScrapperService) {
        this.aaScrapperService =  aaScrapperService;
    }

    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param departure     The departure airport code.
     * @param arrival       The arrival airport code.
     * @param startDate     The start date of the travel period (format: "YYYY-MM-DD").
     * @param endDate       The end date of the travel period (format: "YYYY-MM-DD").
     * @param numPassengers The number of passengers.
     * @param upperCabin    Indicates if upper cabin (Business/First) seats are preferred.
     * @return A list of flight data, each containing a list of slices representing different legs of the journey.
     *         <p>Slice represents a single flight.</p>
     *         <p>The outer list contains flights grouped by date, where each inner list represents flights for a particular date.</p>
     */

    @GetMapping
    public List<List<FlightDto>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin
    ) {
        // Validate the search parameters
        validateFlightSearchParams(departure, arrival, startDate, endDate, numPassengers);

        // Retrieve and return the flight data list
        return aaScrapperService.getFlightDataList(departure, arrival, startDate, endDate, numPassengers, upperCabin);
    }
}
