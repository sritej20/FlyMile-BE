package ca.flymile.CompressionExceptions;

/**
 * An exception indicating an error during gzip decompression.
 */
public class DecompressionGzipException extends DecompressionException {

    /**
     * Constructs a new DecompressionGzipException with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the getCause() method).
     */
    public DecompressionGzipException(String message, Throwable cause) {
        super(message, cause);
    }
}
