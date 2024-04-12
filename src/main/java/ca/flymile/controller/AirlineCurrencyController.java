package ca.flymile.controller;

import ca.flymile.FlyMileAirportData.CurrencyRetriever;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static ca.flymile.InputValidation.InputValidation.validateOrigin;


@RestController
@RequestMapping("/currency/alaska")
class AlaskaCurrencyController {

    @GetMapping
    public String getCurrency(@RequestParam String origin) {
        return "USD";
    }
}

@RestController
@RequestMapping("/currency/american")
class AmericanCurrencyController {

    @GetMapping
    public String getCurrency(@RequestParam String origin) {
        return "USD";
    }
}
@RestController
@RequestMapping("/currency/virginAustralia")
class VirginAustraliaCurrencyController {
    @GetMapping
    public String getCurrency(@RequestParam String origin) {
        return "AUD";
    }
}

@RestController
@RequestMapping("/currency/delta")
class DeltaCurrencyController {

    @GetMapping
    public String getCurrency(@RequestParam String origin) {
        origin = origin.toUpperCase();
        validateOrigin(origin);
        return CurrencyRetriever.getValidCurrencyForAirport(origin);
    }
}
