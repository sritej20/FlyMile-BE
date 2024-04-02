package ca.flymile.controller;

import ca.flymile.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-link")
public class BookingController {

    private final BookingService alaskaBookingService;
    private final BookingService deltaBookingService;
    private final BookingService americanBookingService;

    public BookingController(@Qualifier("alaskaBookingServiceImpl") BookingService alaskaBookingService,
                             @Qualifier("deltaBookingServiceImpl") BookingService deltaBookingService,
                             @Qualifier("americanBookingServiceImpl") BookingService americanBookingService) {
        this.alaskaBookingService = alaskaBookingService;
        this.deltaBookingService = deltaBookingService;
        this.americanBookingService = americanBookingService;
    }

    @GetMapping
    public String getBookingLink(@RequestParam String airline, @RequestParam String departure, @RequestParam String arrival, @RequestParam String startDate, @RequestParam int numPassengers) {
        return switch (airline)
        {
            case "AS" -> alaskaBookingService.getBookingLink(departure, arrival, startDate, numPassengers);
            case "DL" -> deltaBookingService.getBookingLink(departure, arrival, startDate, numPassengers);
            case "AA" -> americanBookingService.getBookingLink(departure, arrival, startDate, numPassengers);
            default -> "https://www.flymile.ca/";
        };
    }
}
