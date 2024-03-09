package ca.flymile.APIExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that the provided date format is invalid.
 * This exception is annotated with {@link ResponseStatus} to automatically set the HTTP status code to 400 (Bad Request)
 * when this exception is thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateFormatException extends RuntimeException {

    /**
     * Constructs a new InvalidDateFormatException with a default error message.
     */
    public InvalidDateFormatException() {
        super("Invalid date format, expected yyyy-MM-dd.");
    }
}

