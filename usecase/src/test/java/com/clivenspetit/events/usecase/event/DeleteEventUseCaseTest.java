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

package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
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
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class DeleteEventUseCaseTest {

    private static final String EVENT_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<DeleteEventUseCase>> violations;
    private EventRepository eventRepository;
    private DeleteEventUseCase deleteEventUseCase;

    @Before
    public void setUp() throws Exception {
        eventRepository = mock(EventRepository.class);
        deleteEventUseCase = new DeleteEventUseCase(eventRepository);

        when(eventRepository.eventExists(EVENT_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        deleteEventUseCase = null;
        violations = null;
    }

    @Test
    public void deleteEventById_nullArgumentPassed_throwException() throws Exception {
        Method method = DeleteEventUseCase.class.getMethod("deleteEventById", String.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(deleteEventUseCase, method, parameters);

        assertThat("Null id argument should not pass.", violations.size(), is(1));
    }

    @Test
    public void deleteEventById_invalidIdPassed_throwException() throws Exception {
        Method method = DeleteEventUseCase.class.getMethod("deleteEventById", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = validationResource.executableValidator.validateParameters(deleteEventUseCase, method, parameters);

        assertThat("Invalid id argument should not pass.", violations.size(), is(1));
    }

    @Test(expected = EventNotFoundException.class)
    public void deleteEventById_unknownIdPassed_throwException() {
        deleteEventUseCase.deleteEventById("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void deleteEventById_validIdPassed_return() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        deleteEventUseCase.deleteEventById(EVENT_ID);

        verify(eventRepository, times(1))
                .eventExists(argumentCaptor.capture());

        verify(eventRepository, times(1))
                .deleteEventById(argumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(EVENT_ID));
    }
}
