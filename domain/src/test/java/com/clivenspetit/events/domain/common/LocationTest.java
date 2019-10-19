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

package com.clivenspetit.events.domain.common;

import com.clivenspetit.events.domain.ValidationResource;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Clivens Petit
 */
public class LocationTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<Location>> violations;

    @Test
    public void invalidLocationPassed_returnConstraintViolations() {
        // Test required fields
        Location location = LocationMother.allFieldNullLocation()
                .build();

        violations = validationResource.validator.validate(location);
        assertEquals(3, violations.size());

        // Test invalid location
        location = LocationMother.allFieldNullLocation()
                .version(-1)
                .build();

        violations = validationResource.validator.validate(location);
        assertEquals(4, violations.size());
    }

    @Test
    public void validLocationPassed_returnTrue() {
        Location location = LocationMother.validLocation().build();

        violations = validationResource.validator.validate(location);
        assertTrue("Valid location should pass.", violations.isEmpty());
    }
}
