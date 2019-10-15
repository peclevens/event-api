package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import java.util.Optional;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
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
