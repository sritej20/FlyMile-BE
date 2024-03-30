package ca.flymile.InputValidation;

import ca.flymile.APIExceptions.*;
import ca.flymile.service.DateHandler;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


import static ca.flymile.FlyMileAirportData.AirportData.airportSet;
import static ca.flymile.FlyMileAirportData.AirportTimeZoneMap.AIRPORT_TIMEZONE_MAP;

/**
 * Provides static methods for validating flight search parameters to ensure they meet
 * the application's requirements and constraints.
 */
public class InputValidation {

    private static final Map<String, Boolean> CABIN_CLASSES = new HashMap<>();

    static {
        CABIN_CLASSES.put("BE", true);
        CABIN_CLASSES.put("MAIN", true);
        CABIN_CLASSES.put("DCP", true);
        CABIN_CLASSES.put("FIRST", true);
        CABIN_CLASSES.put("DPPS", true);
        CABIN_CLASSES.put("D1", true);
    }

    public static String validateCabinClassDelta(String input) {
        return CABIN_CLASSES.containsKey(input) ? input : "BE";
    }
    /**
     * Validates the provided flight search parameters against a set of predefined rules.*
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
     * @throws OriginDestinationRequiredException If either, the origin or destination is not provided.
     * @throws OriginDestinationSameException If the origin and destination are the same.
     * @throws PassengerNumberInvalidException If the number of passengers is outside the allowed range.
     * @throws EndDateOutsideRangeException If the end date does not fall within the allowed timeframe relative to the start date.
     */
    public static void validateOriginDestinationStartDateZoneEndDatePassengers(String origin, String destination, String startDate, String endDate, int numPassengers) {
        validateOriginDestination(origin, destination);
        ZoneId originZoneId = getZoneIdForAirport(origin);
        LocalDate start = StartDateZone(startDate, originZoneId);
        validateEndDateWithoutZone(endDate, start);
        validateNumPassengers(numPassengers);
    }

    public static void validateOriginDestinationStartDateZoneEndDateNumPassengersAlaska(String origin, String destination, String startDate, String endDate, int numPassengers) {
        validateOriginDestination(origin, destination);
        ZoneId originZoneId = getZoneIdForAirport(origin);
        LocalDate start = StartDateZone(startDate, originZoneId);
        validateEndDateWithoutZone(endDate, start);
        validateNumPassengersAlaska(numPassengers);
    }
    public static void validateOriginDestinationStartDateZoneNumPassengersAlaska(String origin, String destination, String startDate, int numPassengers) {
        validateOriginDestination(origin, destination);
        ZoneId originZoneId = getZoneIdForAirport(origin);
        StartDateZone(startDate, originZoneId);
        validateNumPassengersAlaska(numPassengers);
    }
    public static void validateOriginDestinationNumPassengersAlaska(String origin, String destination, int numPassengers) {
        validateOriginDestination(origin, destination);
        validateNumPassengersAlaska(numPassengers);
    }
    private static void validateNumPassengersAlaska(int numPassengers) {
        if (numPassengers < 1 || numPassengers > 7) {
            throw new PassengerNumberInvalidExceptionAlaska();
        }
    }

    /**
     * Validates the flight search parameters for American yearly flights.
     * <p>
     * This method checks if the provided airport codes are valid
     * and if the number of passengers is within the allowed limits.
     * Note that, unlike the weekly validation, it does not check the start date
     * as the yearly data retrieval does not require a specific start date.
     * </p>
     *
     * @param origin        the origin airport code
     * @param destination   the destination airport code
     * @param numPassengers the number of passengers should be between 1 and 9 inclusive
     * @throws IllegalArgumentException if any of the parameters are invalid
     */
    public static void validateOriginDestinationPassengers(String origin, String destination, int numPassengers)
    {
        validateOriginDestination(origin, destination);
        validateNumPassengers(numPassengers);
    }
    /**
     * Validates the flight search parameters for American weekly flights.
     * <p>
     * This method checks if the provided airport codes are valid,
     * if the start date is correctly formatted and within an acceptable range,
     * and if the number of passengers is within the allowed limits.
     * </p>
     *
     * @param origin        the origin airport code
     * @param destination   the destination airport code
     * @param startDate     the start date of the travel period in "YYYY-MM-DD" format
     * @param numPassengers the number of passengers should be between 1 and 9 inclusive
     * @throws IllegalArgumentException if any of the parameters are invalid
     */
    public static void validateOriginDestinationStartDatePassengers(String origin, String destination, String startDate, int numPassengers) {
        validateOriginDestinationPassengers(origin, destination, numPassengers);
        StartDateWithoutZone(startDate);

    }

