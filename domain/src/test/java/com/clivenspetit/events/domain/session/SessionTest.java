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

package com.clivenspetit.events.domain.session;

import com.clivenspetit.events.domain.ValidationResource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Clivens Petit
 */
public class SessionTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<Session>> violations;
    private Session session;

    @Before
    public void setUp() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2036, 9, 26,
                10, 0, 0);

//        session = new Session();
//        session.setVersion(1);
//        session.setId("f50425ee-dca3-4ada-93cc-09993db07311");
//        session.setName("Using Angular 4 Pipes");
//        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
//        session.setLevel(Level.BEGINNER);
//        session.setPresenter("John Doe");
//        session.setDuration(LocalTime.of(1, 0));
//        session.setVoters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));
    }

    @After
    public void tearDown() throws Exception {
        session = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullArgumentPassed_throwException() {
        violations = validationResource.validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validSessionPassed_returnTrue() {
        Session session = SessionMother.validSession().build();

        violations = validationResource.validator.validate(session);
        assertTrue("Valid session should pass.", violations.isEmpty());
    }

    @Test
    public void invalidSessionPassed_returnFalse() {
//        session.setVersion(-1);
//        session.setId("f50425ee");
//        session.setName("A");
//        session.setDescription("Learn all about it.");
//        session.setLevel(null);
//        session.setPresenter("John/Doe");
//        session.setDuration(null);
//        session.setVoters(new LinkedHashSet<>(Arrays.asList("voter/1", "voter2")));

        Session session = SessionMother.invalidSession().build();

        violations = validationResource.validator.validate(session);
        assertEquals(8, violations.size());
    }
}
