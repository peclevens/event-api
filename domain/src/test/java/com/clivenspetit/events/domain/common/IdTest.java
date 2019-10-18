package com.clivenspetit.events.domain.common;

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
public class IdTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<Id>> violations;

    @Test
    public void invalidIdPassed_returnFalse() {
        violations = validationResource.validator.validate(new Id());
        assertFalse("Null id should pass.", violations.isEmpty());

        violations = validationResource.validator.validate(new Id(""));
        assertFalse("Empty id should not pass.", violations.isEmpty());

        violations = validationResource.validator.validate(new Id("id"));
        assertFalse("Invalid UUID should not pass.", violations.isEmpty());
    }

    @Test
    public void validIdPassed_returnTrue() {
        violations = validationResource.validator.validate(new Id("5ed1dd8d-b715-48ff-a55b-ca4e12e98393"));
        assertTrue("Valid UUID should pass.", violations.isEmpty());
    }
}
