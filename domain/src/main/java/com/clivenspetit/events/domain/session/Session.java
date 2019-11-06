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

package com.clivenspetit.events.domain.session;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.validation.constraints.IterableOfStringPattern;
import com.clivenspetit.events.domain.validation.constraints.Name;
import com.clivenspetit.events.domain.validation.constraints.UUID;
import com.clivenspetit.events.domain.validation.constraints.Word;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Session
 * <p>
 * Event session model
 *
 * @author Clivens Petit
 */
public final class Session implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * The version of this object.
     */
    @PositiveOrZero(message = "Version should be greater or equal to zero.")
    private Integer version;

    /**
     * Session id.
     */
    @UUID(message = "Id should be a valid v4 UUID.")
    private String id;

    /**
     * Session name
     */
    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 120, message = "Name should be between {min} and {max} characters.")
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
     * The name of the speaker of the session
     */
    @NotBlank(message = "Presenter name is required.")
    @Name(message = "Presenter name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 120, message = "Presenter name should be between {min} and {max} characters.")
    private String presenter;

    /**
     * List of voters.
     */
    @Valid
    @IterableOfStringPattern(regexp = "^[a-z0-9_.-]+$",
            message = "Voter user names should contain only characters from a-z, 0-9 and symbols . _")
    private Set<String> voters;

    private Session(Session.Builder builder) {
        this.version = builder.version != null ? builder.version : 0;
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.level = builder.level;
        this.duration = builder.duration;
        this.presenter = builder.presenter;
        this.voters = builder.voters != null ? builder.voters : new LinkedHashSet<>();
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

    public String getDescription() {
        return description;
    }

    public Level getLevel() {
        return level;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public String getPresenter() {
        return presenter;
    }

    public Set<String> getVoters() {
        return voters;
    }

    public static Session.Builder builder() {
        return new Session.Builder();
    }

    public static Session.Builder builder(Session session) {
        Builder builder = new Builder();
        builder.version = session.version;
        builder.id = session.id;
        builder.name = session.name;
        builder.description = session.description;
        builder.level = session.level;
        builder.duration = session.duration;
        builder.presenter = session.presenter;
        builder.voters = session.voters;

        return builder;
    }

    public static final class Builder {
        private Integer version = 0;
        private String id;
        private String name;
        private String description;
        private Level level;
        private LocalTime duration;
        private String presenter;
        private Set<String> voters = new LinkedHashSet<>();

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

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        public Builder duration(LocalTime duration) {
            this.duration = duration;
            return this;
        }

        public Builder presenter(String presenter) {
            this.presenter = presenter;
            return this;
        }

        public Builder voters(Set<String> voters) {
            this.voters = voters;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
