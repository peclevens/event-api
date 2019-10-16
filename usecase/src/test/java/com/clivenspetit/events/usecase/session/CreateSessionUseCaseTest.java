package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.session.CreateSession;
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
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class CreateSessionUseCaseTest {

    private static final Id NEW_SESSION_ID = new Id("f50425ee-dca3-4ada-93cc-09993db07311");

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<CreateSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private CreateSessionUseCase createSessionUseCase;
    private CreateSession session = null;

    @Before
    public void setUp() throws Exception {
        session = new CreateSession();
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));

        sessionRepository = mock(SessionRepository.class);
        createSessionUseCase = new CreateSessionUseCase(sessionRepository);

        when(sessionRepository.createSession(session))
                .thenReturn(NEW_SESSION_ID);
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        createSessionUseCase = null;
        violations = null;
        session = null;
    }

    @Test
    public void createSession_nullArgumentPassed_throwException() throws Exception {
        Method method = CreateSessionUseCase.class.getMethod("createSession", CreateSession.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(createSessionUseCase,
                method, parameters);

        assertThat(violations.size(), is(1));
    }

    @Test
    public void createSession_validSessionPassed_returnNewSessionId() throws Exception {
        ArgumentCaptor<CreateSession> argumentCaptor = ArgumentCaptor.forClass(CreateSession.class);

        Id newSession = createSessionUseCase.createSession(this.session);

        verify(sessionRepository, times(1))
                .createSession(argumentCaptor.capture());

        assertThat(newSession.getId(), is(NEW_SESSION_ID.getId()));
    }

    @Test
    public void createSession_invalidSessionPassed_throwException() throws Exception {
        CreateSession session = new CreateSession();
        session.setName("A");
        session.setDescription("Learn all about it.");
        session.setLevel(null);
        session.setPresenter("John/Doe");
        session.setDuration(null);

        Method method = CreateSessionUseCase.class.getMethod("createSession", CreateSession.class);
        Object[] parameters = new Object[]{session};

        violations = validationResource.executableValidator.validateParameters(createSessionUseCase,
                method, parameters);

        assertThat(violations, hasSize(5));
    }
}
