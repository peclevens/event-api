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

import com.clivenspetit.events.data.event.mapper.EventMapper;
import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.repository.EventRepository;
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
    private final Cache<String, Event> eventCache;
    private final EventMapper eventMapper;

    public DefaultEventRepository(
            JpaEventRepository jpaEventRepository, Cache<String, Event> eventCache, EventMapper eventMapper) {

        this.jpaEventRepository = jpaEventRepository;
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
    public Event getEventById(@UUID(message = "Session id should be a valid v4 UUID.") String id) {
        logger.info("Search event with id: {}", id);

        // Cache key
        String cacheKey = String.format(EVENT_CACHE_KEY_TPL, id);

        // Find event in cache
        Event cacheEvent = eventCache.get(cacheKey);
        if (cacheEvent != null) {
            logger.info("Event found in cache. Id: {}", id);
            return cacheEvent;
        }

        // Find event from database
        return jpaEventRepository.findByEventId(id)
                .map(eventEntity -> {
                    Event event = eventMapper.from(eventEntity);

                    // Cache event if found
                    if (event != null) {
                        logger.info("Event found in db, cache it. Id: {}", id);
                        eventCache.put(cacheKey, event);
                    }

                    return event;
                })
                .orElseGet(() -> {
                    logger.info("Event not found. Id: {}", id);
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
    public Boolean eventExists(@UUID(message = "Session id should be a valid v4 UUID.") String id) {
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
    public Id createEvent(@NotNull @Valid CreateEvent event) {
        return null;
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

    }

    /**
     * Delete all events.
     */
    @Override
    public void deleteAllEvents() {

    }
}
