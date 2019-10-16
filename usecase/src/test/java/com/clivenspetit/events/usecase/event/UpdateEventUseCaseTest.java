package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.usecase.DataStubResource;
import com.clivenspetit.events.usecase.ValidationResource;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class UpdateEventUseCaseTest {

    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    @Rule
    public DataStubResource stubResource = new DataStubResource();

    private Set<ConstraintViolation<UpdateEventUseCase>> violations;
    private EventRepository eventRepository;
    private UpdateEventUseCase updateEventUseCase;

    @Before
    public void setUp() throws Exception {
        eventRepository = mock(EventRepository.class);
        updateEventUseCase = new UpdateEventUseCase(eventRepository);

        when(eventRepository.updateEvent(EVENT_ID, stubResource.event))
                .thenReturn(stubResource.event);

        when(eventRepository.eventExists(EVENT_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        updateEventUseCase = null;
        violations = null;
    }

    @Test
    public void updateEvent_nullArgumentPassed_throwException() throws Exception {
        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{null, null};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Null arguments should not pass.", violations.size(), is(2));
    }

    @Test
    public void updateEvent_invalidArgumentPassed_throwException() throws Exception {
        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{"id", new Event()};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Invalid arguments should not pass.", violations.size(), is(6));
    }

    @Test(expected = EventNotFoundException.class)
    public void updateEvent_unknownIdPassed_throwException() {
        updateEventUseCase.updateEvent("cd4c770a-e53c-4d19-8393-3b37ec811b66", stubResource.event);
    }

    @Test
    public void updateEvent_invalidModifiedEventPassed_throwException() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2018, 9, 26,
                10, 0, 0);

        Event event = stubResource.event;

        event.setName("A");
        event.setPrice(-2.00);
        event.setOnlineUrl(null);
        event.setLocation(null);
        event.setStartDate(startDate);

        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{EVENT_ID, event};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Invalid arguments should not pass.", violations.size(), is(4));
    }

    @Test
    public void updateEvent_validModifiedEventPassed_returnUpdatedEvent() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        Event event = stubResource.event;

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
