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

package com.clivenspetit.events.domain.event;

import com.clivenspetit.events.domain.ValidationResource;
import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.session.CreateSession;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Clivens Petit
 */
public class CreateEventTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<CreateEvent>> violations;

    @Test(expected = IllegalArgumentException.class)
    public void nullArgumentPassed_throwException() {
        violations = validationResource.validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validEventPassed_returnTrue() {
        LocalDateTime startDate = LocalDateTime.of(2036, 9, 26,
                10, 0, 0);

        CreateSession session = new CreateSession();
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));

        Location location = new Location();
        location.setCountry("United States");
        location.setCity("New York");
        location.setAddress("2695 Frederick Douglass Blvd");

        CreateEvent event = new CreateEvent();
        event.setName("Angular Connect");
        event.setImageUrl("http://localhost/images/angularconnect-shield.png");
        event.setOnlineUrl("https://hangouts.google.com");
        event.setLocation(location);
        event.setPrice(1.00);
        event.setStartDate(startDate);
        event.setSessions(Collections.singleton(session));

        violations = validationResource.validator.validate(event);
        assertTrue("Valid event should pass.", violations.isEmpty());
    }

    @Test
    public void invalidEventPassed_returnFalse() {
        LocalDateTime startDate = LocalDateTime.of(2018, 9, 26,
                10, 0, 0);

        CreateSession session = new CreateSession();
        session.setName("A");
        session.setDescription("Learn all about it.");
        session.setLevel(null);
        session.setPresenter("John/Doe");
        session.setDuration(null);

        Location location = new Location();
        location.setCountry(null);
        location.setCity(null);
        location.setAddress(null);

        CreateEvent event = new CreateEvent();
        event.setName("E");
        event.setImageUrl("localhost/images/angularconnect-shield.png");
        event.setOnlineUrl("hangouts.google.com");
        event.setLocation(location);
        event.setPrice(-1.00);
        event.setStartDate(startDate);
        event.setSessions(Collections.singleton(session));

        violations = validationResource.validator.validate(event);
        assertEquals(13, violations.size());
    }
}
