package ca.flymile.service;

import ca.flymile.ModelBookingDotCom.BookingDotComModel;
import ca.flymile.ModelBookingDotCom.MinPriceRound;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static ca.flymile.API.BookingDotComAPI.getFlightDetails;
@Component
@RequiredArgsConstructor
public class cashPriceComparison
{
    private final Gson gson = new Gson();
    public MinPriceRound getCashPriceComparison(int adults, String cabinClass, String from, String to, String departDate, String airline)
    {
        String response = getFlightDetails(adults, cabinClass, from, to, departDate, airline);
        if(response == null)
            return null;
        BookingDotComModel bookingDotComModel = gson.fromJson(response, BookingDotComModel.class);
        return bookingDotComModel.getMinPriceRound();
    }

}


