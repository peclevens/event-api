package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.usecase.ValidationResource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class UpdateSessionUseCaseTest {

    private static final String SESSION_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<UpdateSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private UpdateSessionUseCase updateSessionUseCase;
    private Session session = null;

    @Before
    public void setUp() throws Exception {
        session = new Session();
        session.setVersion(1);
        session.setId(SESSION_ID);
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));
        session.setVoters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));

        sessionRepository = mock(SessionRepository.class);
        updateSessionUseCase = new UpdateSessionUseCase(sessionRepository);

        when(sessionRepository.updateSession(SESSION_ID, session))
                .thenReturn(session);

        when(sessionRepository.sessionExists(SESSION_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        updateSessionUseCase = null;
        violations = null;
        session = null;
    }

    @Test
    public void updateSession_nullArgumentPassed_throwException() throws Exception {
        Method method = UpdateSessionUseCase.class.getMethod("updateSession", String.class, Session.class);
        Object[] parameters = new Object[]{null, null};

        violations = validationResource.executableValidator.validateParameters(updateSessionUseCase,
                method, parameters);

        assertThat("Null arguments should not pass.", violations.size(), is(2));
    }

    @Test
    public void updateSession_invalidArgumentPassed_throwException() throws Exception {
        Method method = UpdateSessionUseCase.class.getMethod("updateSession", String.class, Session.class);
        Object[] parameters = new Object[]{"id", new Session()};

        violations = validationResource.executableValidator.validateParameters(updateSessionUseCase,
                method, parameters);

        assertThat("Invalid arguments should not pass.", violations, hasSize(8));
    }

    @Test(expected = SessionNotFoundException.class)
    public void updateSession_unknownIdPassed_throwException() {
        updateSessionUseCase.updateSession("cd4c770a-e53c-4d19-8393-3b37ec811b66", session);
    }

    @Test
    public void updateSession_invalidModifiedSessionPassed_throwException() throws Exception {
        session.setName("A");
        session.setDescription("two words");
        session.setVersion(-1);
        session.setDuration(null);
        session.setPresenter("user/name"); // TODO Name regex validation is not working.

        Method method = UpdateSessionUseCase.class.getMethod("updateSession", String.class, Session.class);
        Object[] parameters = new Object[]{SESSION_ID, session};

        violations = validationResource.executableValidator.validateParameters(updateSessionUseCase,
                method, parameters);

        assertThat("Invalid arguments should not pass.", violations, hasSize(5));
    }

    @Test
    public void updateSession_validModifiedSessionPassed_returnUpdatedSession() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Session> sessionArgumentCaptor = ArgumentCaptor.forClass(Session.class);

        session.setName("Using Angular Pipes");
        session.setDescription("Learn all about the new pipes in Angular");
        session.setLevel(Level.ADVANCED);

        Session updatedSession = updateSessionUseCase.updateSession(SESSION_ID, session);

        verify(sessionRepository, times(1))
                .sessionExists(argumentCaptor.capture());

        verify(sessionRepository, times(1))
                .updateSession(argumentCaptor.capture(), sessionArgumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(SESSION_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(SESSION_ID));
        assertThat(sessionArgumentCaptor.getAllValues().get(0).getName(), is(session.getName()));
        assertThat(updatedSession.getName(), is("Using Angular Pipes"));
        assertThat(updatedSession.getDescription(), is("Learn all about the new pipes in Angular"));
        assertThat(updatedSession.getLevel(), is(Level.ADVANCED));
    }
}
