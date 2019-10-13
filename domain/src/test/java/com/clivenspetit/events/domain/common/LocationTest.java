package com.clivenspetit.events.domain.common;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class LocationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Set<ConstraintViolation<Location>> violations;

    @BeforeClass
    public static void beforeClass() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        validatorFactory.close();
    }

    @Test
    public void invalidLocationPassed_returnConstraintViolations() {
        // Test required fields
        violations = validator.validate(new Location());
        assertEquals(3, violations.size());

        // Test invalid location
        Location location = new Location();
        location.setVersion(-1);
        location.setAddress(null);
        location.setCity(null);
        location.setCountry(null);

        violations = validator.validate(location);
        assertEquals(4, violations.size());
    }

    @Test
    public void validLocationPassed_returnTrue() {
        Location location = new Location();
        location.setVersion(0);
        location.setAddress("Some Street");
        location.setCity("Some City");
        location.setCountry("Some Country");

        violations = validator.validate(location);
        assertTrue("Valid location should pass.", violations.isEmpty());
    }
}
