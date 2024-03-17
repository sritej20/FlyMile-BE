package ca.flymile.service;

import ca.flymile.ModelAlaska30Days.MonthlyDetails;
import ca.flymile.ModelAlaska30Days.dailyCheapest;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import org.springframework.stereotype.Component;

import java.util.List;
import static ca.flymile.API.RequestHandlerAlaska30Days.requestHandlerAlaska30Days;

@Component
public class Alaska30Days {



    /**
     * Retrieves a list of flight data based on the provided search parameters.
     *
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param start          The start date of the travel period (format: "YYYY-MM-DD").

     * @return A list of dailyCheapest data, each containing price in points, price in cash and Date.</p>
     */

    public List<dailyCheapest> getFlightDataListAlaska30Days(String origin, String destination, String start) {
        String json = requestHandlerAlaska30Days(origin, destination, start);
        Gson gson = new Gson();
        Type type = new TypeToken<MonthlyDetails>() {}.getType();
        MonthlyDetails monthlyDetails = gson.fromJson(json, type);
        return monthlyDetails.shoulderDates();
    }
}
