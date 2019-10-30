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

package com.clivenspetit.events.data.common.entity;

import com.clivenspetit.events.data.event.entity.EventEntity;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Clivens Petit
 */
@Entity
@Table(name = "location")
public class LocationEntity implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * Location id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Location external id
     */
    @UUID(message = "Location id should be a valid v4 UUID.")
    @Column(name = "location_id")
    private String locationId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false, unique = true)
    private EventEntity eventId;

    /**
     * The version of this object.
     */
    @Version
    @PositiveOrZero(message = "Version should be greater or equal to zero.")
    @Column(name = "version")
    private Integer version = 0;

    @Size(max = 120, message = "Address should have be {max} characters max.")
    @NotBlank(message = "Address is required.")
    @Column(name = "address")
    private String address;

    @Size(max = 60, message = "City should have be {max} characters max.")
    @NotBlank(message = "City is required.")
    @Column(name = "city")
    private String city;

    @Size(max = 60, message = "Country should have be {max} characters max.")
    @NotBlank(message = "Country is required.")
    @Column(name = "country")
    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @PrePersist
    public void prePersist() {
        if (locationId == null) locationId = java.util.UUID.randomUUID().toString();
    }
}
