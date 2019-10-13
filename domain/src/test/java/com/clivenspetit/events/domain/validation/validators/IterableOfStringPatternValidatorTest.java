package com.clivenspetit.events.domain.validation.validators;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class IterableOfStringPatternValidatorTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Set<ConstraintViolation<IterableOfStringPatternTest>> violations;

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
    public void isValid_emptyStringsPassed_returnFalse() {
        violations = validator.validate(new IterableOfStringPatternTest(Arrays.asList("", "")));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_invalidItemPassed_returnFalse() {
        violations = validator.validate(new IterableOfStringPatternTest(Arrays.asList("item", "item1")));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_validItemsPassed_returnTrue() {
        violations = validator.validate(new IterableOfStringPatternTest(Arrays.asList("item", "items")));
        assertTrue(violations.isEmpty());
    }
}
