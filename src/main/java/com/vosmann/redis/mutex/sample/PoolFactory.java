package com.vosmann.redis.mutex.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Sample Redis connection pool factory.
 */
public class PoolFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PoolFactory.class);

    public static JedisPool newJedisPool() {

        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Config.MAX_NR_CONNECTIONS);
        poolConfig.setMaxIdle(Config.MAX_NR_IDLE_CONNECTIONS);
        poolConfig.setMaxWaitMillis(Config.MAX_WAIT_MILLIS);

        // Jedis uses this one timeout value for both connection and socket timeout.
        final JedisPool jedisPool = new JedisPool(poolConfig, Config.HOST, Config.PORT, Config.READ_TIMEOUT);

        logPool();
        return jedisPool;
    }

    private static void logPool() {
        LOG.info("New JedisPool. Host: {}, port: {}, maxConns: {}, maxIdleConns: {}, maxWaitMillis: {}, readTimeout: {}",
                Config.HOST, Config.PORT, Config.MAX_NR_CONNECTIONS, Config.MAX_NR_IDLE_CONNECTIONS,
                Config.MAX_WAIT_MILLIS, Config.READ_TIMEOUT);
    }

}
