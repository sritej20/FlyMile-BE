package ca.flymile.APIExceptions;

import org.springframework.http.HttpStatus;

/**
 * Represents an error that occurred during API processing. This is FlyMile API.
 * Errors are related to invalid request parameters.
 * Need to validate airports as well:
 * Found reputable data from DELTA AIR LINES, need to process it. Will make HashMap of airport code, then will look for
 * both origin and destination being present, if not throw 2 new exceptions , InvalidOrigin / InvalidDestination
 */
public class ApiError {

    private HttpStatus status; // The HTTP status code associated with the error.
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

    /**
     * Retrieves the HTTP status code associated with the error.
     *
     * @return The HTTP status code.
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code associated with the error.
     *
     * @param status The HTTP status code to set.
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    /**
     * Retrieves the message describing the error.
     *
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message describing the error.
     *
     * @param message The error message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
