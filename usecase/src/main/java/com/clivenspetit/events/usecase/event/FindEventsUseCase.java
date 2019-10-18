package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Clivens Petit
 */
public class FindEventsUseCase {

    private final EventRepository eventRepository;

    public FindEventsUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    Page<Event> findAllEvents(String query, Pageable pageable) {
        return eventRepository.getAllEvents(query, pageable);
    }
}
