package com.clivenspetit.events.domain.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public enum Level {

    BEGINNER("BEGINNER"),
    INTERMEDIATE("INTERMEDIATE"),
    ADVANCED("ADVANCED");

    private final String value;
    private final static Map<String, Level> CONSTANTS = new HashMap<>();

    static {
        for (Level l : values()) {
            CONSTANTS.put(l.value, l);
        }
    }

    private Level(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static Level fromValue(String value) {
        Level constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
