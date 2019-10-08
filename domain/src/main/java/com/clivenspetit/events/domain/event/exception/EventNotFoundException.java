package com.clivenspetit.events.domain.event.exception;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException() {
        this("Event not found.");
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
