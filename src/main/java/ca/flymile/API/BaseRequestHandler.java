package ca.flymile.API;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static ca.flymile.DecompressorLogic.Decompressor.decompress;

public class BaseRequestHandler {
    public static String getJsonStringFromAirline(HttpClient client, HttpRequest request) {
        CompletableFuture<String> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(response -> {
                    // Dynamically get the content encoding from the response headers
                    String contentEncoding = response.headers().firstValue("Content-Encoding").orElse("");
                    return decompress(response.body(), contentEncoding);
                })
                .thenApply(decompressedBody -> {
                    if (decompressedBody == null) {
                        System.err.println("There is issue in decompressedBody == null");
                        return null;
                    }
                    return new String(decompressedBody);
                });

        try {
            return future.get();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            System.err.println(e.getMessage());
            return null;
        }
    }
}
