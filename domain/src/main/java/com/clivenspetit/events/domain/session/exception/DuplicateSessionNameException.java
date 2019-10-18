package com.clivenspetit.events.domain.session.exception;

/**
 * @author Clivens Petit
 */
public class DuplicateSessionNameException extends RuntimeException {

    public DuplicateSessionNameException() {
        this("Session name should be unique.");
    }

    public DuplicateSessionNameException(String message) {
        super(message);
    }
}
