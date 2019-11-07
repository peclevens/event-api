/*
 * Copyright 2019 MAGIC SOFTWARE BAY, SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.ValidationResource;
import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.SessionMother;
import com.clivenspetit.events.domain.session.UpdateSession;
import com.clivenspetit.events.domain.session.UpdateSessionMother;
import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
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
public class UpdateSessionUseCaseTest {

    private static final String SESSION_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<UpdateSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private UpdateSessionUseCase updateSessionUseCase;
    private Session session;
    private UpdateSession modifiedSession;

    @Before
    public void setUp() throws Exception {
        sessionRepository = mock(SessionRepository.class);
        updateSessionUseCase = new UpdateSessionUseCase(sessionRepository);

        modifiedSession = UpdateSessionMother.validSession()
                .name("Using Angular Pipes")
                .description("Learn all about the new pipes in Angular")
                .level(Level.ADVANCED)
                .build();

        session = SessionMother.validSession()
                .name(modifiedSession.getName())
                .description(modifiedSession.getDescription())
                .level(modifiedSession.getLevel())
                .build();

        when(sessionRepository.updateSession(SESSION_ID, modifiedSession))
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
        modifiedSession = null;
    }

    @Test
    public void updateSession_nullArgumentPassed_throwException() throws Exception {
        Method method = UpdateSessionUseCase.class.getMethod("updateSession", String.class, UpdateSession.class);
        Object[] parameters = new Object[]{null, null};

        violations = validationResource.executableValidator.validateParameters(updateSessionUseCase,
                method, parameters);

        assertThat("Null arguments should not pass.", violations.size(), is(2));
    }

    @Test
    public void updateSession_invalidArgumentPassed_throwException() throws Exception {
        Method method = UpdateSessionUseCase.class.getMethod("updateSession", String.class, UpdateSession.class);
        Object[] parameters = new Object[]{"id", UpdateSession.builder().build()};

        violations = validationResource.executableValidator.validateParameters(updateSessionUseCase,
                method, parameters);

        assertThat("Invalid arguments should not pass.", violations, hasSize(7));
    }

    @Test(expected = SessionNotFoundException.class)
    public void updateSession_unknownIdPassed_throwException() {
        updateSessionUseCase.updateSession("cd4c770a-e53c-4d19-8393-3b37ec811b66", modifiedSession);
    }

    @Test
    public void updateSession_invalidModifiedSessionPassed_throwException() throws Exception {
        UpdateSession session = UpdateSessionMother.validSession()
                .version(-1)
                .name("A")
                .description("two words")
                .duration(null)
                .presenter("user/name")
                .build();

        Method method = UpdateSessionUseCase.class.getMethod("updateSession", String.class, UpdateSession.class);
        Object[] parameters = new Object[]{SESSION_ID, session};

        violations = validationResource.executableValidator.validateParameters(updateSessionUseCase,
                method, parameters);

        assertThat("Invalid arguments should not pass.", violations, hasSize(5));
    }

    @Test
    public void updateSession_validModifiedSessionPassed_returnUpdatedSession() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UpdateSession> sessionArgumentCaptor = ArgumentCaptor.forClass(UpdateSession.class);

        Session updatedSession = updateSessionUseCase.updateSession(SESSION_ID, modifiedSession);

        verify(sessionRepository, times(1))
                .sessionExists(argumentCaptor.capture());

        verify(sessionRepository, times(1))
                .updateSession(argumentCaptor.capture(), sessionArgumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(SESSION_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(SESSION_ID));
        assertThat(sessionArgumentCaptor.getAllValues().get(0).getName(), is(modifiedSession.getName()));
        assertThat(updatedSession.getName(), is("Using Angular Pipes"));
        assertThat(updatedSession.getDescription(), is("Learn all about the new pipes in Angular"));
        assertThat(updatedSession.getLevel(), is(Level.ADVANCED));
    }
}
