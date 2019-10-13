package com.clivenspetit.events.domain.validation.validators;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class WordValidatorTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Set<ConstraintViolation<WordTest>> violations;

    @BeforeClass
    public static void beforeClass() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        validatorFactory.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid_nullArgumentPassed_throwException() {
        violations = validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_emptyStringPassed_returnFalse() {
        violations = validator.validate(new WordTest(""));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_2wordsPassed_returnFalse() {
        violations = validator.validate(new WordTest("Hello world"));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_5wordsPassed_returnTrue() {
        violations = validator.validate(new WordTest("Hello world, how are you?"));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_10wordsPassed_returnTrue() {
        WordTest word = new WordTest("Hello world, how are you? This test should be fail.");
        violations = validator.validate(word);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_11wordsPassed_returnFalse() {
        WordTest word = new WordTest("Hello world, how are you doing? This test should be fail.");
        violations = validator.validate(word);

        assertFalse(violations.isEmpty());
    }
}
