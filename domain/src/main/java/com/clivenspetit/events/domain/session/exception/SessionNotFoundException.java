package com.clivenspetit.events.domain.session.exception;

/**
 * @author Clivens Petit
 */
public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException() {
        this("Session not found.");
    }

    public SessionNotFoundException(String message) {
        super(message);
    }
}
