package com.clivenspetit.events.data.event.repository;

import com.clivenspetit.events.data.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@Repository
public interface JpaEventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> findByEventId(String id);
}
