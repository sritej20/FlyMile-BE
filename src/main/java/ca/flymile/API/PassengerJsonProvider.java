package ca.flymile.API;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PassengerJsonProvider {

    private static final String[] passengersJsonMap = new String[10];

    static {
        for (int i = 1; i <= 9; i++) {
            String passengersJson = IntStream.rangeClosed(1, i)
                    .mapToObj(id -> String.format("{\"passengerTypeCode\":\"ADT\",\"passengerId\":\"%d\"}", id))
                    .collect(Collectors.joining(",", "[", "]"));
            passengersJsonMap[i] = passengersJson;

        }
    }

    public static String getPassengersJson(int numPassengers) {
        return passengersJsonMap[numPassengers];
    }
}
