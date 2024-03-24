package ca.flymile.APIExceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Represents an error that occurred during API processing. This is FlyMile API.
 * Errors are related to invalid request parameters.
 * Need to validate airports as well:
 * Found reputable data from DELTA AIR LINES, need to process it. Will make HashMap of airport code, then will look for
 * both origin and destination being present, if not throw 2 new exceptions , InvalidOrigin / InvalidDestination
 */
@Setter
@Getter
public class ApiError {

    /**
     * -- GETTER --
     *  Retrieves the HTTP status code associated with the error.
     * -- SETTER --
     *  Sets the HTTP status code associated with the error.
     */
    private HttpStatus status; // The HTTP status code associated with the error.
    /**
     * -- GETTER --
     *  Retrieves the message describing the error.
     * -- SETTER --
     *  Sets the message describing the error.
     */
    private String message;    // A message describing the error.

    /**
     * Constructs a new ApiError instance with the specified HTTP status and message.
     *
     * @param status  The HTTP status code of the error.
     * @param message A message describing the error.
     */
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
