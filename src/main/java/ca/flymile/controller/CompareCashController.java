package ca.flymile.controller;
import ca.flymile.ModelBookingDotCom.MinPriceRound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.flymile.service.cashPriceComparison;

@RestController
public class CompareCashController {

    private final cashPriceComparison priceComparisonService;

    @Autowired
    public CompareCashController(cashPriceComparison priceComparisonService) {
        this.priceComparisonService = priceComparisonService;
    }

    @GetMapping("/compareCashPrice")
    public MinPriceRound compareFlightPrices(
            @RequestParam int adults,
            @RequestParam String cabinClass,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String departDate,
            @RequestParam String airline) {

        return priceComparisonService.getCashPriceComparison(adults, cabinClass, from, to, departDate, airline);
    }
}
