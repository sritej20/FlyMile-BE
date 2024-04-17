package ca.flymile.controller;

import ca.flymile.Flight.FlightDto;
import ca.flymile.InputValidation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ca.flymile.InputValidation.InputValidation.validateOriginDestinationStartDateZoneEndDatePassengers;

@RestController
@RequestMapping("/flights/all")
@CrossOrigin(origins = "*")
public class AllFlights {
    private final ca.flymile.service.American american;
    private final ca.flymile.service.Delta delta;
    private final ca.flymile.service.Alaska alaska;

    /**
     * Constructs a new AllFlights controller with the specified services.
     *
     * @param american The service responsible for scraping flight data from the AA website.
     * @param delta The service responsible for scraping flight data from the Delta website.
     * @param alaska The service responsible for scraping flight data from the Alaska website.
     */
    @Autowired
    public AllFlights(ca.flymile.service.American american, ca.flymile.service.Delta delta, ca.flymile.service.Alaska alaska) {
        this.american = american;
        this.delta = delta;
        this.alaska = alaska;
    }

    @GetMapping
    public CompletableFuture<List<FlightDto>> getFlightDataList(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "1") int numPassengers,
            @RequestParam(defaultValue = "false") boolean upperCabin
    ) {
        String origin = departure.toUpperCase();
        String destination = arrival.toUpperCase();
        validateOriginDestinationStartDateZoneEndDatePassengers(origin, destination, startDate, endDate, numPassengers);
        CompletableFuture<List<FlightDto>> deltaResult = delta.getFlightDataListDelta(origin, destination, startDate, endDate, numPassengers, upperCabin, false);
        CompletableFuture<List<FlightDto>> alaskaResult;
        if(numPassengers < 8)
            alaskaResult = alaska.getFlightDataListAlaska(origin, destination, startDate, endDate, numPassengers);
        else
            alaskaResult = CompletableFuture.completedFuture(new ArrayList<>());
        CompletableFuture<List<FlightDto>> americanResult = american.getFlightDataListAmerican(origin, destination, startDate, endDate, numPassengers, upperCabin, "3");


        return CompletableFuture.allOf(deltaResult, americanResult)
                .thenApply(ignored -> {
                    List<FlightDto> mergedList = new ArrayList<>();
                    mergedList.addAll(deltaResult.join());
                    mergedList.addAll(alaskaResult.join());
                    mergedList.addAll(americanResult.join());
                    return mergedList.stream()
                            .sorted(Comparator.comparingInt(FlightDto::getDuration))
                            .collect(Collectors.toList());
                });
    }
}
