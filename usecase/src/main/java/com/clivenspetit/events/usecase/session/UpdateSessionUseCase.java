/*
 * Copyright 2019 MAGIC SOFTWARE BAY, SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
