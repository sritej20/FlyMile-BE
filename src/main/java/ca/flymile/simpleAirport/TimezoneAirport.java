package ca.flymile.simpleAirport;

import lombok.Data;

@Data
public class TimezoneAirport {


    /**
     * Represents an airport entity, encapsulating details such as its IATA code, city location, and timezone.
     * Utilizes the Lombok {@link Data} annotation to automatically generate boilerplate code like getters, setters,
     * and {@code toString}, {@code equals}, and {@code hashCode} methods, enhancing maintainability and reducing clutter.
     * <p>
     * This class is part of the simpleAirport module of the FlyMile project, designed to model airport-related information
     * for various applications within the project, including but not limited to flight scheduling and geographical services.
     * </p>
     *
     */

        /**
         * The IATA airport code, a globally recognized three-letter identifier for airports.
         * These codes are assigned by the International Air Transport Association (IATA)
         * and are widely used in the aviation industry and related services for ticketing,
         * baggage handling, and flight planning.
         */
        private String airportCode;

        /**
         * The name of the city where the airport is located. This field is particularly useful
         * for distinguishing airports within larger metropolitan areas that may host multiple
         * airports or when an airport serves as the primary airport for multiple nearby cities.
         */
        private String cityName;

        /**
         * The timezone in which the airport is located, represented in the IANA Time Zone Database format,
         * such as "America/New_York". This information is critical for calculating local times of flights,
         * scheduling services, and providing users with location-aware notifications.
         * <p>
         * In cases where the exact timezone could not be determined, especially for smaller or less known airports,
         * "Europe/London" has been used as a default placeholder. This choice is based on London's GMT/UTC+0 timezone,
         * providing a neutral baseline. Approximately 100 out of 1627 catalogued airports fall into this category.
         * Efforts to refine these default assignments with more accurate data are ongoing.
         * </p>
         * <b>Example:</b>
         * <pre>
         * {
         *   "airportCode": "DOH",
         *   "cityName": "Doha",
         *   "timezone": "Asia/Qatar"
         * }
         * </pre>
         */
        private String timezone;
    }

