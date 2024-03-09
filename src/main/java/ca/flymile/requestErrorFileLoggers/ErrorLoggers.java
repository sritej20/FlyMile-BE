package ca.flymile.requestErrorFileLoggers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The ErrorLoggers class provides methods for logging request errors to files.
 */
public class ErrorLoggers {
    /**
     * Saves successful requests to a log file.
     *
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param startDate      The start date of the travel period.
     * @param endDate        The end date of the travel period.
     * @param numPassengers  The number of passengers.
     * @param upperCabin     Indicates if upper cabin seats are preferred.
     * @param length         The length of the successful request.
     */
    public static void saveSuccessRequestToFile(String origin, String destination, String startDate, String endDate, int numPassengers, boolean upperCabin, int length) {
        String content = String.format("%s    %s    %s    %s    %d   %b    %d%n", origin, destination, startDate, endDate, numPassengers, upperCabin, length);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Success.log", true))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("An issue occurred while saving successful request to file: " + e.getMessage());
        }
    }

    /**
     * Saves failed requests to a log file.
     *
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param startDate      The start date of the travel period.
     * @param endDate        The end date of the travel period.
     * @param numPassengers  The number of passengers.
     * @param upperCabin     Indicates if upper cabin seats are preferred.
     */
    public static void saveFailRequestToFile(String origin, String destination, String startDate, String endDate, int numPassengers, boolean upperCabin) {
        String content = String.format("%s    %s    %s    %s    %d    %b%n", origin, destination, startDate, endDate, numPassengers, upperCabin);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Fail.log", true))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("An issue occurred while saving failed request to file: " + e.getMessage());
        }
    }

    /**
     * Saves unknown errors to a log file. For Future Knowledge
     *
     * @param error          The unknown error message.
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param startDate      The start date of the travel period.
     * @param numPassengers  The number of passengers.
     * @param upperCabin     Indicates if upper cabin seats are preferred.
     */
    public static void saveUnknownErrorToFile(String error, String origin, String destination, String startDate, int numPassengers, boolean upperCabin) {
        String content = String.format("%s    %s    %s    %s    %d    %b%n", error, origin, destination, startDate, numPassengers, upperCabin);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("unknownError.log", true))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("An issue occurred while saving unknown error to file: " + e.getMessage());
        }
    }

    /**
     * Saves error code 309 to a log file. Known Error means "No Flights Found For Given Input"
     *
     * @param error          The error message.
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param startDate      The start date of the travel period.
     * @param numPassengers  The number of passengers.
     * @param upperCabin     Indicates if upper cabin seats are preferred.
     */
    public static void save309ErrorToFile(String error, String origin, String destination, String startDate, int numPassengers, boolean upperCabin) {
        String content = String.format("%s    %s    %s    %s    %d    %b%n", error, origin, destination, startDate, numPassengers, upperCabin);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("309Error.log", true))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("An issue occurred while saving error 309 to file: " + e.getMessage());
        }
    }
}
