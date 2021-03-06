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

package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.ValidationResource;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Clivens Petit
 */
public class WordValidatorTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<WordTest>> violations;

    @Test(expected = IllegalArgumentException.class)
    public void isValid_nullArgumentPassed_throwException() {
        violations = validationResource.validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_emptyStringPassed_returnFalse() {
        violations = validationResource.validator.validate(new WordTest(""));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_2wordsPassed_returnFalse() {
        violations = validationResource.validator.validate(new WordTest("Hello world"));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_5wordsPassed_returnTrue() {
        violations = validationResource.validator.validate(new WordTest("Hello world, how are you?"));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_10wordsPassed_returnTrue() {
        WordTest word = new WordTest("Hello world, how are you? This test should be fail.");
        violations = validationResource.validator.validate(word);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_11wordsPassed_returnFalse() {
        WordTest word = new WordTest("Hello world, how are you doing? This test should be fail.");
        violations = validationResource.validator.validate(word);

        assertFalse(violations.isEmpty());
    }
}
