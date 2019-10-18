package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.repository.EventRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Clivens Petit
 */
public class CreateEventUseCase {

    private final EventRepository eventRepository;

    public CreateEventUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Id createEvent(@NotNull @Valid CreateEvent event) {
        return eventRepository.createEvent(event);
    }
}
