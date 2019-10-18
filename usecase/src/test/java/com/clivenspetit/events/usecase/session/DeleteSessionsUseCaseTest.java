package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.usecase.ValidationResource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class DeleteSessionsUseCaseTest {

    private static final String EVENT_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<DeleteSessionsUseCase>> violations;
    private SessionRepository sessionRepository;
    private EventRepository eventRepository;
    private DeleteSessionsUseCase deleteSessionsUseCase;

    @Before
    public void setUp() throws Exception {
        sessionRepository = mock(SessionRepository.class);
        eventRepository = mock(EventRepository.class);
        deleteSessionsUseCase = new DeleteSessionsUseCase(sessionRepository, eventRepository);

        when(eventRepository.eventExists(EVENT_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        deleteSessionsUseCase = null;
        violations = null;
    }

    @Test
    public void deleteAllSessionsByEventId_nullArgumentPassed_throwException() throws Exception {
        Method method = DeleteSessionsUseCase.class.getMethod("deleteAllSessionsByEventId", String.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(deleteSessionsUseCase,
                method, parameters);

        assertThat("Null id argument should not pass.", violations, hasSize(1));
    }

    @Test
    public void deleteAllSessionsByEventId_invalidIdPassed_throwException() throws Exception {
        Method method = DeleteSessionsUseCase.class.getMethod("deleteAllSessionsByEventId", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = validationResource.executableValidator.validateParameters(deleteSessionsUseCase,
                method, parameters);

        assertThat("Invalid id argument should not pass.", violations, hasSize(1));
    }

    @Test(expected = EventNotFoundException.class)
    public void deleteAllSessionsByEventId_unknownIdPassed_throwException() {
        deleteSessionsUseCase.deleteAllSessionsByEventId("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void deleteAllSessionsByEventId_validIdPassed_return() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        deleteSessionsUseCase.deleteAllSessionsByEventId(EVENT_ID);

        verify(eventRepository, times(1))
                .eventExists(argumentCaptor.capture());

        verify(sessionRepository, times(1))
                .deleteAllSessionsByEventId(argumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(EVENT_ID));
    }
}
