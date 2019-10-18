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

package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

/**
 * @author Clivens Petit
 */
public class DeleteSessionsUseCase {

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;

    public DeleteSessionsUseCase(SessionRepository sessionRepository, EventRepository eventRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
    }

    public void deleteAllSessionsByEventId(@UUID(message = "Id should be a valid v4 UUID.") String eventId) {
        // Make sure the event exists
        Boolean found = eventRepository.eventExists(eventId);
        if (found == null || found == Boolean.FALSE) throw new EventNotFoundException();

        sessionRepository.deleteAllSessionsByEventId(eventId);
    }
}
