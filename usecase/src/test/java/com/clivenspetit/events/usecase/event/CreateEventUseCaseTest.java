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

import com.clivenspetit.events.domain.ValidationResource;
import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.event.CreateEvent;
import com.clivenspetit.events.domain.event.CreateEventMother;
import com.clivenspetit.events.domain.event.repository.EventRepository;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class CreateEventUseCaseTest {

    private static final Id NEW_EVENT_ID = new Id("0f366033-57c2-407a-8bf8-f057bd3487fd");

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<CreateEventUseCase>> violations;
    private EventRepository eventRepository;
    private CreateEventUseCase createEventUseCase;
    private CreateEvent event;

    @Before
    public void setUp() throws Exception {
        eventRepository = mock(EventRepository.class);
        createEventUseCase = new CreateEventUseCase(eventRepository);

        event = CreateEventMother.validEvent().build();
        when(eventRepository.createEvent(event))
                .thenReturn(NEW_EVENT_ID);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        createEventUseCase = null;
        violations = null;
        event = null;
    }

    @Test
    public void createEvent_nullArgumentPassed_throwException() throws Exception {
        Method method = CreateEventUseCase.class.getMethod("createEvent", CreateEvent.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(createEventUseCase, method, parameters);

        assertThat(violations.size(), is(1));
    }

    @Test
    public void createEvent_validEventPassed_returnNewEventId() throws Exception {
        ArgumentCaptor<CreateEvent> argumentCaptor = ArgumentCaptor.forClass(CreateEvent.class);

        Id newEvent = createEventUseCase.createEvent(this.event);

        verify(eventRepository, times(1))
                .createEvent(argumentCaptor.capture());

        assertThat(newEvent.getId(), is(NEW_EVENT_ID.getId()));
    }

    @Test
    public void createEvent_invalidEventPassed_throwException() throws Exception {
        CreateEvent event = CreateEventMother.invalidEvent().build();

        Method method = CreateEventUseCase.class.getMethod("createEvent", CreateEvent.class);
        Object[] parameters = new Object[]{event};

        violations = validationResource.executableValidator.validateParameters(createEventUseCase, method, parameters);

        assertEquals(13, violations.size());
    }
}