    /**
     * Parses and validate a start date string, ensuring it is in a valid format and within an acceptable date range.
     * This method does not account for time zones in its validation, treating the start date as a local date.
     *
     * @param startDate The start date string to be parsed and validated, in the format of "YYYY-MM-DD".
     * @throws InvalidDateFormatException If the start date does not conform to the expected date format.
     * @throws StartDateOutsideRangeException If the start date is before today or more than 331 days in the future.
     */
    private static void StartDateWithoutZone(String startDate) {
        LocalDate start;
        try {
            start = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        if (start.isBefore(DateHandler.getCurrentDate()) || start.isAfter(DateHandler.getLimitDate())) {
            throw new StartDateOutsideRangeException();
        }
    }
    private static void validateEndDateWithoutZone(String endDate, LocalDate startDate) {
        LocalDate  end;
        try {
              end = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        if (end.isBefore(startDate)) {
            throw new EndDateBeforeStartDateException();
        } else if (end.isAfter(DateHandler.getLimitDate())) {
            throw new EndDateOutsideRangeException();
        }
    }
    public static void validateOrigin(String origin) {
        if (!airportSet.contains(origin))
            throw new OriginAirportInvalidException();

    }

    public static void validateOriginDestination(String origin, String destination) {
        if (origin == null || origin.trim().isEmpty() || destination == null || destination.trim().isEmpty()) {
            throw new OriginDestinationRequiredException();
        }

        if (!airportSet.contains(origin)) {
            throw new OriginAirportInvalidException();
        }

        if (!airportSet.contains(destination)) {
            throw new DestinationAirportInvalidException();
        }

        if (origin.equalsIgnoreCase(destination)) {
            throw new OriginDestinationSameException();
        }
    }

    private static void validateNumPassengers(int numPassengers) {
        if (numPassengers < 1 || numPassengers > 9) {
            throw new PassengerNumberInvalidException();
        }
    }
    private static ZoneId getZoneIdForAirport(String airportCode) {
        String timeZone = getAirportTimeZone(airportCode);
        if (timeZone == null) {
            timeZone = "Europe/London";   //Should not happen
        }
        return ZoneId.of(timeZone);
    }

    private static LocalDate StartDateZone(String startDate, ZoneId zoneId) {
        LocalDate start;
        try {
            start = LocalDate.parse(startDate);
            // Convert start date to ZonedDateTime at the start of the day in the specified zone
            ZonedDateTime startDateTime = start.atStartOfDay(zoneId);
            // Get the current date and time, then truncate to the start of the day for an accurate comparison
            ZonedDateTime today = ZonedDateTime.now(zoneId).truncatedTo(ChronoUnit.DAYS);


            // Check if the start date is before today or more than 331 days ahead
            if (startDateTime.isBefore(today) || startDateTime.isAfter(today.plusDays(331))) {
                throw new StartDateOutsideRangeException();
            }
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
        return start;
    }
    public static String parseAndValidateStops(String maxStops) {
        // Use a default value if maxStops is null or blank
        String trimmedStops = (maxStops == null || maxStops.trim().isEmpty()) ? "3" : maxStops.trim();

        int stops;
        try {
            stops = Integer.parseInt(trimmedStops);
        } catch (NumberFormatException e) {
            throw new InvalidNumberOfStopsException("Invalid number of stops: must be an integer.");
        }

        if (stops < 0 || stops > 3) {
            throw new InvalidNumberOfStopsException();
        }

        return String.valueOf(stops);
    }

    private static String getAirportTimeZone(String airportCode)
    {
        return AIRPORT_TIMEZONE_MAP.get(airportCode);
    }
}