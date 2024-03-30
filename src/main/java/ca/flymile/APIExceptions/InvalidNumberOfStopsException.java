package ca.flymile.APIExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating that the number of stops provided is invalid.
 * This exception is annotated with {@link ResponseStatus} to automatically set the HTTP status code to 400 (Bad Request)
 * when this exception is thrown. It is also annotated with {@link ResponseBody} to indicate that the exception
 * message should be included in the response body.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseBody
public class InvalidNumberOfStopsException extends RuntimeException {

    /**
     * Constructs a new InvalidNumberOfStopsException with the default error message.
     */
    public InvalidNumberOfStopsException() {
        super("Invalid number of stops: must be an integer between 0 and 3.");
    }

    /**
     * Constructs a new InvalidNumberOfStopsException with a custom error message.
     *
     * @param message The custom error message.
     */
    public InvalidNumberOfStopsException(String message) {
        super(message);
    }
}

