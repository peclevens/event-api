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
public class Location implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * The version of this object.
     */
    @PositiveOrZero(message = "Version should be greater or equal to zero.")
    private Integer version = 0;

    @Size(max = 120, message = "Address should have be {max} characters max.")
    @NotBlank(message = "Address is required.")
    private String address;

    @Size(max = 60, message = "City should have be {max} characters max.")
    @NotBlank(message = "City is required.")
    private String city;

    @Size(max = 60, message = "Country should have be {max} characters max.")
    @NotBlank(message = "Country is required.")
    private String country;

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
}
