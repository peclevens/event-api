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

package com.clivenspetit.events.domain.user;

import com.clivenspetit.events.domain.ValidationResource;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Clivens Petit
 */
public class UserTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<User>> violations;

    @Test(expected = IllegalArgumentException.class)
    public void nullArgumentPassed_throwException() {
        violations = validationResource.validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validUserPassed_returnTrue() {
        User user = UserMother.validUser().build();

        violations = validationResource.validator.validate(user);
        assertTrue("Valid user should pass.", violations.isEmpty());

        assertEquals("2cb4601f-bd11-4d01-98f5-b8a249e2b0ed", user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@gmail.com", user.getEmail());
        assertEquals("Hello123++", user.getPassword());
    }

    @Test
    public void invalidUserPassed_returnFalse() {
        User user = UserMother.invalidUser().build();

        violations = validationResource.validator.validate(user);
        assertEquals(6, violations.size());
    }
}
