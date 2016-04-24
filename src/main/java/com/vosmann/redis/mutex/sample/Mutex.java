package com.vosmann.redis.mutex.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Sample implementation of the rudimentary mutual exclusion mechanism used to ensure that only one request
 * per e-mail computes the recos.
 */
public class Mutex {

    private static final Logger LOG = LoggerFactory.getLogger(Mutex.class);

    private final JedisPool jedisPool = PoolFactory.newJedisPool();

    boolean getLock(final String key) {

        final Jedis jedis = jedisPool.getResource();
        try {
            final long result = jedis.hsetnx(key, "first", "got it");
            final boolean wonLock = (result == 1);
            return wonLock;
        } catch (final RuntimeException e) {
            LOG.error("Could not set lock. Returning false; will continue to blocking read.", e);
            return false;
        } finally {
            expire(key, jedis);
            jedis.close();
        }
    }

    private void expire(final String key, final Jedis jedis) {
        final int lockTtl = Config.LOCK_TTL;
        LOG.debug("Setting TTL {} seconds on key: {}.", lockTtl, key);
        try {
            jedis.expire(key, lockTtl); // Every thread that tries to set the lock, sets lock expiry too.
        } catch (final RuntimeException e) {
            LOG.error("Expire failed. Could not set TTL on lock key: {}", key);
        }
    }

}
