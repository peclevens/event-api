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

package com.clivenspetit.events.domain.event;

import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.validation.constraints.AnyOf;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import com.clivenspetit.events.domain.validation.constraints.Url;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Event
 * <p>
 * Event item
 *
 * @author Clivens Petit
 */
@AnyOf(fields = {"onlineUrl", "location"}, message = "Location or online url is required. Both are allowed too.")
public final class Event implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * The version of this object.
     */
    @PositiveOrZero(message = "Version should be greater or equal to zero.")
    private Integer version;

    /**
     * Event id
     */
    @UUID(message = "Id should be a valid v4 UUID.")
    private String id;

    /**
     * Event name
     */
    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 120, message = "Name should be between {min} and {max} characters.")
    private String name;

    /**
     * Event start datetime
     */
    @NotNull(message = "Start date is required.")
    @Future(message = "Start date should be in the future.")
    private LocalDateTime startDate;

    /**
     * Event price
     */
    @PositiveOrZero(message = "Price should be greater or equal to zero.")
    @DecimalMax(value = "999.99", message = "Price should be less than equals to {value}.")
    private BigDecimal price;

    /**
     * Event URL picture
     */
    @NotBlank(message = "Image Url is required.")
    @Url(message = "Url should be valid and start with http or https.")
    private String imageUrl;

    /**
     * Event Online URL
     */
    @Url(message = "Url should be valid and start with http or https.")
    private String onlineUrl;

    /**
     * Location
     */
    @Valid
    private Location location;

    /**
     * Event sessions
     */
    @Valid
    private Set<Session> sessions;

    private Event(Event.Builder builder) {
        this.version = builder.version != null ? builder.version : 0;
        this.id = builder.id;
        this.name = builder.name;
        this.startDate = builder.startDate;
        this.price = builder.price != null ? builder.price : BigDecimal.ZERO;
        this.imageUrl = builder.imageUrl;
        this.onlineUrl = builder.onlineUrl;
        this.location = builder.location;
        this.sessions = builder.sessions != null ? builder.sessions : new LinkedHashSet<>();
    }

    public static Event.Builder builder() {
        return new Event.Builder();
    }

    public static Event.Builder builder(Event event) {
        Builder builder = new Builder();
        builder.version = event.version;
        builder.id = event.id;
        builder.name = event.name;
        builder.startDate = event.startDate;
        builder.price = event.price;
        builder.imageUrl = event.imageUrl;
        builder.onlineUrl = event.onlineUrl;
        builder.location = Location.builder(event.location).build();
        builder.sessions = new LinkedHashSet<>();

        if (event.sessions != null && !event.sessions.isEmpty())
            event.sessions.forEach(session -> builder.sessions.add(Session.builder(session).build()));

        return builder;
    }

    public Integer getVersion() {
        return version;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public Location getLocation() {
        return location;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public static final class Builder {
        private Integer version = 0;
        private String id;
        private String name;
        private LocalDateTime startDate;
        private BigDecimal price = BigDecimal.ZERO;
        private String imageUrl;
        private String onlineUrl;
        private Location location;
        private Set<Session> sessions;

        private Builder() {

        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder onlineUrl(String onlineUrl) {
            this.onlineUrl = onlineUrl;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder sessions(Set<Session> sessions) {
            this.sessions = sessions;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
