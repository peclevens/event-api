package com.clivenspetit.events.domain.session;

import com.clivenspetit.events.domain.common.Level;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class CreateSessionTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Set<ConstraintViolation<CreateSession>> violations;

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
    public void nullArgumentPassed_throwException() {
        violations = validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validEventPassed_returnTrue() {
        CreateSession session = new CreateSession();
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));

        violations = validator.validate(session);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void invalidEventPassed_returnFalse() {
        CreateSession session = new CreateSession();
        session.setName("A");
        session.setDescription("Learn all about it.");
        session.setLevel(null);
        session.setPresenter("John/Doe");
        session.setDuration(null);

        violations = validator.validate(session);
        assertEquals(4, violations.size());
    }
}
