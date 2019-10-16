package com.clivenspetit.events.usecase;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.Session;
import org.junit.rules.ExternalResource;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class DataStubResource extends ExternalResource {

    public CreateEvent invalidCreateEvent;
    public CreateEvent createEvent;
    public Event event;
    public CreateSession invalidCreateSession;
    public CreateSession createSession;
    public Session session;
    private Location location;
    private Location invalidLocation;

    @Override
    protected void before() throws Throwable {
        createValidLocation();
        createInvalidLocation();

        createValidSession();
        createInvalidSession();

        createValidEvent();
        createInvalidEvent();
    }

    @Override
    protected void after() {
        createSession = null;
        location = null;
        createEvent = null;
    }

    private void createValidSession() {
        // Create session objec
        createSession = new CreateSession();
        createSession.setName("Using Angular 4 Pipes");
        createSession.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        createSession.setLevel(Level.BEGINNER);
        createSession.setPresenter("John Doe");
        createSession.setDuration(LocalTime.of(1, 0));

        // Session object
        session = new Session();
        session.setVersion(1);
        session.setId("f50425ee-dca3-4ada-93cc-09993db07311");
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));
        session.setVoters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));
    }

    private void createInvalidSession() {
        invalidCreateSession = new CreateSession();
        invalidCreateSession.setName("A");
        invalidCreateSession.setDescription("Learn all about it.");
        invalidCreateSession.setLevel(null);
        invalidCreateSession.setPresenter("John/Doe");
        invalidCreateSession.setDuration(null);
    }

    private void createValidLocation() {
        location = new Location();
        location.setCountry("United States");
        location.setCity("New York");
        location.setAddress("2695 Frederick Douglass Blvd");
    }

    private void createInvalidLocation() {
        invalidLocation = new Location();
        invalidLocation.setCountry(null);
        invalidLocation.setCity(null);
        invalidLocation.setAddress(null);
    }

    private void createValidEvent() {
        LocalDateTime startDate = LocalDateTime.of(2036, 9, 26,
                10, 0, 0);

        // Create event object
        createEvent = new CreateEvent();
        createEvent.setName("Angular Connect");
        createEvent.setImageUrl("http://localhost/images/angularconnect-shield.png");
        createEvent.setOnlineUrl("https://hangouts.google.com");
        createEvent.setLocation(location);
        createEvent.setPrice(1.00);
        createEvent.setStartDate(startDate);
        createEvent.setSessions(Collections.singleton(createSession));

        // Event object
        event = new Event();
        event.setVersion(2);
        event.setId("eb3a377c-3742-43ac-8d87-35534de2db8f");
        event.setName("Angular Connect");
        event.setImageUrl("http://localhost/images/angularconnect-shield.png");
        event.setOnlineUrl("https://hangouts.google.com");
        event.setLocation(location);
        event.setPrice(1.00);
        event.setStartDate(startDate);
        event.setSessions(Collections.singleton(session));
    }

    private void createInvalidEvent() {
        LocalDateTime startDate = LocalDateTime.of(2018, 9, 26,
                10, 0, 0);

        invalidCreateEvent = new CreateEvent();
        invalidCreateEvent.setName("E");
        invalidCreateEvent.setImageUrl("localhost/images/angularconnect-shield.png");
        invalidCreateEvent.setOnlineUrl("hangouts.google.com");
        invalidCreateEvent.setLocation(invalidLocation);
        invalidCreateEvent.setPrice(-1.00);
        invalidCreateEvent.setStartDate(startDate);
        invalidCreateEvent.setSessions(Collections.singleton(invalidCreateSession));
    }
}