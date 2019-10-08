package com.clivenspetit.events.domain.event.exception;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class DuplicateEventNameException extends RuntimeException {

    public DuplicateEventNameException() {
        this("Event name should be unique.");
    }

    public DuplicateEventNameException(String message) {
        super(message);
    }
}
