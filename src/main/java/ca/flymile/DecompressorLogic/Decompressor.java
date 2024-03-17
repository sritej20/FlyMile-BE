package ca.flymile.DecompressorLogic;

import ca.flymile.CompressionExceptions.DecompressionDeflateException;
import ca.flymile.CompressionExceptions.DecompressionException;
import ca.flymile.CompressionExceptions.DecompressionGzipException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * The Decompressor class provides methods to decompress data using different compression algorithms.
 */
public class Decompressor {

    /**
     * Decompresses the provided data based on the specified encoding.
     *
     * @param data     The compressed data to decompress.
     * @param encoding The encoding used for compression (e.g., "gzip", "deflate").
     * @return The decompressed data.
     */
    public static byte[] decompress(byte[] data, String encoding) {
        try {
            return switch (encoding) {
                case "gzip" -> decompressGzip(data);
                case "deflate" -> decompressDeflate(data);
                default -> data;
            };
        } catch (DecompressionException e) {
            System.err.println("Decompression error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decompresses GZIP compressed data.
     *
     * @param compressedData The GZIP compressed data to decompress.
     * @return The decompressed data.
     * @throws DecompressionGzipException If an error occurs during GZIP decompression.
     */
    private static byte[] decompressGzip(byte[] compressedData) throws DecompressionGzipException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             GZIPInputStream gis = new GZIPInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new DecompressionGzipException("Failed to decompress data using GZIP.", e);
        }
    }

    /**
     * Decompresses DEFLATE compressed data.
     *
     * @param compressedData The DEFLATE compressed data to decompress.
     * @return The decompressed data.
     * @throws DecompressionDeflateException If an error occurs during DEFLATE decompression.
     */
    private static byte[] decompressDeflate(byte[] compressedData) throws DecompressionDeflateException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             InflaterInputStream iis = new InflaterInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = iis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new DecompressionDeflateException("Failed to decompress data using deflate.", e);
        }
    }
}
