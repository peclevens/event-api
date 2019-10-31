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

package com.clivenspetit.events.data.event.repository;

import com.clivenspetit.events.data.event.entity.EventEntity;
import com.clivenspetit.events.data.event.mapper.EventMapper;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.Cache;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Clivens Petit
 */
public class DefaultEventRepository implements EventRepository {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEventRepository.class);
    public static final String EVENT_CACHE_KEY_TPL = "event:%s";

    private final JpaEventRepository jpaEventRepository;
    private final SessionRepository sessionRepository;
    private final Cache<String, Event> eventCache;
    private final EventMapper eventMapper;

    public DefaultEventRepository(
            JpaEventRepository jpaEventRepository, SessionRepository sessionRepository,
            Cache<String, Event> eventCache, EventMapper eventMapper) {

        this.jpaEventRepository = jpaEventRepository;
        this.sessionRepository = sessionRepository;
        this.eventCache = eventCache;
        this.eventMapper = eventMapper;
    }

    /**
     * Find event by id.
     *
     * @param id The event id.
     * @return The event matching the id passed.
     */
    @Override
    public Event getEventById(@UUID String id) {
        logger.info("Search event with id {}.", id);

        // Cache key
        String cacheKey = String.format(EVENT_CACHE_KEY_TPL, id);

        // Find event in cache
        Event cacheEvent = eventCache.get(cacheKey);
        if (cacheEvent != null) {
            logger.info("Event id {} found in cache.", id);
            return cacheEvent;
        }

        // Find event from database
        return jpaEventRepository.findByEventId(id)
                .map(eventEntity -> {
                    Event event = eventMapper.from(eventEntity);

                    // Cache event if found
                    if (event != null) {
                        eventCache.put(cacheKey, event);
                        logger.info("Event id {} found in db, cache it.", id);
                    }

                    return event;
                })
                .orElseGet(() -> {
                    logger.info("Event id {} not found.", id);
                    return null;
                });
    }

    /**
     * Find out whether an event exists or not by id.
     *
     * @param id The event id.
     * @return Return true if exists, false otherwise.
     */
    @Override
    public Boolean eventExists(@UUID String id) {
        return this.getEventById(id) != null;
    }

    /**
     * Find / filter all events
     *
     * @param query    A DSL friendly query string to generate SQL query filters from.
     * @param pageable Combination of size, page and sort information to retrieve data from a datasource.
     * @return A paginated list of events.
     */
    @Override
    public Page<Event> getAllEvents(String query, Pageable pageable) {
        return null;
    }

    /**
     * Create a new event.
     *
     * @param event Event creation object to store to a datasource.
     * @return The newly created event id.
     */
    @Override
    public String createEvent(@NotNull @Valid CreateEvent event) {
        logger.info("Create event with title: {}", event.getName());

        // Map object to entity
        EventEntity eventEntity = eventMapper.from(event);

        // Save the event
        eventEntity = jpaEventRepository.save(eventEntity);

        logger.info("Event with title: {} was created successfully with id: {}",
                event.getName(), eventEntity.getEventId());

        return eventEntity.getEventId();
    }

    /**
     * Update existing event.
     *
     * @param id    The event id.
     * @param event A modified event object to store to a datasource.
     * @return The newly updated event.
     */
    @Override
    public Event updateEvent(@UUID String id, @NotNull @Valid Event event) {
        return null;
    }

    /**
     * Delete event by id.
     *
     * @param id The event id.
     */
    @Override
    public void deleteEventById(@UUID String id) {
        logger.info("Delete event with id {}.", id);

        // Cache key
        String cacheKey = String.format(EVENT_CACHE_KEY_TPL, id);
        logger.debug("Event generated cache key {}.", cacheKey);

        // Delete all event sessions from storage
        sessionRepository.deleteAllSessionsByEventId(id);

        // Delete event from storage
        jpaEventRepository.deleteEventById(id);
        logger.info("Event with id {} was deleted successfully.", id);

        // Remove event in cache
        eventCache.remove(cacheKey);
        logger.info("Remove event with id {} from cache.", id);
    }

    /**
     * Delete all events.
     */
    @Override
    public void deleteAllEvents() {

    }
}
