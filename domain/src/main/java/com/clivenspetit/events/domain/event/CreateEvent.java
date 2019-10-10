package com.clivenspetit.events.domain.event;

import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.validation.constraints.RequireEventLocationOrOnlineUrl;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.validation.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * CreateEvent
 * <p>
 * CreateEvent item
 *
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@RequireEventLocationOrOnlineUrl
public class CreateEvent {

    /**
     * Event name
     */
    @Size(min = 2, max = 120, message = "Name should be between {min} and {max} characters.")
    @NotBlank(message = "Name is required.")
    private String name;

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
     * Event price
     */
    @DecimalMax(value = "999.99", message = "Price should be less than equals to {value}.")
    private Double price = 0.0D;

    /**
     * Event start datetime
     */
    @NotNull(message = "Start date is required.")
    @Future(message = "Start date should be in the future.")
    private LocalDateTime startDate;

    /**
     * Event sessions
     */
    @Valid
    private Set<CreateSession> sessions = new HashSet<>();

    public Set<CreateSession> getSessions() {
        return sessions;
    }

    public void setSessions(Set<CreateSession> sessions) {
        this.sessions = sessions;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
