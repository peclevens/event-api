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

import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

/**
 * @author Clivens Petit
 */
public class UpvoteSessionUseCase {

    private final SessionRepository sessionRepository;

    public UpvoteSessionUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void upVoteSession(@UUID(message = "Id should be a valid v4 UUID.") String id, @UUID String userId) {
        // Make sure the session exists
        Boolean found = sessionRepository.sessionExists(id);
        if (found == null || found == Boolean.FALSE) throw new SessionNotFoundException();

        sessionRepository.upVoteSession(id, userId);
    }
}
