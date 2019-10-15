package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class UpdateEventUseCase {

    private final EventRepository eventRepository;

    public UpdateEventUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event updateEvent(@UUID(message = "Id should be a valid v4 UUID.") String id, @NotNull @Valid Event event) {
        Event updatedEvent = null;

        // Make sure the event exists
        Boolean found = eventRepository.eventExists(id);
        if (found != null && found) {
            updatedEvent = eventRepository.updateEvent(id, event);
        }

        return Optional.ofNullable(updatedEvent)
                .orElseThrow(EventNotFoundException::new);
    }
}
