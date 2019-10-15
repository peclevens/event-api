package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class DeleteEventUseCase {

    private final EventRepository eventRepository;

    public DeleteEventUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void deleteEventById(@UUID(message = "Id should be a valid v4 UUID.") String id) {
        // Make sure the event exists
        Boolean found = eventRepository.eventExists(id);
        if (found == null || found == Boolean.FALSE) throw new EventNotFoundException();

        eventRepository.deleteEventById(id);
    }
}
