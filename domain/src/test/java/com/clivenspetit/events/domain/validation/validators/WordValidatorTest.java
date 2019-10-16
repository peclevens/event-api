package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.ValidationResource;
import org.junit.ClassRule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
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
