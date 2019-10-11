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
public class AnyOfValidatorTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Set<ConstraintViolation<AnyOfTest>> violations;

    @BeforeClass
    public static void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        validatorFactory.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid_nullArgumentPassed_throwException() {
        violations = validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_nullValuePassedForAllFields_returnFalse() {
        violations = validator.validate(new AnyOfTest());
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_valuePassedForFirstField_returnTrue() {
        violations = validator.validate(new AnyOfTest("fieldOne", null));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_valuePassedForSecondField_returnTrue() {
        violations = validator.validate(new AnyOfTest(null, "fieldTwo"));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_valuePassedForTwoFields_returnTrue() {
        violations = validator.validate(new AnyOfTest("fieldOne", "fieldTwo"));
        assertTrue(violations.isEmpty());
    }
}
