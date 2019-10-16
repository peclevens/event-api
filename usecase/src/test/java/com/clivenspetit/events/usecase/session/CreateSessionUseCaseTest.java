package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.usecase.DataStubResource;
import com.clivenspetit.events.usecase.ValidationResource;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
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

    @Rule
    public DataStubResource stubResource = new DataStubResource();

    private Set<ConstraintViolation<CreateSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private CreateSessionUseCase createSessionUseCase;

    @Before
    public void setUp() throws Exception {
        sessionRepository = mock(SessionRepository.class);
        createSessionUseCase = new CreateSessionUseCase(sessionRepository);

        when(sessionRepository.createSession(stubResource.createSession))
                .thenReturn(NEW_SESSION_ID);
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        createSessionUseCase = null;
        violations = null;
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

        Id newSession = createSessionUseCase.createSession(stubResource.createSession);

        verify(sessionRepository, times(1))
                .createSession(argumentCaptor.capture());

        assertThat(newSession.getId(), is(NEW_SESSION_ID.getId()));
    }

    @Test
    public void createSession_invalidSessionPassed_throwException() throws Exception {
        Method method = CreateSessionUseCase.class.getMethod("createSession", CreateSession.class);
        Object[] parameters = new Object[]{stubResource.invalidCreateSession};

        violations = validationResource.executableValidator.validateParameters(createSessionUseCase,
                method, parameters);

        assertThat(violations, hasSize(5));
    }
}
