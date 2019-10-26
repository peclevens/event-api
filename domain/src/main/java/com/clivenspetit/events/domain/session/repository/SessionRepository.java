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

package com.clivenspetit.events.domain.session.repository;

import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.springframework.data.domain.Sort;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Sessions CRUD operations management.
 *
 * @author Clivens Petit
 */
public interface SessionRepository {

    /**
     * Find session by id.
     *
     * @param id The session id.
     * @return The session matching the id passed.
     */
    Session getSessionById(@UUID String id);

    /**
     * Find all sessions by event id.
     *
     * @param eventId The event id.
     * @param query   A DSL friendly query string to generate SQL query filters from.
     * @param sort    The sort order of the resulting data.
     * @return A list of sessions.
     */
    List<Session> getSessionsByEventId(@UUID String eventId, String query, Sort sort);

    /**
     * Find out whether a session exists or not by id.
     *
     * @param id The session id.
     * @return Return true if exists, false otherwise.
     */
    Boolean sessionExists(@UUID String id);

    /**
     * Create new session.
     *
     * @param session Session creation object to store to a datasource.
     * @return The newly created session id.
     */
    String createSession(@NotNull @Valid CreateSession session);

    /**
     * Update existing session.
     *
     * @param id      The session id.
     * @param session A modified session object to store to a datasource.
     * @return The newly updated session.
     */
    Session updateSession(@UUID String id, @NotNull @Valid Session session);

    /**
     * Delete session by id.
     *
     * @param id The session id.
     */
    void deleteSessionById(@UUID String id);

    /**
     * Delete all sessions for a specific event.
     *
     * @param eventId The event id.
     */
    void deleteAllSessionsByEventId(@UUID String eventId);

    /**
     * Upvote session by id.
     *
     * @param id The session id.
     */
    void upVoteSession(@UUID String id);

    /**
     * Downvote session by id.
     *
     * @param id The session id.
     */
    void downVoteSession(@UUID String id);
}
