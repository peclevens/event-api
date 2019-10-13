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
public class UrlValidatorTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Set<ConstraintViolation<UrlTest>> violations;

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
    public void isValid_nullValuePassedForUrl_returnTrue() {
        violations = validator.validate(new UrlTest());
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_emptyStringPassedForUrl_returnFalse() {
        violations = validator.validate(new UrlTest(""));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_invalidUrlPassed_returnFalse() {
        violations = validator.validate(new UrlTest("localhost"));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_invalidUrlProtocolPassed_returnFalse() {
        violations = validator.validate(new UrlTest("ftp://localhost"));
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isValid_validUrlWithHttpPassed_returnTrue() {
        violations = validator.validate(new UrlTest("http://localhost"));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_validUrlWithHttpsPassed_returnTrue() {
        violations = validator.validate(new UrlTest("https://localhost"));
        assertTrue(violations.isEmpty());
    }

    @Test
    public void isValid_validUrlWithPathPassed_returnTrue() {
        violations = validator.validate(new UrlTest("https://localhost/images/logo.png"));
        assertTrue(violations.isEmpty());
    }
}
