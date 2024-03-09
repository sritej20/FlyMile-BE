package ca.flymile.CompressionExceptions;

/**
 * An exception indicating an error during deflate decompression.
 */
public class DecompressionDeflateException extends DecompressionException {

    /**
     * Constructs a new DecompressionDeflateException with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the getCause() method).
     */
    public DecompressionDeflateException(String message, Throwable cause) {
        super(message, cause);
    }
}
