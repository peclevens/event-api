package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.Session;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class UpdateEventUseCaseTest {

    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";

    private static ValidatorFactory validatorFactory;
    private static ExecutableValidator executableValidator;
    private Set<ConstraintViolation<UpdateEventUseCase>> violations;
    private EventRepository eventRepository;
    private UpdateEventUseCase updateEventUseCase;
    private Event event = null;

    @BeforeClass
    public static void beforeClass() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        executableValidator = validatorFactory.getValidator().forExecutables();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        validatorFactory.close();
        executableValidator = null;
        validatorFactory = null;
    }

    @Before
    public void setUp() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2036, 9, 26,
                10, 0, 0);

        Session session = new Session();
        session.setVersion(1);
        session.setId("f50425ee-dca3-4ada-93cc-09993db07311");
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));
        session.setVoters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));

        Location location = new Location();
        location.setCountry("United States");
        location.setCity("New York");
        location.setAddress("2695 Frederick Douglass Blvd");

        event = new Event();
        event.setVersion(2);
        event.setId(EVENT_ID);
        event.setName("Angular Connect");
        event.setImageUrl("http://localhost/images/angularconnect-shield.png");
        event.setOnlineUrl("https://hangouts.google.com");
        event.setLocation(location);
        event.setPrice(1.00);
        event.setStartDate(startDate);
        event.setSessions(Collections.singleton(session));

        eventRepository = mock(EventRepository.class);
        updateEventUseCase = new UpdateEventUseCase(eventRepository);

        when(eventRepository.updateEvent(EVENT_ID, event))
                .thenReturn(event);

        when(eventRepository.eventExists(EVENT_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        updateEventUseCase = null;
        violations = null;
        event = null;
    }

    @Test
    public void updateEvent_nullArgumentPassed_throwException() throws Exception {
        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{null, null};

        violations = executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Null arguments should not pass.", violations.size(), is(2));
    }

    @Test
    public void updateEvent_invalidArgumentPassed_throwException() throws Exception {
        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{"id", new Event()};

        violations = executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Invalid arguments should not pass.", violations.size(), is(6));
    }

    @Test(expected = EventNotFoundException.class)
    public void updateEvent_unknownIdPassed_throwException() {
        updateEventUseCase.updateEvent("cd4c770a-e53c-4d19-8393-3b37ec811b66", event);
    }

    @Test
    public void updateEvent_invalidModifiedEventPassed_throwException() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2018, 9, 26,
                10, 0, 0);

        event.setName("A");
        event.setPrice(-2.00);
        event.setOnlineUrl(null);
        event.setLocation(null);
        event.setStartDate(startDate);

        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{EVENT_ID, event};

        violations = executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Invalid arguments should not pass.", violations.size(), is(4));
    }

    @Test
    public void updateEvent_validModifiedEventPassed_returnUpdatedEvent() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        Location location = event.getLocation();
        location.setCountry("France");
        location.setCity("Paris");
        location.setAddress("72 Rue Nationale");

        event.setName("Using Angular Pipes");
        event.setPrice(0.00);

        Event updatedEvent = updateEventUseCase.updateEvent(EVENT_ID, event);

        verify(eventRepository, times(1))
                .eventExists(argumentCaptor.capture());

        verify(eventRepository, times(1))
                .updateEvent(argumentCaptor.capture(), eventArgumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(EVENT_ID));
        assertThat(eventArgumentCaptor.getAllValues().get(0).getName(), is(event.getName()));
        assertThat(updatedEvent.getName(), is("Using Angular Pipes"));
        assertThat(updatedEvent.getPrice(), is(0.00));
        assertThat(updatedEvent.getLocation().getCountry(), is("France"));
        assertThat(updatedEvent.getLocation().getCity(), is("Paris"));
        assertThat(updatedEvent.getLocation().getAddress(), is("72 Rue Nationale"));
    }
}
