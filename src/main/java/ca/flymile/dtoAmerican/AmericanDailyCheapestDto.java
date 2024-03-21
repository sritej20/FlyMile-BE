package ca.flymile.dtoAmerican;

import ca.flymile.ModelAmericanWeekly.dailyCheapest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class AmericanDailyCheapestDto {
    private String date;
    private int points;
    private double cashPrice;
    public static AmericanDailyCheapestDto toDto(dailyCheapest cheapest) {
        return new AmericanDailyCheapestDto()
                .setDate(cheapest.getDate())
                .setPoints(stringToInt(cheapest.getPoints()))
                .setCashPrice(stringToDouble(cheapest.getCashPrice()));

    }
    private static int stringToInt(String str)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }

    }
    private static double stringToDouble(String str)
    {
        try
        {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }

    }
}


