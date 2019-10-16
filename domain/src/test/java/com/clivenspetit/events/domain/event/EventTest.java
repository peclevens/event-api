package com.clivenspetit.events.domain.event;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.session.Session;
import org.junit.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class EventTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Set<ConstraintViolation<Event>> violations;
    private Session session = null;
    private Location location = null;
    private Event event = null;

    @BeforeClass
    public static void beforeClass() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        validatorFactory.close();
    }

    @Before
    public void setUp() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2036, 9, 26,
                10, 0, 0);

        session = new Session();
        session.setVersion(1);
        session.setId("f50425ee-dca3-4ada-93cc-09993db07311");
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));
        session.setVoters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));

        location = new Location();
        location.setCountry("United States");
        location.setCity("New York");
        location.setAddress("2695 Frederick Douglass Blvd");

        event = new Event();
        event.setVersion(2);
        event.setId("eb3a377c-3742-43ac-8d87-35534de2db8f");
        event.setName("Angular Connect");
        event.setImageUrl("http://localhost/images/angularconnect-shield.png");
        event.setOnlineUrl("https://hangouts.google.com");
        event.setLocation(location);
        event.setPrice(1.00);
        event.setStartDate(startDate);
        event.setSessions(Collections.singleton(session));
    }

    @After
    public void tearDown() throws Exception {
        session = null;
        location = null;
        event = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullArgumentPassed_throwException() {
        violations = validator.validate(null);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validEventPassed_returnTrue() {
        violations = validator.validate(event);
        assertTrue("Valid event should pass.", violations.isEmpty());
    }

    @Test
    public void invalidEventPassed_returnFalse() {
        session.setVersion(-1);
        session.setId("f50425ee");
        session.setName("A");
        session.setDescription("Learn all about it.");
        session.setLevel(null);
        session.setPresenter("John/Doe");
        session.setDuration(null);
        session.setVoters(new LinkedHashSet<>(Arrays.asList("voter/1", "voter2")));

        location.setCountry(null);
        location.setCity(null);
        location.setAddress(null);

        event.setVersion(-2);
        event.setId("eb3a377c");
        event.setName("E");
        event.setImageUrl("localhost/images/angularconnect-shield.png");
        event.setOnlineUrl("hangouts.google.com");
        event.setLocation(location);
        event.setPrice(-1.00);
        event.setStartDate(event.getStartDate().withYear(2018));
        event.setSessions(Collections.singleton(session));

        violations = validator.validate(event);
        assertEquals(18, violations.size());
    }
}
