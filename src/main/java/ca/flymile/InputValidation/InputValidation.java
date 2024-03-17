package ca.flymile.InputValidation;

import ca.flymile.APIExceptions.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import static ca.flymile.FlyMileAirportData.AirportData.airportSet;
import static ca.flymile.FlyMileAirportData.AirportTimeZoneMap.AIRPORT_TIMEZONE_MAP;

/**
 * Provides static methods for validating flight search parameters to ensure they meet
 * the application's requirements and constraints.
 */
public class InputValidation {

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
     * @throws OriginDestinationRequiredException If either the origin or destination is not provided.
     * @throws OriginDestinationSameException If the origin and destination are the same.
     * @throws PassengersNumberInvalidException If the number of passengers is outside the allowed range.
     * @throws EndDateOutsideRangeException If the end date does not fall within the allowed timeframe relative to the start date.
     */
    public static void validateFlightSearchParams(String origin, String destination, String startDate, String endDate, int numPassengers) {
        validateAirports(origin, destination);
        ZoneId originZoneId = getZoneIdForAirport(origin);
        LocalDate start = parseAndValidateStartDate(startDate, originZoneId);
        parseAndValidateEndDate(endDate, start, originZoneId);
        validateNumPassengers(numPassengers);
    }
    /**
     * Validates flight search parameters for Alaska Airlines 30-day search feature, focusing on airport codes and start date.
     *
     * @param origin The IATA airport code for the flight's origin.
     * @param destination The IATA airport code for the flight's destination.
     * @param startDate The start date of the search period, in YYYY-MM-DD format.
     */
    public static void validateFlightSearchParamsForAlaska30Days(String origin, String destination, String startDate) {
        validateAirports(origin, destination);
        parseAndValidateStartDateWithoutZone(startDate);
    }
    /**
     * Parses and validates a start date string, ensuring it is in a valid format and within an acceptable date range.
     * This method does not account for time zones in its validation, treating the start date as a local date.
     *
     * @param startDate The start date string to be parsed and validated, in the format of "YYYY-MM-DD".
     * @throws InvalidDateFormatException If the start date does not conform to the expected date format.
     * @throws StartDateOutsideRangeException If the start date is before today or more than 331 days in the future.
     */
    private static void parseAndValidateStartDateWithoutZone(String startDate) {
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
    }

    private static void validateAirports(String origin, String destination) {
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
            throw new PassengersNumberInvalidException();
        }
    }
    private static ZoneId getZoneIdForAirport(String airportCode) {
        String timeZone = getAirportTimeZone(airportCode);
        if (timeZone == null) {
            timeZone = "Europe/London";   //Should not happen
        }
        return ZoneId.of(timeZone);
    }

    private static LocalDate parseAndValidateStartDate(String startDate, ZoneId zoneId) {
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


    private static void parseAndValidateEndDate(String endDate, LocalDate start, ZoneId zoneId) {
        LocalDate end;
        try {
            end = LocalDate.parse(endDate);
            ZonedDateTime endDateTime = end.atStartOfDay(zoneId);
            ZonedDateTime startDateTime = start.atStartOfDay(zoneId);
            LocalDate todayNewYork = LocalDate.now(); //Montreal and New York are in same timezone
            //AMERICAN RELEASE NEW SEATS AT 12.01 AM, FROM 331 DAYS TODAY
            if (endDateTime.isBefore(startDateTime) || endDateTime.isAfter(startDateTime.plusDays(7)) || end.isAfter(todayNewYork.plusDays(331))) {
                throw new EndDateOutsideRangeException();
            }
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }
    private static String getAirportTimeZone(String airportCode)
    {
        return AIRPORT_TIMEZONE_MAP.get(airportCode);
    }
}