package com.vosmann.redis.mutex.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Sample cache implementation.
 */
public class Cache {

    private static final Logger LOG = LoggerFactory.getLogger(Cache.class);

    private final JedisPool jedisPool = PoolFactory.newJedisPool();

    void write(final String key, final String recos) {

        final Jedis jedis = jedisPool.getResource();

        // Delete in case data hasn't expired yet. Pushing without delete may make the list grow bigger than 1 element.
        delete(key, jedis);

        try {
            jedis.lpush(key, recos);
        } catch (final RuntimeException e) {
            LOG.error("Could not write Recos to Redis. Key: {}", key);
        } finally {
            expire(key, jedis);
            jedis.close();
        }
    }

    /**
     * Every read effectively performs a rotation on the 1-element list. This is achieved with a blocking call, popping
     * the recos object from the left and pushing it back to the right of the same list.
     */
    String read(final String key) {

        final Jedis jedis = jedisPool.getResource();
        try {
            final String recos = jedis.brpoplpush(key, key, Config.MAX_CACHE_WAIT_TIME);
            return recos;
        } catch (final RuntimeException e) {
            LOG.error("Read failed.", e);
            return "Failure."; // Sample.
        } finally {
            expire(key, jedis);
            jedis.close();
        }
    }

    private void delete(final String key, final Jedis jedis) {
        LOG.debug("Deleting data on key: {}.", key);
        try {
            final long nrDeleted = jedis.del(key); // Check possible.
        } catch (final RuntimeException e) {
            LOG.error("Delete failed. Could not delete recos for key: {}", key);
        }
    }

    private void expire(final String key, final Jedis jedis) {
        LOG.debug("Expiring data key {} in {} seconds .", key, Config.RECOS_TTL);
        try {
            jedis.expire(key, Config.RECOS_TTL);
        } catch (final RuntimeException e) {
            LOG.error("Expire failed on key: {}", key);
        }
    }

}
