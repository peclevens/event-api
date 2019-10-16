package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.usecase.ValidationResource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class CreateEventUseCaseTest {

    private static final Id NEW_EVENT_ID = new Id("0f366033-57c2-407a-8bf8-f057bd3487fd");

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<CreateEventUseCase>> violations;
    private EventRepository eventRepository;
    private CreateEventUseCase createEventUseCase;
    private CreateEvent event = null;

    @Before
    public void setUp() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2036, 9, 26,
                10, 0, 0);

        CreateSession session = new CreateSession();
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));

        Location location = new Location();
        location.setCountry("United States");
        location.setCity("New York");
        location.setAddress("2695 Frederick Douglass Blvd");

        event = new CreateEvent();
        event.setName("Angular Connect");
        event.setImageUrl("http://localhost/images/angularconnect-shield.png");
        event.setOnlineUrl("https://hangouts.google.com");
        event.setLocation(location);
        event.setPrice(1.00);
        event.setStartDate(startDate);
        event.setSessions(Collections.singleton(session));

        eventRepository = mock(EventRepository.class);
        createEventUseCase = new CreateEventUseCase(eventRepository);

        when(eventRepository.createEvent(event))
                .thenReturn(NEW_EVENT_ID);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        createEventUseCase = null;
        violations = null;
        event = null;
    }

    @Test
    public void createEvent_nullArgumentPassed_throwException() throws Exception {
        Method method = CreateEventUseCase.class.getMethod("createEvent", CreateEvent.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(createEventUseCase, method, parameters);

        assertThat(violations.size(), is(1));
    }

    @Test
    public void createEvent_validEventPassed_returnNewEventId() throws Exception {
        ArgumentCaptor<CreateEvent> argumentCaptor = ArgumentCaptor.forClass(CreateEvent.class);

        Id newEvent = createEventUseCase.createEvent(this.event);

        verify(eventRepository, times(1))
                .createEvent(argumentCaptor.capture());

        assertThat(newEvent.getId(), is(NEW_EVENT_ID.getId()));
    }

    @Test
    public void createEvent_invalidEventPassed_throwException() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2018, 9, 26,
                10, 0, 0);

        CreateSession session = new CreateSession();
        session.setName("A");
        session.setDescription("Learn all about it.");
        session.setLevel(null);
        session.setPresenter("John/Doe");
        session.setDuration(null);

        Location location = new Location();
        location.setCountry(null);
        location.setCity(null);
        location.setAddress(null);

        CreateEvent event = new CreateEvent();
        event.setName("E");
        event.setImageUrl("localhost/images/angularconnect-shield.png");
        event.setOnlineUrl("hangouts.google.com");
        event.setLocation(location);
        event.setPrice(-1.00);
        event.setStartDate(startDate);
        event.setSessions(Collections.singleton(session));

        Method method = CreateEventUseCase.class.getMethod("createEvent", CreateEvent.class);
        Object[] parameters = new Object[]{event};

        violations = validationResource.executableValidator.validateParameters(createEventUseCase, method, parameters);

        assertEquals(13, violations.size());
    }
}
