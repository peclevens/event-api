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

package com.clivenspetit.events.domain.event.repository;

import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.UpdateEvent;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Event CRUD operations management.
 *
 * @author Clivens Petit
 */
public interface EventRepository {

    /**
     * Find event by id.
     *
     * @param id The event id.
     * @return The event matching the id passed.
     */
    Event getEventById(@UUID String id);

    /**
     * Find out whether an event exists or not by id.
     *
     * @param id The event id.
     * @return Return true if exists, false otherwise.
     */
    Boolean eventExists(@UUID String id);

    /**
     * Find / filter all events
     *
     * @param query    A DSL friendly query string to generate SQL query filters from.
     * @param pageable Combination of size, page and sort information to retrieve data from a datasource.
     * @return A paginated list of events.
     */
    Page<Event> getAllEvents(String query, Pageable pageable);

    /**
     * Create a new event.
     *
     * @param event Event creation object to store to a datasource.
     * @return The newly created event id.
     */
    String createEvent(@NotNull @Valid CreateEvent event);

    /**
     * Update existing event.
     *
     * @param id    The event id.
     * @param event A modified event object to store to a datasource.
     * @return The newly updated event.
     */
    Event updateEvent(@UUID String id, @NotNull @Valid UpdateEvent event);

    /**
     * Delete event by id.
     *
     * @param id The event id.
     */
    void deleteEventById(@UUID String id);

    /**
     * Delete all events.
     */
    void deleteAllEvents();
}
