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

package com.clivenspetit.events.data.session.entity;

import com.clivenspetit.events.data.event.entity.EventEntity;
import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.validation.constraints.IterableOfStringPattern;
import com.clivenspetit.events.domain.validation.constraints.Name;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import com.clivenspetit.events.domain.validation.constraints.Word;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

/**
 * @author Clivens Petit
 */
@Entity
@Table(name = "session")
@TypeDefs({@TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
public class SessionEntity implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * Session id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Session external id
     */
    @UUID(message = "Session id should be a valid v4 UUID.")
    @Column(name = "session_id")
    private String sessionId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity eventId;

    /**
     * The version of this object.
     */
    @Version
    @PositiveOrZero(message = "Version should be greater or equal to zero.")
    @Column(name = "version")
    private Integer version = 0;

    /**
     * Session name
     */
    @Size(min = 2, max = 120, message = "Name should be between {min} and {max} characters.")
    @NotBlank(message = "Name is required.")
    @Column(name = "name")
    private String name;

    /**
     * Session description
     */
    @NotBlank(message = "Description is required.")
    @Word(min = 5, max = 500, message = "Description should be between {min} to {max} words.")
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * Session level
     */
    @NotNull(message = "Level is required.")
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    /**
     * Session duration
     */
    @NotNull(message = "Duration is required.")
    @Column(name = "duration")
    private LocalTime duration;

    /**
     * The name of the speaker of the session
     */
    @Name(message = "Presenter name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 120, message = "Presenter name should be between {min} and {max} characters.")
    @NotBlank(message = "Presenter name is required.")
    @Column(name = "presenter")
    private String presenter;

    /**
     * List of voters.
     */
    @Valid
    @IterableOfStringPattern(regexp = "^[a-z0-9_.-]+$",
            message = "Voter user names should contain only characters from a-z, 0-9 and symbols . _")
    @Type(type = "json")
    @Column(name = "voters", columnDefinition = "json")
    private Set<String> voters;

    /**
     * Session created datetime
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Session last updated datetime
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Session deleted datetime
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Session active status
     */
    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public EventEntity getEventId() {
        return eventId;
    }

    public void setEventId(EventEntity eventId) {
        this.eventId = eventId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public Set<String> getVoters() {
        return voters;
    }

    public void setVoters(Set<String> voters) {
        this.voters = voters;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Boolean isActive() {
        return active != null ? active : Boolean.TRUE;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @PrePersist
    public void prePersist() {
        sessionId = java.util.UUID.randomUUID().toString();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
