package com.clivenspetit.events.domain.session;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.validation.constraints.Name;
import com.clivenspetit.events.domain.validation.constraints.Word;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

/**
 * CreateSession
 * <p>
 * Event session model
 *
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class CreateSession {

    /**
     * Session name
     */
    @Size(min = 2, max = 120, message = "Name should be between {min} and {max} characters.")
    @NotBlank(message = "Name is required.")
    private String name;

    /**
     * Session description
     */
    @NotBlank(message = "Description is required.")
    @Word(min = 5, max = 500, message = "Description should be between {min} to {max} words.")
    private String description;

    /**
     * Session level
     */
    @NotNull(message = "Level is required.")
    private Level level;

    /**
     * Session duration
     */
    @NotNull(message = "Duration is required.")
    private LocalTime duration;

    /**
     * The speaker of the session
     */
    @Name(message = "Presenter name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 120, message = "Presenter name should be between {min} and {max} characters.")
    @NotBlank(message = "Presenter name is required.")
    private String presenter;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
