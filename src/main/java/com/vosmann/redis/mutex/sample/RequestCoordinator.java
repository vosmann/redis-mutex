package com.vosmann.redis.mutex.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sample implementation of a Redis-powered coordination system used to display product recommendations in e-mails.
 */
public class RequestCoordinator {

    private static final Logger LOG = LoggerFactory.getLogger(RequestCoordinator.class);

    private final Mutex mutex;
    private final Cache cache;

    public RequestCoordinator(final Mutex mutex, final Cache cache) {
        this.mutex = mutex;
        this.cache = cache;
    }

    public char getReco(final String params, final int position) {

        final String recos = getRecos(params, position);
        final int nrRecos = recos.length();
        if (position >= nrRecos) {
            throw new RuntimeException("Requested a position that was not computed.");
        }

        final char reco = recos.charAt(position);
        return reco;
    }

    private String getRecos(final String params, final int position) {

        final Key key = Key.from(params);

        final boolean wonLock = mutex.getLock(key.forLock());

        if (wonLock) {
            return computeRecos(key, position, params);
        } else {
            return readRecos(key, position);
        }
    }

    private String computeRecos(final Key key, final int position, final String params) {

        LOG.debug("[position={}] Won lock {}. Computing.", position, key.forLock());
        final String recos = computeRecos(params);

        LOG.debug("[position={}] Done computing. Writing recos to {}.", position, key.forData());
        cache.write(key.forData(), recos);

        LOG.debug("[position={}] Done writing. Returning {} newsletter recos.", position, recos.length());
        return recos;
    }

    private String readRecos(final Key key, final int position) {
        LOG.debug("[position={}] Didn't win lock. Reading recos from key {}.", position, key.forData());
        final String recos = cache.read(key.forData());

        LOG.debug("[position={}] Read {} recos from key {}.", position, recos.length(), key.forData());
        return recos;
    }

    private String computeRecos(final String params) {
        return ""; // Heavy lifting.
    }

}
