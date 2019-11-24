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

package com.clivenspetit.events.data.session.mapper;

import com.clivenspetit.events.data.session.entity.SessionEntity;
import com.clivenspetit.events.domain.session.Session;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Clivens Petit
 */
public abstract class SessionMapperDecorator implements SessionMapper {

    private final SessionMapper mapper;

    public SessionMapperDecorator(SessionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Session from(SessionEntity session) {
        Session sessionResponse = mapper.from(session);
        Session.Builder builder = Session.builder(sessionResponse);

        Set<String> voters = new HashSet<>();
        if (session.getVoters() != null && !session.getVoters().isEmpty()) {
            voters = session.getVoters().stream()
                    .map(sessionVote -> String.format("%s %s", sessionVote.getId().getUserId().getFirstName(),
                            sessionVote.getId().getUserId().getLastName()))
                    .collect(Collectors.toSet());
        }

        return builder
                .voters(voters)
                .build();
    }
}
