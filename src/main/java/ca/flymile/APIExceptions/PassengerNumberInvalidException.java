package ca.flymile.APIExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that the number of passengers provided is invalid.
 * This exception is annotated with {@link ResponseStatus} to automatically set the HTTP status code to 400 (Bad Request)
 * when this exception is thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PassengerNumberInvalidException extends RuntimeException {

    /**
     * Constructs a new PassengersNumberInvalidException with a default error message.
     */
    public PassengerNumberInvalidException() {
        super("Number of passengers must be between 1 and 9.");
    }
}
