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

package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import java.util.Optional;

/**
 * @author Clivens Petit
 */
public class FindEventUseCase {

    private final EventRepository eventRepository;

    public FindEventUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event findEventById(@UUID(message = "Id should be a valid v4 UUID.") String id) {
        return Optional.ofNullable(eventRepository.getEventById(id))
                .orElseThrow(EventNotFoundException::new);
    }
}
