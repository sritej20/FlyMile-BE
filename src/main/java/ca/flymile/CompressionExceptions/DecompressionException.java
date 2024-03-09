package ca.flymile.CompressionExceptions;

/**
 * An exception indicating an error during decompression.
 */
public class DecompressionException extends Exception {

    /**
     * Constructs a new DecompressionException with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the getCause() method).
     */
    public DecompressionException(String message, Throwable cause) {
        super(message, cause);
    }
}
