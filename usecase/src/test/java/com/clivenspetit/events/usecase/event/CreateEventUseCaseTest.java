package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.usecase.DataStubResource;
import com.clivenspetit.events.usecase.ValidationResource;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
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

    @Rule
    public DataStubResource stubResource = new DataStubResource();

    private Set<ConstraintViolation<CreateEventUseCase>> violations;
    private EventRepository eventRepository;
    private CreateEventUseCase createEventUseCase;

    @Before
    public void setUp() throws Exception {
        eventRepository = mock(EventRepository.class);
        createEventUseCase = new CreateEventUseCase(eventRepository);

        when(eventRepository.createEvent(stubResource.createEvent))
                .thenReturn(NEW_EVENT_ID);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        createEventUseCase = null;
        violations = null;
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

        Id newEvent = createEventUseCase.createEvent(stubResource.createEvent);

        verify(eventRepository, times(1))
                .createEvent(argumentCaptor.capture());

        assertThat(newEvent.getId(), is(NEW_EVENT_ID.getId()));
    }

    @Test
    public void createEvent_invalidEventPassed_throwException() throws Exception {
        Method method = CreateEventUseCase.class.getMethod("createEvent", CreateEvent.class);
        Object[] parameters = new Object[]{stubResource.invalidCreateEvent};

        violations = validationResource.executableValidator.validateParameters(createEventUseCase, method, parameters);

        assertEquals(13, violations.size());
    }
}
