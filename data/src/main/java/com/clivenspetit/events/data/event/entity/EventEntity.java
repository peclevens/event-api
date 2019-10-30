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

package com.clivenspetit.events.data.event.entity;

import com.clivenspetit.events.data.common.entity.LocationEntity;
import com.clivenspetit.events.data.session.entity.SessionEntity;
import com.clivenspetit.events.domain.validation.constraints.AnyOf;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import com.clivenspetit.events.domain.validation.constraints.Url;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Clivens Petit
 */
@AnyOf(fields = {"onlineUrl", "location"}, message = "Location or online url is required. Both are allowed too.")
@Entity
@Table(name = "event")
public class EventEntity implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * Event id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Event external id
     */
    @UUID(message = "Event id should be a valid v4 UUID.")
    @Column(name = "event_id")
    private String eventId;

    /**
     * The version of this object.
     */
    @Version
    @PositiveOrZero(message = "Version should be greater or equal to zero.")
    @Column(name = "version")
    private Integer version = 0;

    /**
     * Event name
     */
    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 120, message = "Name should be between {min} and {max} characters.")
    @Column(name = "name", unique = true)
    private String name;

    /**
     * Event start datetime
     */
    @NotNull(message = "Start date is required.")
    @Future(message = "Start date should be in the future.")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    /**
     * Event price
     */
    @PositiveOrZero(message = "Price should be greater or equal to zero.")
    @DecimalMax(value = "999.99", message = "Price should be less than equals to {value}.")
    @Column(name = "price")
    private BigDecimal price;

    /**
     * Event URL picture
     */
    @NotBlank(message = "Image Url is required.")
    @Url(message = "Url should be valid and start with http or https.")
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Event Online URL
     */
    @Url(message = "Url should be valid and start with http or https.")
    @Column(name = "online_url")
    private String onlineUrl;

    /**
     * Location
     */
    @Valid
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, mappedBy = "eventId")
    private LocationEntity location;

    /**
     * Event sessions
     */
    @Valid
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "eventId")
    @Column(name = "event_id")
    private Set<SessionEntity> sessions;

    /**
     * Event created datetime
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Event last updated datetime
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Event deleted datetime
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Event active status
     */
    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public void setOnlineUrl(String onlineUrl) {
        this.onlineUrl = onlineUrl;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        if (location != null) location.setEventId(this);

        this.location = location;
    }

    public Set<SessionEntity> getSessions() {
        return sessions;
    }

    public void setSessions(Set<SessionEntity> sessions) {
        if (sessions != null && !sessions.isEmpty()) {
            sessions.forEach(session -> session.setEventId(this));
        }

        this.sessions = sessions;
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
        eventId = java.util.UUID.randomUUID().toString();
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
