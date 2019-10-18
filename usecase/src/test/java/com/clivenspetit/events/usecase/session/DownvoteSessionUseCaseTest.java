package com.clivenspetit.events.usecase.session;

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
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class DownvoteSessionUseCaseTest {

    private static final String SESSION_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<DownvoteSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private DownvoteSessionUseCase downvoteSessionUseCase;

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

        violations = validationResource.executableValidator.validateParameters(downvoteSessionUseCase,
                method, parameters);

        assertThat("Null id argument should not pass.", violations, hasSize(1));
    }

    @Test
    public void downVoteSession_invalidIdPassed_throwException() throws Exception {
        Method method = DownvoteSessionUseCase.class.getMethod("downVoteSession", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = validationResource.executableValidator.validateParameters(downvoteSessionUseCase,
                method, parameters);

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
