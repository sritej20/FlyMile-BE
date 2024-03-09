package ca.flymile.APIExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that the end date provided is outside the allowed range.
 * This exception is annotated with {@link ResponseStatus} to automatically set the HTTP status code to 400 (Bad Request)
 * when this exception is thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EndDateOutsideRangeException extends RuntimeException {

    /**
     * Constructs a new EndDateOutsideRangeException with a default error message.
     */
    public EndDateOutsideRangeException() {
        super("End date must be up to 7 days after start date and fall within 331 days from today.");
    }
}
