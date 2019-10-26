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

package com.clivenspetit.events.data.event.mapper;

import com.clivenspetit.events.data.event.entity.EventEntity;
import com.clivenspetit.events.data.session.mapper.SessionMapper;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Clivens Petit
 */
@Mapper(uses = {SessionMapper.class})
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(source = "eventId", target = "id")
    Event from(EventEntity event);

    EventEntity from(CreateEvent event);
}
