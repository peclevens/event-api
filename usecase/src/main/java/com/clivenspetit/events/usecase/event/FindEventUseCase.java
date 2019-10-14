package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class FindEventUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindEventUseCase.class);

    private final EventRepository eventRepository;

    public FindEventUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event findEventById(@UUID(message = "Id should be a valid v4 UUID.") String id) {
        logger.info("Searching event id: {}", id);

        return Optional.ofNullable(eventRepository.getEventById(id))
                .orElseThrow(() -> {
                    logger.info("Event not found id: {}", id);
                    return new EventNotFoundException();
                });
    }
}
