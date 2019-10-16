package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.usecase.DataStubResource;
import com.clivenspetit.events.usecase.ValidationResource;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class FindEventUseCaseTest {

    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    @Rule
    public DataStubResource stubResource = new DataStubResource();

    private Set<ConstraintViolation<FindEventUseCase>> violations;
    private EventRepository eventRepository;
    private FindEventUseCase findEventUseCase;

    @Before
    public void setUp() throws Exception {
        eventRepository = mock(EventRepository.class);
        findEventUseCase = new FindEventUseCase(eventRepository);

        when(eventRepository.getEventById(EVENT_ID))
                .thenReturn(stubResource.event);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        findEventUseCase = null;
        violations = null;
    }

    @Test
    public void findEventById_nullArgumentPassed_throwException() throws Exception {
        Method method = FindEventUseCase.class.getMethod("findEventById", String.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(findEventUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test
    public void findEventById_invalidIdPassed_throwException() throws Exception {
        Method method = FindEventUseCase.class.getMethod("findEventById", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = validationResource.executableValidator.validateParameters(findEventUseCase, method, parameters);

        assertFalse("Invalid id argument should not pass.", violations.isEmpty());
    }

    @Test(expected = EventNotFoundException.class)
    public void findEventById_unknownIdPassed_throwException() {
        findEventUseCase.findEventById("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void findEventById_validIdPassed_returnEvent() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Event event = findEventUseCase.findEventById(EVENT_ID);

        verify(eventRepository, times(1))
                .getEventById(argumentCaptor.capture());

        List<Object> anyOfLocationOnlineUrl = Arrays.asList(event.getOnlineUrl(), event.getLocation());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(event.getId(), is(EVENT_ID));
        assertThat(event.getName(), is("Angular Connect"));
        assertThat(event.getSessions().size(), is(1));
        assertThat(event.getPrice(), is(1.00));
        assertThat(anyOfLocationOnlineUrl, anyOf(hasItem(notNullValue())));

        // Assert event location
        Location location = event.getLocation();
        if (location != null) {
            assertThat(location.getCountry(), is("United States"));
            assertThat(location.getCity(), is("New York"));
            assertThat(location.getAddress(), is("2695 Frederick Douglass Blvd"));
        }

        // Assert event first session
        Session session = event.getSessions().iterator().next();
        assertThat(session.getId(), is("f50425ee-dca3-4ada-93cc-09993db07311"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getVoters().size(), is(2));
    }
}
