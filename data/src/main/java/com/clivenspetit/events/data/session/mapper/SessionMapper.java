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
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.UpdateSession;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * @author Clivens Petit
 */
@Mapper
@DecoratedWith(SessionMapperDecorator.class)
public interface SessionMapper {

    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    @Mappings({
            @Mapping(source = "sessionId", target = "id"),
            @Mapping(target = "voters", ignore = true)
    })
    Session from(SessionEntity session);

    SessionEntity from(CreateSession session);

    SessionEntity merge(UpdateSession session, @MappingTarget SessionEntity oldSession);
}
