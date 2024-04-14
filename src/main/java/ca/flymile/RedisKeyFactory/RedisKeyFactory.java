package ca.flymile.RedisKeyFactory;

public class RedisKeyFactory {
    public static String DELTA_CODE = "DL";
    public static String ALASKA_CODE = "AS";
    public static String AMERICAN_CODE = "AA";
    public static String generateCacheKey(String airline , String... args) {
        return String.format("%s:%s",airline, String.join(":", args));
    }
}
