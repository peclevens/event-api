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
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.CreateSessionMother;
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
public class CreateSessionUseCaseTest {

    private static final String NEW_SESSION_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<CreateSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private CreateSessionUseCase createSessionUseCase;
    private CreateSession session;

    @Before
    public void setUp() throws Exception {
        sessionRepository = mock(SessionRepository.class);
        createSessionUseCase = new CreateSessionUseCase(sessionRepository);

        session = CreateSessionMother.validSession().build();

        when(sessionRepository.createSession(NEW_SESSION_ID, session))
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
        Method method = CreateSessionUseCase.class.getMethod("createSession", String.class, CreateSession.class);
        Object[] parameters = new Object[]{null, null};

        violations = validationResource.executableValidator.validateParameters(createSessionUseCase,
                method, parameters);

        assertThat(violations.size(), is(2));
    }

    @Test
    public void createSession_validSessionPassed_returnNewSessionId() throws Exception {
        ArgumentCaptor<String> idArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<CreateSession> createSessionArgumentCaptor = ArgumentCaptor.forClass(CreateSession.class);

        String id = createSessionUseCase.createSession(NEW_SESSION_ID, session);

        verify(sessionRepository, times(1))
                .createSession(idArgumentCaptor.capture(), createSessionArgumentCaptor.capture());

        assertThat(id, is(NEW_SESSION_ID));
    }

    @Test
    public void createSession_invalidSessionPassed_throwException() throws Exception {
        CreateSession session = CreateSessionMother.invalidSession().build();

        Method method = CreateSessionUseCase.class.getMethod("createSession", String.class, CreateSession.class);
        Object[] parameters = new Object[]{NEW_SESSION_ID, session};

        violations = validationResource.executableValidator.validateParameters(createSessionUseCase,
                method, parameters);

        assertThat(violations, hasSize(5));
    }
}
