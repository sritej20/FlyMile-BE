package ca.flymile.APIExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that the origin and destination provided are the same, but they must be different.
 * This exception is annotated with {@link ResponseStatus} to automatically set the HTTP status code to 400 (Bad Request)
 * when this exception is thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OriginDestinationSameException extends RuntimeException {

    /**
     * Constructs a new OriginDestinationSameException with a default error message.
     */
    public OriginDestinationSameException() {
        super("Origin and destination must be different.");
    }
}
