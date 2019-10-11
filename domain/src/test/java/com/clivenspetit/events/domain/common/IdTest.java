package com.clivenspetit.events.domain.common;

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
public class IdTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Set<ConstraintViolation<Id>> violations;

    @BeforeClass
    public static void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        validatorFactory.close();
    }

    @Test
    public void invalidIdPassed_returnFalse() {
        violations = validator.validate(new Id());
        assertFalse(violations.isEmpty());

        violations = validator.validate(new Id(""));
        assertFalse(violations.isEmpty());

        violations = validator.validate(new Id("id"));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validIdPassed_returnTrue() {
        violations = validator.validate(new Id("5ed1dd8d-b715-48ff-a55b-ca4e12e98393"));
        assertTrue(violations.isEmpty());
    }
}
