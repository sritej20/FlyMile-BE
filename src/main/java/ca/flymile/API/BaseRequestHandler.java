package ca.flymile.API;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static ca.flymile.DecompressorLogic.Decompressor.decompress;

public class BaseRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseRequestHandler.class);

    public static String getJsonStringFromAirline(HttpClient client, HttpRequest request) {
        CompletableFuture<String> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(response -> {
                    // Dynamically get the content encoding from the response headers
                    String contentEncoding = response.headers().firstValue("Content-Encoding").orElse("");
                    return decompress(response.body(), contentEncoding);
                })
                .thenApply(decompressedBody -> {
                    if (decompressedBody == null) {
                        logger.error("There is issue in decompressedBody == null");
                        return null;
                    }
                    return new String(decompressedBody);
                });

        try {
            return future.get();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            logger.error("Error processing request", e);
            return null;
        }
    }
}
