package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.repository.SessionRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class CreateSessionUseCase {

    private final SessionRepository sessionRepository;

    public CreateSessionUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Id createSession(@NotNull @Valid CreateSession session) {
        return sessionRepository.createSession(session);
    }
}
