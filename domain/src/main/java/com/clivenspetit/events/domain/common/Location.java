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

package com.clivenspetit.events.domain.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Clivens Petit
 */
public final class Location implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * The version of this object.
     */
    @PositiveOrZero(message = "Version should be greater or equal to zero.")
    private Integer version = 0;

    @NotBlank(message = "Address is required.")
    @Size(max = 120, message = "Address should have be {max} characters max.")
    private String address;

    @NotBlank(message = "City is required.")
    @Size(max = 60, message = "City should have be {max} characters max.")
    private String city;

    @NotBlank(message = "Country is required.")
    @Size(max = 60, message = "Country should have be {max} characters max.")
    private String country;

    private Location(Location.Builder builder) {
        this.version = builder.version;
        this.address = builder.address;
        this.city = builder.city;
        this.country = builder.country;
    }

    public Integer getVersion() {
        return version;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public static Location.Builder builder() {
        return new Location.Builder();
    }

    public static Location.Builder builder(Location location) {
        Builder builder = new Builder();
        builder.version = location.version;
        builder.address = location.address;
        builder.city = location.city;
        builder.country = location.country;

        return builder;
    }

    public static final class Builder {
        private Integer version = 0;
        private String address;
        private String city;
        private String country;

        private Builder() {

        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
