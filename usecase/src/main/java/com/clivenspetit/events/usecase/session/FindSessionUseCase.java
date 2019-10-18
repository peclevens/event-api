package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import java.util.Optional;

/**
 * @author Clivens Petit
 */
public class FindSessionUseCase {

    private final SessionRepository sessionRepository;

    public FindSessionUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session findSessionById(@UUID(message = "Id should be a valid v4 UUID.") String id) {
        return Optional.ofNullable(sessionRepository.getSessionById(id))
                .orElseThrow(SessionNotFoundException::new);
    }
}
