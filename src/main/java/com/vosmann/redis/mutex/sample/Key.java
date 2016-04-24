package com.vosmann.redis.mutex.sample;

final class Key {

    private final String lockKey;
    private final String dataKey;

    public static Key from(final String params) {
        return new Key(params);
    }

    private Key(final String key) {
        this.lockKey = "LOCK-" + key;
        this.dataKey = "LIST-" + key;
    }

    public String forLock() {
        return lockKey;
    }

    public String forData() {
        return dataKey;
    }

}
