package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
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

    private static ValidatorFactory validatorFactory;
    private static ExecutableValidator executableValidator;
    private Set<ConstraintViolation<CreateSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private CreateSessionUseCase createSessionUseCase;
    private CreateSession session = null;

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

        violations = executableValidator.validateParameters(createSessionUseCase, method, parameters);

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

        violations = executableValidator.validateParameters(createSessionUseCase, method, parameters);

        assertThat(violations, hasSize(4));
    }
}
