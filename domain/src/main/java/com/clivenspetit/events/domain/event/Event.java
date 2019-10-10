package com.clivenspetit.events.domain.event;

import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.validation.constraints.AnyOf;
import com.clivenspetit.events.domain.validation.constraints.URL;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Event
 * <p>
 * Event item
 *
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@AnyOf(message = "Location or online url is required. Both are allowed too.")
public class Event {

    /**
     * The version of this object.
     */
    private Integer version = 0;

    /**
     * Event id
     */
    @UUID(message = "Id should be a valid v4 UUID.")
    private String id;

    /**
     * Event name
     */
    @Size(min = 2, max = 120, message = "Name should be between {min} and {max} characters.")
    @NotBlank(message = "Name is required.")
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
    @DecimalMax(value = "999.99", message = "Price should be less than equals to {value}.")
    private Double price = 0.0D;

    /**
     * Event URL picture
     */
    @NotBlank(message = "Image Url is required.")
    @URL(message = "Invalid URL")
    private String imageUrl;

    /**
     * Event Online URL
     */
    @NotBlank(message = "Online Url is required.")
    @URL(message = "Invalid URL")
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
    private Set<Session> sessions = new HashSet<>();

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }
}
