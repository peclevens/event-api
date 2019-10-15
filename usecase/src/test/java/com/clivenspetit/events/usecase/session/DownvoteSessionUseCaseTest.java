package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class DownvoteSessionUseCaseTest {

    private static final String SESSION_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    private static ValidatorFactory validatorFactory;
    private static ExecutableValidator executableValidator;
    private Set<ConstraintViolation<DownvoteSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private DownvoteSessionUseCase downvoteSessionUseCase;

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
        sessionRepository = mock(SessionRepository.class);
        downvoteSessionUseCase = new DownvoteSessionUseCase(sessionRepository);

        when(sessionRepository.sessionExists(SESSION_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        downvoteSessionUseCase = null;
        violations = null;
    }

    @Test
    public void downVoteSession_nullArgumentPassed_throwException() throws Exception {
        Method method = DownvoteSessionUseCase.class.getMethod("downVoteSession", String.class);
        Object[] parameters = new Object[]{null};

        violations = executableValidator.validateParameters(downvoteSessionUseCase, method, parameters);

        assertThat("Null id argument should not pass.", violations, hasSize(1));
    }

    @Test
    public void downVoteSession_invalidIdPassed_throwException() throws Exception {
        Method method = DownvoteSessionUseCase.class.getMethod("downVoteSession", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = executableValidator.validateParameters(downvoteSessionUseCase, method, parameters);

        assertThat("Invalid id argument should not pass.", violations, hasSize(1));
    }

    @Test(expected = SessionNotFoundException.class)
    public void downVoteSession_unknownIdPassed_throwException() {
        downvoteSessionUseCase.downVoteSession("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void downVoteSession_validIdPassed_return() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        downvoteSessionUseCase.downVoteSession(SESSION_ID);

        verify(sessionRepository, times(1))
                .sessionExists(argumentCaptor.capture());

        verify(sessionRepository, times(1))
                .downVoteSession(argumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(SESSION_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(SESSION_ID));
    }
}
