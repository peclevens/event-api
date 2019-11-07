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
import com.clivenspetit.events.domain.event.UpdateEvent;
import com.clivenspetit.events.domain.event.UpdateEventMother;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
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
    private UpdateEvent modifiedEvent;

    @Before
    public void setUp() throws Exception {
        eventRepository = mock(EventRepository.class);
        updateEventUseCase = new UpdateEventUseCase(eventRepository);

        modifiedEvent = UpdateEventMother.validEvent()
                .name("Using Angular Pipes")
                .price(0.00D)
                .location(Location.builder()
                        .country("France")
                        .city("Paris")
                        .address("72 Rue Nationale")
                        .build())
                .build();

        event = EventMother.validEvent()
                .name(modifiedEvent.getName())
                .price(BigDecimal.valueOf(modifiedEvent.getPrice()))
                .location(modifiedEvent.getLocation())
                .build();

        when(eventRepository.updateEvent(EVENT_ID, modifiedEvent))
                .thenReturn(event);

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
        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, UpdateEvent.class);
        Object[] parameters = new Object[]{null, null};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Null arguments should not pass.", violations.size(), is(2));
    }

    @Test
    public void updateEvent_invalidArgumentPassed_throwException() throws Exception {
        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, UpdateEvent.class);
        Object[] parameters = new Object[]{"id", UpdateEvent.builder().build()};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Invalid arguments should not pass.", violations.size(), is(6));
    }

    @Test(expected = EventNotFoundException.class)
    public void updateEvent_unknownIdPassed_throwException() {
        updateEventUseCase.updateEvent("cd4c770a-e53c-4d19-8393-3b37ec811b66", modifiedEvent);
    }

    @Test
    public void updateEvent_invalidModifiedEventPassed_throwException() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2018, 9, 26,
                10, 0, 0);

        UpdateEvent event = UpdateEventMother.validEvent()
                .name("A")
                .price(-2.00)
                .onlineUrl(null)
                .location(null)
                .startDate(startDate)
                .build();

        Method method = UpdateEventUseCase.class.getMethod("updateEvent", String.class, UpdateEvent.class);
        Object[] parameters = new Object[]{EVENT_ID, event};

        violations = validationResource.executableValidator.validateParameters(updateEventUseCase, method, parameters);

        assertThat("Invalid arguments should not pass.", violations.size(), is(5));
    }

    @Test
    public void updateEvent_validModifiedEventPassed_returnUpdatedEvent() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UpdateEvent> eventArgumentCaptor = ArgumentCaptor.forClass(UpdateEvent.class);

        Event updatedEvent = updateEventUseCase.updateEvent(EVENT_ID, modifiedEvent);

        verify(eventRepository, times(1))
                .eventExists(argumentCaptor.capture());

        verify(eventRepository, times(1))
                .updateEvent(argumentCaptor.capture(), eventArgumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(EVENT_ID));
        assertThat(eventArgumentCaptor.getAllValues().get(0).getName(), is(modifiedEvent.getName()));
        assertThat(updatedEvent.getName(), is("Using Angular Pipes"));
        assertThat(updatedEvent.getPrice(), equalTo(BigDecimal.valueOf(0.0)));
        assertThat(updatedEvent.getLocation().getCountry(), is("France"));
        assertThat(updatedEvent.getLocation().getCity(), is("Paris"));
        assertThat(updatedEvent.getLocation().getAddress(), is("72 Rue Nationale"));
    }
}
