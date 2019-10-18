package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.repository.EventRepository;

/**
 * @author Clivens Petit
 */
public class DeleteEventsUseCase {

    private final EventRepository eventRepository;

    public DeleteEventsUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void deleteAllEvents() {
        eventRepository.deleteAllEvents();
    }
}
