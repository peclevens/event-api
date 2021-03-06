/*
 * Copyright 2019 MAGIC SOFTWARE BAY, SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clivenspetit.events.data.config;

import com.clivenspetit.events.data.StubContext;
import com.clivenspetit.events.data.security.audit.DefaultAuditorAware;
import com.clivenspetit.events.domain.Context;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.persistence.EntityManager;

/**
 * @author Clivens Petit
 */
@TestConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class TestJpaAuditingConfiguration {

    private static final MutableConfiguration<String, Long> userIdsConfig = new MutableConfiguration<>();
    private static final CachingProvider cachingProvider = Caching.getCachingProvider();

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AuditorAware<Long> auditorProvider(EntityManager entityManager, Context context) {
        String cacheName = "userIds";
        CacheManager cacheManager = cachingProvider.getCacheManager();
        Cache<String, Long> userIdCache = cacheManager.getCache(cacheName);
        if (userIdCache == null) {
            userIdCache = cacheManager.createCache(cacheName, userIdsConfig);
        }

        return new DefaultAuditorAware(entityManager, context, userIdCache);
    }

    @Bean
    public Context context() {
        return new StubContext();
    }
}
