package com.clivenspetit.events.domain.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class Location {

    /**
     * The version of this object.
     */
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
