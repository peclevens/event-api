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
import com.clivenspetit.events.domain.common.LocationMother;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Clivens Petit
 */
public class EventTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<Event>> violations;

    @Test(expected = IllegalArgumentException.class)
    public void nullArgumentPassed_throwException() {
        violations = validationResource.validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validEventPassed_returnTrue() {
        violations = validationResource.validator.validate(EventMother.validEvent().build());
        assertTrue("Valid event should pass.", violations.isEmpty());
    }

    @Test
    public void invalidEventPassed_returnFalse() {
        Event event = EventMother.invalidEvent()
                .location(LocationMother.allFieldNullLocation()
                        .version(-1)
                        .build())
                .build();

        violations = validationResource.validator.validate(event);
        assertEquals(18, violations.size());
    }
}
