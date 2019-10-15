package com.clivenspetit.events.domain.event.repository;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Event CRUD operations management.
 *
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public interface EventRepository {

    /**
     * Find event by id.
     *
     * @param id
     * @return
     */
    Event getEventById(@UUID String id);

    /**
     * Find out whether an event exists or not by id.
     *
     * @param id
     * @return
     */
    Boolean eventExists(@UUID String id);

    /**
     * Find / filter all events
     *
     * @param query
     * @param pageable
     * @return
     */
    Page<Event> getAllEvents(String query, Pageable pageable);

    /**
     * Create a new event.
     *
     * @param event
     * @return
     */
    Id createEvent(@NotNull @Valid CreateEvent event);

    /**
     * Update existing event.
     *
     * @param id
     * @param event
     * @return
     */
    Event updateEvent(@UUID String id, @NotNull @Valid Event event);

    /**
     * Delete event by id.
     *
     * @param id
     */
    void deleteEventById(@UUID String id);

    /**
     * Delete all events.
     */
    void deleteAllEvents();
}
