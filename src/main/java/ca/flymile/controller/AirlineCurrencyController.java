package ca.flymile.controller;

import ca.flymile.FlyMileAirportData.CurrencyRetriever;
import org.springframework.web.bind.annotation.*;

import static ca.flymile.InputValidation.InputValidation.validateOrigin;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/currency/alaska")
class AlaskaCurrencyController {

    @GetMapping
    public String getCurrency(@RequestParam String origin) {
        return "USD";
    }
}
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/currency/american")
class AmericanCurrencyController {

    @GetMapping
    public String getCurrency(@RequestParam String origin) {
        return "USD";
    }
}
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/currency/virginAustralia")
class VirginAustraliaCurrencyController {
    @GetMapping
    public String getCurrency(@RequestParam String origin) {
        return "AUD";
    }
}
@CrossOrigin(origins = "*")
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
