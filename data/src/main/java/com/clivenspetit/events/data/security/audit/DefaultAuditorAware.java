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

package com.clivenspetit.events.data.security.audit;

import com.clivenspetit.events.data.user.entity.UserEntity;
import com.clivenspetit.events.domain.Context;
import com.clivenspetit.events.domain.user.exception.UserNotFoundException;
import com.clivenspetit.events.domain.user.login.exception.UnauthenticatedException;
import org.springframework.data.domain.AuditorAware;

import javax.cache.Cache;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.Optional;

/**
 * @author Clivens Petit
 */
public class DefaultAuditorAware implements AuditorAware<Long> {

    private final EntityManager entityManager;
    private final Context context;
    private final Cache<String, Long> userIdCache;

    public DefaultAuditorAware(EntityManager entityManager, Context context, Cache<String, Long> userIdCache) {
        this.entityManager = entityManager;
        this.context = context;
        this.userIdCache = userIdCache;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        // Get the user id
        String userId = context.getLoggedUser().orElseThrow(UnauthenticatedException::new).getId();

        // If user id is in cache, return it.
        if (userIdCache.containsKey(userId)) return Optional.of(userIdCache.get(userId));

        String queryString = "SELECT u FROM UserEntity u WHERE u.userId = :userId";

        FlushModeType flushMode = entityManager.getFlushMode();
        entityManager.setFlushMode(FlushModeType.COMMIT);
        TypedQuery<UserEntity> query = entityManager.createQuery(queryString, UserEntity.class);
        query.setParameter("userId", userId);

        // Find user with id
        UserEntity userEntity = Optional.of(query.getSingleResult()).orElseThrow(UserNotFoundException::new);

        // Set flush mode back
        entityManager.setFlushMode(flushMode);

        // Cache user id
        userIdCache.put(userId, userEntity.getId());

        return Optional.of(userEntity.getId());
    }
}
