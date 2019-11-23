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

import com.clivenspetit.events.data.event.entity.EventEntity;
import com.clivenspetit.events.data.event.repository.JpaEventRepository;
import com.clivenspetit.events.data.session.entity.SessionEntity;
import com.clivenspetit.events.data.session.mapper.SessionMapper;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.UpdateSession;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import javax.cache.Cache;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Clivens Petit
 */
public class DefaultSessionRepository implements SessionRepository {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSessionRepository.class);
    public static final String SESSION_BASE_CACHE_KEY_TPL = "event:%s:*";
    public static final String SESSION_CACHE_KEY_TPL = "event:%s:session:%s"; // TODO Refactor session cache keys

    private final JpaSessionRepository jpaSessionRepository;
    private final JpaEventRepository jpaEventRepository;
    private final Cache<String, Session> sessionCache;
    private final SessionMapper sessionMapper;

    public DefaultSessionRepository(
            JpaSessionRepository jpaSessionRepository, JpaEventRepository jpaEventRepository,
            Cache<String, Session> sessionCache, SessionMapper sessionMapper) {

        this.jpaSessionRepository = jpaSessionRepository;
        this.jpaEventRepository = jpaEventRepository;
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
    public Session getSessionById(@UUID String id) {
        logger.info("Search session with id {}.", id);

        // Cache key
        String cacheKey = String.format(SESSION_CACHE_KEY_TPL, "*", id);

        // Find session in cache
        Session cacheSession = sessionCache.get(cacheKey);
        if (cacheSession != null) {
            logger.info("Session id {} found in cache.", id);
            return cacheSession;
        }

        // Find session from database
        return jpaSessionRepository.findBySessionId(id)
                .map(sessionEntity -> {
                    Session session = sessionMapper.from(sessionEntity);

                    // Cache session if found
                    if (session != null) {
                        logger.info("Session id {} found in db, cache it.", id);
                        sessionCache.put(cacheKey, session);
                    }

                    return session;
                })
                .orElseGet(() -> {
                    logger.info("Session id {} not found.", id);
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
        return this.getSessionById(id) != null;
    }

    /**
     * Create new session.
     *
     * @param eventId The event id.
     * @param session Session creation object to store to a datasource.
     * @return The newly created session id.
     */
    @Override
    public String createSession(@UUID String eventId, @NotNull @Valid CreateSession session) {
        logger.info("Create session with title: {}", session.getName());

        // Parent event
        EventEntity eventEntity = jpaEventRepository.findByEventId(eventId)
                .orElseThrow(EventNotFoundException::new);
        eventEntity.setEventId(eventId);

        // Map object to entity
        SessionEntity sessionEntity = sessionMapper.from(session);
        sessionEntity.setEventId(eventEntity);

        // Save the session
        sessionEntity = jpaSessionRepository.save(sessionEntity);

        logger.info("Session with title: {} was created successfully with id: {}",
                session.getName(), sessionEntity.getSessionId());

        return sessionEntity.getSessionId();
    }

    /**
     * Update existing session.
     *
     * @param id      The session id.
     * @param session A modified session object to store to a datasource.
     * @return The newly updated session.
     */
    @Override
    public Session updateSession(@UUID String id, @NotNull @Valid UpdateSession session) {
        logger.info("Update session with id: {}.", id);

        // Cache key
        String cacheKey = String.format(SESSION_CACHE_KEY_TPL, "*", id);
        logger.debug("Session generated cache key {}.", cacheKey);

        // Find old session
        SessionEntity oldSession = jpaSessionRepository.findBySessionId(id).orElse(null);

        // Merge sessions
        SessionEntity mergeSession = sessionMapper.merge(session, oldSession);

        // Update the session
        SessionEntity sessionEntity = jpaSessionRepository.save(mergeSession);

        Session updatedSession = sessionMapper.from(sessionEntity);
        logger.info("Session with id: {} was updated successfully. The new title is {}.",
                updatedSession.getId(), updatedSession.getName());

        // Remove session in cache
        sessionCache.remove(cacheKey);
        logger.debug("Remove session with id {} from cache.", id);

        return updatedSession;
    }

    /**
     * Delete session by id.
     *
     * @param id The session id.
     */
    @Override
    public void deleteSessionById(@UUID String id) {
        logger.info("Delete session with id {}.", id);

        // Cache key
        String cacheKey = String.format(SESSION_CACHE_KEY_TPL, "*", id);
        logger.debug("Session generated cache key {}.", cacheKey);

        // Delete session from storage
        jpaSessionRepository.deleteSessionById(id);
        logger.info("Session with id {} was deleted successfully.", id);

        // Remove session in cache
        sessionCache.remove(cacheKey);
        logger.debug("Remove session with id {} from cache.", id);
    }

    /**
     * Delete all sessions for a specific event.
     *
     * @param eventId The event id.
     */
    @Override
    public void deleteAllSessionsByEventId(@UUID String eventId) {
        logger.info("Delete all sessions for event with id {}.", eventId);

        // Cache key
        String cacheKey = String.format(SESSION_BASE_CACHE_KEY_TPL, eventId);
        logger.debug("Session generated pattern cache key: {}.", cacheKey);

        jpaSessionRepository.deleteAllSessionsByEventId(eventId);
        logger.info("All sessions with event id {} were deleted successfully.", eventId);

        // Remove all sessions matching this event id from cache
        sessionCache.remove(cacheKey);
        logger.info("Remove all sessions matching the event id {} from cache.", eventId);
    }

    /**
     * Delete all sessions providing a list of event ids.
     *
     * @param eventIds List of event id.
     */
    @Override
    public void deleteAllSessionsByEventIds(@NotNull Set<@UUID String> eventIds) {
        String eventIdsStr = Arrays.toString(eventIds.toArray());

        logger.info("Delete all sessions for event with ids {}.", eventIdsStr);

        // Cache key
        String cacheKey = String.format(SESSION_BASE_CACHE_KEY_TPL, eventIdsStr); // TODO Manage ids in cache
        logger.debug("Sessions generated pattern cache key: {}.", cacheKey);

        jpaSessionRepository.deleteAllSessionsByEventIds(eventIds);
        logger.info("All sessions with event ids {} were deleted successfully.", eventIdsStr);

        // Remove all sessions matching the event ids from cache
        sessionCache.remove(eventIdsStr); // TODO Manage ids in cache
        logger.info("Remove all sessions matching the event ids {} from cache.", eventIdsStr);
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
