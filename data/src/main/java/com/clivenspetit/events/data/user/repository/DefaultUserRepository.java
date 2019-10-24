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

package com.clivenspetit.events.data.user.repository;

import com.clivenspetit.events.data.user.mapper.UserMapper;
import com.clivenspetit.events.domain.user.UpdateUser;
import com.clivenspetit.events.domain.user.User;
import com.clivenspetit.events.domain.user.repository.UserRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author Clivens Petit
 */
public class DefaultUserRepository implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUserRepository.class);
    public static final String USER_CACHE_KEY_TPL = "user:%s";

    private final JpaUserRepository jpaUserRepository;
    private final Cache<String, User> userCache;
    private final UserMapper userMapper;

    public DefaultUserRepository(
            JpaUserRepository jpaUserRepository, Cache<String, User> userCache, UserMapper userMapper) {

        this.jpaUserRepository = jpaUserRepository;
        this.userCache = userCache;
        this.userMapper = userMapper;
    }

    /**
     * Find user by id.
     *
     * @param id The user id.
     * @return The user matching the id passed.
     */
    @Override
    public User getUserById(@UUID(message = "User id should be a valid v4 UUID.") String id) {
        logger.info("Search user with id: {}", id);

        // Cache key
        String cacheKey = String.format(USER_CACHE_KEY_TPL, id);

        // Find user in cache
        User cacheUser = userCache.get(cacheKey);
        if (cacheUser != null) {
            logger.info("User found in cache. Id: {}, ", id);
            return cacheUser;
        }

        // Find user from database
        return jpaUserRepository.findByUserId(id)
                .map(userEntity -> {
                    User user = userMapper.from(userEntity);

                    // Cache user if found
                    if (user != null) {
                        logger.info("User found in db, cache it. Id: {}", id);
                        userCache.put(cacheKey, user);
                    }

                    return user;
                })
                .orElseGet(() -> {
                    logger.info("User not found. Id: {}", id);
                    return null;
                });
    }

    /**
     * Find user by email.
     *
     * @param email The user email.
     * @return The user matching the email passed.
     */
    @Override
    public User getUserEmail(@Email String email) {
        return null;
    }

    /**
     * Find out whether a user exists or not by id.
     *
     * @param id The user id.
     * @return Return true if exists, false otherwise.
     */
    @Override
    public Boolean userExists(@UUID(message = "User id should be a valid v4 UUID.") String id) {
        return null;
    }

    /**
     * Update existing user.
     *
     * @param id   The user id.
     * @param user A modified user object to store to a datasource.
     * @return The newly updated user.
     */
    @Override
    public User updateUser(@UUID String id, @NotNull @Valid UpdateUser user) {
        return null;
    }
}
