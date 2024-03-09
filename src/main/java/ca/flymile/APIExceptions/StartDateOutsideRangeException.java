package ca.flymile.APIExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that the start date provided is outside the allowed range.
 * This exception is annotated with {@link ResponseStatus} to automatically set the HTTP status code to 400 (Bad Request)
 * when this exception is thrown. It is also annotated with {@link ResponseBody} to indicate that the exception
 * message should be included in the response body.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseBody
public class StartDateOutsideRangeException extends RuntimeException {

    /**
     * Constructs a new StartDateOutsideRangeException with a default error message.
     */
    public StartDateOutsideRangeException() {
        super("Start date must be from today to 331 days in the future");
    }
}
