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
import java.util.Optional;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
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
     * @param id
     * @return
     */
    @Override
    public Event getEventById(@UUID String id) {
        logger.info("Search event with id: {}", id);

        // Cache key
        String cacheKey = String.format(EVENT_CACHE_KEY_TPL, id);

        // Find event in cache
        Event cacheEvent = eventCache.get(cacheKey);

        return Optional.ofNullable(cacheEvent)
                .map(event -> { // Return cache event if exists
                    logger.info("Event found in cache. Id: {}, ", id);
                    return event;
                })
                .orElseGet(() -> jpaEventRepository.findByEventId(id)
                        .map(eventEntity -> {
                            Event event = eventMapper.from(eventEntity);

                            // Cache event if found
                            if (event != null) {
                                logger.info("Event found in db. Id: {}", id);
                                eventCache.put(cacheKey, event);
                            }

                            return event;
                        }).orElse(null));
    }

    /**
     * Find out whether an event exists or not by id.
     *
     * @param id
     * @return
     */
    @Override
    public Boolean eventExists(@UUID String id) {
        return null;
    }

    /**
     * Find / filter all events
     *
     * @param query
     * @param pageable
     * @return
     */
    @Override
    public Page<Event> getAllEvents(String query, Pageable pageable) {
        return null;
    }

    /**
     * Create a new event.
     *
     * @param event
     * @return
     */
    @Override
    public Id createEvent(@NotNull @Valid CreateEvent event) {
        return null;
    }

    /**
     * Update existing event.
     *
     * @param id
     * @param event
     * @return
     */
    @Override
    public Event updateEvent(@UUID String id, @NotNull @Valid Event event) {
        return null;
    }

    /**
     * Delete event by id.
     *
     * @param id
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
