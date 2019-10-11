package com.clivenspetit.events.domain.common;

import com.clivenspetit.events.domain.validation.constraints.UUID;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class Id {

    /**
     * Object id
     */
    @UUID(message = "Id should be a valid v4 UUID.")
    private String id;

    public Id() {

    }

    public Id(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
