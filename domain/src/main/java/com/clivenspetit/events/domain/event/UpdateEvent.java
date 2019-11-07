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
import com.clivenspetit.events.domain.validation.constraints.AnyOf;
import com.clivenspetit.events.domain.validation.constraints.Url;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CreateEvent
 * <p>
 * CreateEvent item
 *
 * @author Clivens Petit
 */
@AnyOf(fields = {"onlineUrl", "location"}, message = "Location or online url is required. Both are allowed too.")
public final class UpdateEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * The version of this object.
     */
    @PositiveOrZero(message = "Version should be greater or equal to zero.")
    private Integer version;

    /**
     * Event name
     */
    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 120, message = "Name should be between {min} and {max} characters.")
    private String name;

    /**
     * Event URL picture
     */
    @NotBlank(message = "Image Url is required.")
    @Url(message = "Url should be valid and start with http or https.")
    private String imageUrl;

    /**
     * Event Online URL
     */
    @NotBlank(message = "Online Url is required.")
    @Url(message = "Url should be valid and start with http or https.")
    private String onlineUrl;

    /**
     * Location
     */
    @Valid
    private Location location;

    /**
     * Event price
     */
    @PositiveOrZero(message = "Price should be greater or equal to zero.")
    @DecimalMax(value = "999.99", message = "Price should be less than equals to {value}.")
    private Double price;

    /**
     * Event start datetime
     */
    @NotNull(message = "Start date is required.")
    @Future(message = "Start date should be in the future.")
    private LocalDateTime startDate;

    private UpdateEvent(UpdateEvent.Builder builder) {
        this.version = builder.version;
        this.name = builder.name;
        this.imageUrl = builder.imageUrl;
        this.onlineUrl = builder.onlineUrl;
        this.location = builder.location;
        this.price = builder.price != null ? builder.price : 0.0D;
        this.startDate = builder.startDate;
    }

    public Integer getVersion() {
        return version;
    }

    public String getName() {
        return name;
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

    public Double getPrice() {
        return price;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public static UpdateEvent.Builder builder() {
        return new UpdateEvent.Builder();
    }

    public static final class Builder {
        private Integer version;
        private String name;
        private String imageUrl;
        private String onlineUrl;
        private Location location;
        private Double price = 0.0D;
        private LocalDateTime startDate;

        private Builder() {

        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
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

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public UpdateEvent build() {
            return new UpdateEvent(this);
        }
    }
}
