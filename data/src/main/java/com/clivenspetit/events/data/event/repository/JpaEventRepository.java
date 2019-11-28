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

package com.clivenspetit.events.data.event.repository;

import com.clivenspetit.events.data.event.entity.EventEntity;
import com.clivenspetit.events.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Clivens Petit
 */
public interface JpaEventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> findByEventIdAndActiveIsTrue(String id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM EventEntity e WHERE e.eventId = :eventId")
    void deleteEventById(@Param("eventId") String eventId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM EventEntity e")
    void deleteAllEvents();
}
