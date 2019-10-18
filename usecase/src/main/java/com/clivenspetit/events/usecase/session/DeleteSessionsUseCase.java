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
