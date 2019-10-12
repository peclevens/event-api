package com.clivenspetit.events.domain.session;

import com.clivenspetit.events.domain.validation.constraints.IterableOfStringPattern;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Session
 * <p>
 * Event session model
 *
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class Session extends CreateSession {

    /**
     * The version of this object.
     */
    @PositiveOrZero(message = "Version should be greater or equal than zero.")
    private Integer version = 0;

    /**
     * Session id.
     */
    @UUID(message = "Id should be a valid v4 UUID.")
    private String id;

    /**
     * List of voters.
     */
    @Valid
    @IterableOfStringPattern(regexp = "^[a-z0-9_.-]+$",
            message = "Voter user names should contain only characters from a-z, 0-9 and symbols . _")
    private Set<String> voters = new LinkedHashSet<>();

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

    public Set<String> getVoters() {
        return voters;
    }

    public void setVoters(Set<String> voters) {
        this.voters = voters;
    }
}
