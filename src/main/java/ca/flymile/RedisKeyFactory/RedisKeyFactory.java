package ca.flymile.RedisKeyFactory;

public class RedisKeyFactory {
    public static String generateCacheKey(String airline , String... args) {
        return String.format("%s:%s",airline, String.join(":", args));
    }
}
