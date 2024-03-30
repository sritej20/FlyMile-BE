package ca.flymile.ControllerAdvisorr;

import ca.flymile.APIExceptions.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

/**
 * The GlobalExceptionHandler class handles exceptions globally for the controllers in the application.
 * It provides exception handling for various types of exceptions and returns appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles StartDateOutsideRangeException and returns an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(StartDateOutsideRangeException.class)
    @ResponseBody
    public ResponseEntity<Object> handleStartDateOutsideRangeException(StartDateOutsideRangeException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidDateFormatException and returns an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(InvalidDateFormatException.class)
    @ResponseBody
    public ResponseEntity<Object> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles OriginDestinationRequiredException and returns an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(OriginDestinationRequiredException.class)
    @ResponseBody
    public ResponseEntity<Object> handleOriginDestinationRequiredException(OriginDestinationRequiredException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles OriginDestinationSameException and returns an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(OriginDestinationSameException.class)
    @ResponseBody
    public ResponseEntity<Object> handleOriginDestinationSameException(OriginDestinationSameException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles PassengersNumberInvalidException and returns an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(PassengerNumberInvalidException.class)
    @ResponseBody
    public ResponseEntity<Object> handlePassengerNumberInvalidException(PassengerNumberInvalidException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles PassengersNumberInvalidException and returns an HTTP 400 (Bad Request) response with the error message.
     * This is Unique to alaska Airlines:
     *      Which permits one to seven passengers only
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(PassengerNumberInvalidExceptionAlaska.class)
    @ResponseBody
    public ResponseEntity<Object> handlePassengerNumberInvalidExceptionAlaska(PassengerNumberInvalidExceptionAlaska ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles EndDateOutsideRangeException and returns an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(EndDateOutsideRangeException.class)
    @ResponseBody
    public ResponseEntity<Object> handleEndDateOutsideRangeException(EndDateOutsideRangeException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles DestinationAirportInvalidException by returning an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(DestinationAirportInvalidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleDestinationAirportInvalidException(DestinationAirportInvalidException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles OriginAirportInvalidException by returning an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(OriginAirportInvalidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleOriginAirportInvalidException(OriginAirportInvalidException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles InvalidNumberOfStopsException by returning an HTTP 400 (Bad Request) response with the error message.
     *
     * @param ex The exception to handle.
     * @return A ResponseEntity containing an ApiError with the error message and HTTP status 400.
     */
    @ExceptionHandler(InvalidNumberOfStopsException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleInvalidNumberOfStopsException(InvalidNumberOfStopsException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
