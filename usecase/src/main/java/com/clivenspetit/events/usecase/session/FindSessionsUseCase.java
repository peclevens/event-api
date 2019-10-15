package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class FindSessionsUseCase {

    private final SessionRepository sessionRepository;

    public FindSessionsUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> findSessionsByEventId(@UUID(message = "Id should be a valid v4 UUID.") String eventId,
                                               String query, Sort sort) {

        return sessionRepository.getSessionsByEventId(eventId, query, sort);
    }
}
