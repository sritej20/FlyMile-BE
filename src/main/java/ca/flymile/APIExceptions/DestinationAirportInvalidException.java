package ca.flymile.APIExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the provided destination airport code is invalid or not supported.
 * <p>
 * This exception is used to indicate that the destination airport code passed to an API endpoint
 * does not match any known or supported airports. It triggers a HTTP 400 Bad Request response when thrown.
 * </p>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DestinationAirportInvalidException extends RuntimeException {

    /**
     * Constructs a new DestinationAirportInvalidException with the default error message.
     */
    public DestinationAirportInvalidException() {
        super("The destination airport code is invalid or not supported.");
    }
}