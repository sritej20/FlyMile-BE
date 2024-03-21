package ca.flymile.API;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PassengerJsonProvider {

    private static final Map<Integer, String> passengersJsonMap = new HashMap<>();

    static {
        for (int i = 0; i <= 8; i++) {
            String passengersJson = IntStream.rangeClosed(1, i)
                    .mapToObj(id -> String.format("{\"passengerTypeCode\":\"ADT\",\"passengerId\":\"%d\"}", id))
                    .collect(Collectors.joining(",", "[", "]"));
            passengersJsonMap.put(i, passengersJson);

        }
    }

    public static String getPassengersJson(int numPassengers) {
        return passengersJsonMap.getOrDefault(numPassengers, "[]");
        //Default should never happen
        //As Passengers can only be between 1,9
    }
}
