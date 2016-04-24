package com.vosmann.redis.mutex.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public Mutex mutex() {
        return new Mutex();
    }

    @Bean
    public Cache cache() {
        return new Cache();
    }

    @Bean
    public RequestCoordinator requestCoordinator(final Mutex mutex, final Cache cache) {
        return new RequestCoordinator(mutex, cache);
    }

}
