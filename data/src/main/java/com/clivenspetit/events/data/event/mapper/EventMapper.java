package com.clivenspetit.events.data.event.mapper;

import com.clivenspetit.events.data.event.entity.EventEntity;
import com.clivenspetit.events.domain.event.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(source = "eventId", target = "id")
    Event from(EventEntity event);
}
