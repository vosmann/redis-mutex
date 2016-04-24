package com.vosmann.redis.mutex.sample;

/**
 * Sample configuration values in a static class.
 */
public class Config {

    public static final String HOST = "abc.cache.amazonaws.com";
    public static final int PORT = 6379;
    public static final int READ_TIMEOUT = 100; // Connection and socket timeout.
    public static final int MAX_WAIT_MILLIS = 100; // Max wait time when leasing a connection.
    public static final int MAX_CACHE_WAIT_TIME = 1; // Max wait time for lock winner to write recos to cache.
    public static final int MAX_NR_CONNECTIONS = 100;
    public static final int MAX_NR_IDLE_CONNECTIONS = 200;
    public static final int LOCK_TTL = 600; // Seconds.
    public static final int RECOS_TTL = 900; // Seconds.

}
