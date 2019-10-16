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
public class AnyOfValidatorTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<AnyOfTest>> violations;

    @Test(expected = IllegalArgumentException.class)
    public void isValid_nullArgumentPassed_throwException() {
        violations = validationResource.validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_nullValuePassedForAllFields_returnFalse() {
        violations = validationResource.validator.validate(new AnyOfTest());
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_valuePassedForFirstField_returnTrue() {
        violations = validationResource.validator.validate(new AnyOfTest("fieldOne", null));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_valuePassedForSecondField_returnTrue() {
        violations = validationResource.validator.validate(new AnyOfTest(null, "fieldTwo"));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_valuePassedForTwoFields_returnTrue() {
        violations = validationResource.validator.validate(new AnyOfTest("fieldOne", "fieldTwo"));
        assertTrue(violations.isEmpty());
    }
}
