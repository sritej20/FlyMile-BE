package ca.flymile.InputValidation;

import ca.flymile.APIExceptions.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import static ca.flymile.FlyMileAirportData.AirportData.airportSet;

/**
 * Provides static methods for validating flight search parameters to ensure they meet
 * the application's requirements and constraints.
 */
public class InputValidation {

    /**
     * Validates the provided flight search parameters against a set of predefined rules.
     *
     * - Validates date formats for start and end dates.
     * - Checks if the origin and destination airports are valid and within the known airport set.
     * - Ensures the start date is not before today and within 331 days from today.
     * - Verifies that the origin and destination are provided and not the same.
     * - Validates the number of passengers is within acceptable bounds.
     * - Checks if the end date is logical in relation to the start date and within bounds.
     *
     * @param origin The IATA airport code for the flight's origin.
     * @param destination The IATA airport code for the flight's destination.
     * @param startDate The intended start date of travel, in YYYY-MM-DD format.
     * @param endDate The intended end date of travel, in YYYY-MM-DD format.
     * @param numPassengers The number of passengers traveling.
     * @throws InvalidDateFormatException If either date does not follow the expected format.
     * @throws OriginAirportInvalidException If the origin airport code is not recognized.
     * @throws DestinationAirportInvalidException If the destination airport code is not recognized.
     * @throws StartDateOutsideRangeException If the start date does not fall within the allowed timeframe.
     * @throws OriginDestinationRequiredException If either the origin or destination is not provided.
     * @throws OriginDestinationSameException If the origin and destination are the same.
     * @throws PassengersNumberInvalidException If the number of passengers is outside the allowed range.
     * @throws EndDateOutsideRangeException If the end date does not fall within the allowed timeframe relative to the start date.
     */
    public static void validateFlightSearchParams(String origin, String destination, String startDate, String endDate, int numPassengers) {
        validateAirports(origin, destination);
        LocalDate start = parseAndValidateStartDate(startDate);
        LocalDate end = parseAndValidateEndDate(endDate, start);
        validateNumPassengers(numPassengers);
    }

    private static void validateAirports(String origin, String destination) {
        if (origin == null || origin.trim().isEmpty() || destination == null || destination.trim().isEmpty()) {
            throw new OriginDestinationRequiredException();
        }

        if (!airportSet.contains(origin.toUpperCase())) {
            throw new OriginAirportInvalidException();
        }

        if (!airportSet.contains(destination.toUpperCase())) {
            throw new DestinationAirportInvalidException();
        }

        if (origin.equalsIgnoreCase(destination)) {
            throw new OriginDestinationSameException();
        }
    }

    private static LocalDate parseAndValidateStartDate(String startDate) {
        LocalDate start;
        try {
            start = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        LocalDate today = LocalDate.now();
        if (start.isBefore(today) || start.isAfter(today.plusDays(331))) {
            throw new StartDateOutsideRangeException();
        }

        return start;
    }

    private static LocalDate parseAndValidateEndDate(String endDate, LocalDate start) {
        LocalDate end;
        try {
            end = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        LocalDate today = LocalDate.now();
        if (end.isBefore(start) || end.isAfter(start.plusDays(7)) || end.isAfter(today.plusDays(331))) {
            throw new EndDateOutsideRangeException();
        }

        return end;
    }

    private static void validateNumPassengers(int numPassengers) {
        if (numPassengers < 1 || numPassengers > 9) {
            throw new PassengersNumberInvalidException();
        }
    }
}
