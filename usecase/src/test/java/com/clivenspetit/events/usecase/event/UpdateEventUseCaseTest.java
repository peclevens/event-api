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
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.EventMother;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class UpdateEventUseCaseTest {

    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<UpdateEventUseCase>> violations;
    private EventRepository eventRepository;
    private UpdateEventUseCase updateEventUseCase;
    private Event event;
    private Event modifiedEvent;

    @Before
    public void setUp() throws Exception {
        eventRepository = mock(EventRepository.class);
        updateEventUseCase = new UpdateEventUseCase(eventRepository);

        event = EventMother.validEvent().build();

        modifiedEvent = EventMother.validEvent()
                .name("Using Angular Pipes")
                .price(0.00)
                .location(Location.builder()
                        .country("France")
                        .city("Paris")
                        .address("72 Rue Nationale")
                        .build())
                .build();

        when(eventRepository.updateEvent(EVENT_ID, modifiedEvent))
                .thenReturn(modifiedEvent);

        when(eventRepository.eventExists(EVENT_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        updateEventUseCase = null;
        violations = null;
        event = null;
        modifiedEvent = null;
    }

    @Test
    public void updateEvent_nullArgumentPassed_throwException() throws Exception {
        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{null, null};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Null arguments should not pass.", violations.size(), is(2));
    }

    @Test
    public void updateEvent_invalidArgumentPassed_throwException() throws Exception {
        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{"id", Event.builder().build()};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Invalid arguments should not pass.", violations.size(), is(6));
    }

    @Test(expected = EventNotFoundException.class)
    public void updateEvent_unknownIdPassed_throwException() {
        updateEventUseCase.updateEvent("cd4c770a-e53c-4d19-8393-3b37ec811b66", event);
    }

    @Test
    public void updateEvent_invalidModifiedEventPassed_throwException() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2018, 9, 26,
                10, 0, 0);

        Event event = EventMother.validEvent()
                .name("A")
                .price(-2.00)
                .onlineUrl(null)
                .location(null)
                .startDate(startDate)
                .build();

        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, Event.class);
        Object[] parameters = new Object[]{EVENT_ID, event};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Invalid arguments should not pass.", violations.size(), is(4));
    }

    @Test
    public void updateEvent_validModifiedEventPassed_returnUpdatedEvent() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        Event updatedEvent = updateEventUseCase.updateEvent(EVENT_ID, modifiedEvent);

        verify(eventRepository, times(1))
                .eventExists(argumentCaptor.capture());

        verify(eventRepository, times(1))
                .updateEvent(argumentCaptor.capture(), eventArgumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(EVENT_ID));
        assertThat(eventArgumentCaptor.getAllValues().get(0).getName(), is(modifiedEvent.getName()));
        assertThat(updatedEvent.getName(), is("Using Angular Pipes"));
        assertThat(updatedEvent.getPrice(), is(0.00));
        assertThat(updatedEvent.getLocation().getCountry(), is("France"));
        assertThat(updatedEvent.getLocation().getCity(), is("Paris"));
        assertThat(updatedEvent.getLocation().getAddress(), is("72 Rue Nationale"));
    }
}
