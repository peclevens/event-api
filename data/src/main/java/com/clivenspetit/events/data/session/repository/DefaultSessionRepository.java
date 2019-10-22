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

package com.clivenspetit.events.data.session.repository;

import com.clivenspetit.events.data.session.mapper.SessionMapper;
import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import javax.cache.Cache;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Clivens Petit
 */
public class DefaultSessionRepository implements SessionRepository {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSessionRepository.class);
    public static final String SESSION_CACHE_KEY_TPL = "session:%s";

    private final JpaSessionRepository jpaSessionRepository;
    private final Cache<String, Session> sessionCache;
    private final SessionMapper sessionMapper;

    public DefaultSessionRepository(
            JpaSessionRepository jpaSessionRepository, Cache<String, Session> sessionCache,
            SessionMapper sessionMapper) {

        this.jpaSessionRepository = jpaSessionRepository;
        this.sessionCache = sessionCache;
        this.sessionMapper = sessionMapper;
    }

    /**
     * Find session by id.
     *
     * @param id The session id.
     * @return The session matching the id passed.
     */
    @Override
    public Session getSessionById(@UUID(message = "Session id should be a valid v4 UUID.") String id) {
        logger.info("Search session with id: {}", id);

        // Cache key
        String cacheKey = String.format(SESSION_CACHE_KEY_TPL, id);

        // Find session in cache
        Session cacheSession = sessionCache.get(cacheKey);
        if (cacheSession != null) {
            logger.info("Session found in cache. Id: {}, ", id);
            return cacheSession;
        }

        // Find session from database
        return jpaSessionRepository.findBySessionId(id)
                .map(sessionEntity -> {
                    Session session = sessionMapper.from(sessionEntity);

                    // Cache session if found
                    if (session != null) {
                        logger.info("Session found in db, cache it. Id: {}", id);
                        sessionCache.put(cacheKey, session);
                    }

                    return session;
                })
                .orElseGet(() -> {
                    logger.info("Session not found. Id: {}", id);
                    return null;
                });
    }

    /**
     * Find all sessions by event id.
     *
     * @param eventId The event id.
     * @param query   A DSL friendly query string to generate SQL query filters from.
     * @param sort    The sort order of the resulting data.
     * @return A list of sessions.
     */
    @Override
    public List<Session> getSessionsByEventId(@UUID String eventId, String query, Sort sort) {
        return null;
    }

    /**
     * Find out whether a session exists or not by id.
     *
     * @param id The session id.
     * @return Return true if exists, false otherwise.
     */
    @Override
    public Boolean sessionExists(@UUID String id) {
        return null;
    }

    /**
     * Create new session.
     *
     * @param session Session creation object to store to a datasource.
     * @return The newly created session id.
     */
    @Override
    public Id createSession(@NotNull @Valid CreateSession session) {
        return null;
    }

    /**
     * Update existing session.
     *
     * @param id      The session id.
     * @param session A modified session object to store to a datasource.
     * @return The newly updated session.
     */
    @Override
    public Session updateSession(@UUID String id, @NotNull @Valid Session session) {
        return null;
    }

    /**
     * Delete session by id.
     *
     * @param id The session id.
     */
    @Override
    public void deleteSessionById(@UUID String id) {

    }

    /**
     * Delete all sessions for a specific event.
     *
     * @param eventId The event id.
     */
    @Override
    public void deleteAllSessionsByEventId(@UUID String eventId) {

    }

    /**
     * Upvote session by id.
     *
     * @param id The session id.
     */
    @Override
    public void upVoteSession(@UUID String id) {

    }

    /**
     * Downvote session by id.
     *
     * @param id The session id.
     */
    @Override
    public void downVoteSession(@UUID String id) {

    }
}
