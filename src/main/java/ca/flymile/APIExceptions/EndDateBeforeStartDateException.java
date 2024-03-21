package ca.flymile.APIExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that the end date provided is before the start date.
 * This exception is annotated with {@link ResponseStatus} to automatically set
 * the HTTP status code to 400 (Bad Request) when this exception is thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EndDateBeforeStartDateException extends RuntimeException {

    /**
     * Constructs a new EndDateBeforeStartDateException with a default error message.
     */
    public EndDateBeforeStartDateException() {
        super("End date cannot be before start date.");
    }
}
