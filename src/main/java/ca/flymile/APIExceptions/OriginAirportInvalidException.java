package ca.flymile.APIExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the provided origin airport code is invalid or not supported.
 * <p>
 * Similar to the DestinationAirportInvalidException, this exception signifies that the origin
 * airport code provided in a request is unrecognized or unsupported, leading to a HTTP 400 Bad Request
 * response.
 * </p>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OriginAirportInvalidException extends RuntimeException {

    /**
     * Constructs a new OriginAirportInvalidException with the default error message.
     */
    public OriginAirportInvalidException() {
        super("The origin airport code is invalid or not supported.");
    }
}




