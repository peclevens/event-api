package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author Clivens Petit
 */
public class UpdateSessionUseCase {

    private final SessionRepository sessionRepository;

    public UpdateSessionUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session updateSession(@UUID(message = "Id should be a valid v4 UUID.") String id,
                                 @NotNull @Valid Session session) {

        Session updatedSession = null;

        // Make sure the session exists
        Boolean found = sessionRepository.sessionExists(id);
        if (found != null && found) {
            updatedSession = sessionRepository.updateSession(id, session);
        }

        return Optional.ofNullable(updatedSession)
                .orElseThrow(SessionNotFoundException::new);
    }
}
