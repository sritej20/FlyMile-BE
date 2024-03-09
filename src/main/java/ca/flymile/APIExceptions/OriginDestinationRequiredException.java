package ca.flymile.APIExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that both origin and destination are required but not provided.
 * This exception is annotated with {@link ResponseStatus} to automatically set the HTTP status code to 400 (Bad Request)
 * when this exception is thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OriginDestinationRequiredException extends RuntimeException {

    /**
     * Constructs a new OriginDestinationRequiredException with a default error message.
     */
    public OriginDestinationRequiredException() {
        super("Origin and destination must be provided.");
    }
}
