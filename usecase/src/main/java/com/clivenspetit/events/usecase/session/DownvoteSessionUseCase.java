package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class DownvoteSessionUseCase {

    private final SessionRepository sessionRepository;

    public DownvoteSessionUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void downVoteSession(@UUID(message = "Id should be a valid v4 UUID.") String id) {
        // Make sure the session exists
        Boolean found = sessionRepository.sessionExists(id);
        if (found == null || found == Boolean.FALSE) throw new SessionNotFoundException();

        sessionRepository.downVoteSession(id);
    }
}
