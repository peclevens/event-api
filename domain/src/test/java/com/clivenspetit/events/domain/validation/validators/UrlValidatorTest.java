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
public class UrlValidatorTest {

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<UrlTest>> violations;

    @Test(expected = IllegalArgumentException.class)
    public void isValid_nullArgumentPassed_throwException() {
        violations = validationResource.validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_nullValuePassedForUrl_returnTrue() {
        violations = validationResource.validator.validate(new UrlTest());
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_emptyStringPassedForUrl_returnFalse() {
        violations = validationResource.validator.validate(new UrlTest(""));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_invalidUrlPassed_returnFalse() {
        violations = validationResource.validator.validate(new UrlTest("localhost"));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_invalidUrlProtocolPassed_returnFalse() {
        violations = validationResource.validator.validate(new UrlTest("ftp://localhost"));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_validUrlWithHttpPassed_returnTrue() {
        violations = validationResource.validator.validate(new UrlTest("http://localhost"));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_validUrlWithHttpsPassed_returnTrue() {
        violations = validationResource.validator.validate(new UrlTest("https://localhost"));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_validUrlWithPathPassed_returnTrue() {
        violations = validationResource.validator.validate(new UrlTest("https://localhost/images/logo.png"));
        assertTrue(violations.isEmpty());
    }
}
