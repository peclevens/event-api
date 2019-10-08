package com.clivenspetit.events.domain.validation.validators;

import org.junit.After;
import org.junit.Before;
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

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @After
    public void tearDown() throws Exception {
        validator = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid_nullArgumentPassed_throwException() {
        Set<ConstraintViolation<String>> violations = validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid_emptyStringPassed_returnTrue() {
        Set<ConstraintViolation<String>> violations = validator.validate("");
        assertTrue(violations.isEmpty());
    }
}
