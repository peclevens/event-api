package com.clivenspetit.events.domain.session.exception;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class DuplicateSessionNameException extends RuntimeException {

    public DuplicateSessionNameException() {
        this("Session name should be unique.");
    }

    public DuplicateSessionNameException(String message) {
        super(message);
    }
}
