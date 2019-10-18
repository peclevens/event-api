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

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
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
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class FindSessionUseCaseTest {

    private static final String SESSION_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    @Rule
    public DataStubResource stubResource = new DataStubResource();

    private Set<ConstraintViolation<FindSessionUseCase>> violations;
    private SessionRepository sessionRepository;
    private FindSessionUseCase findSessionUseCase;

    @Before
    public void setUp() throws Exception {
        sessionRepository = mock(SessionRepository.class);
        findSessionUseCase = new FindSessionUseCase(sessionRepository);

        when(sessionRepository.getSessionById(SESSION_ID))
                .thenReturn(stubResource.session);
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        findSessionUseCase = null;
        violations = null;
    }

    @Test
    public void findSessionById_nullArgumentPassed_throwException() throws Exception {
        Method method = FindSessionUseCase.class.getMethod("findSessionById", String.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(findSessionUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test
    public void findSessionById_invalidIdPassed_throwException() throws Exception {
        Method method = FindSessionUseCase.class.getMethod("findSessionById", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = validationResource.executableValidator.validateParameters(findSessionUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test(expected = SessionNotFoundException.class)
    public void findSessionById_unknownIdPassed_throwException() {
        findSessionUseCase.findSessionById("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void findSessionById_validIdPassed_returnSession() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Session session = findSessionUseCase.findSessionById(SESSION_ID);

        verify(sessionRepository, times(1))
                .getSessionById(argumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(SESSION_ID));
        assertThat(session.getId(), is(SESSION_ID));
        assertThat(session.getVoters().size(), is(2));
        assertThat(session.getLevel(), is(Level.BEGINNER));
    }
}
