package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.Session;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class FindEventUseCaseTest {

    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";

    private static ValidatorFactory validatorFactory;
    private static ExecutableValidator executableValidator;
    private Set<ConstraintViolation<FindEventUseCase>> violations;
    private EventRepository eventRepository;
    private FindEventUseCase findEventUseCase;
    private Event event;

    @BeforeClass
    public static void beforeClass() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        executableValidator = validatorFactory.getValidator().forExecutables();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        validatorFactory.close();
        executableValidator = null;
        validatorFactory = null;
    }

    @Before
    public void setUp() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2036, 9, 26,
                10, 0, 0);

        Session session = new Session();
        session.setVersion(1);
        session.setId("f50425ee-dca3-4ada-93cc-09993db07311");
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));
        session.setVoters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));

        Location location = new Location();
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

        eventRepository = mock(EventRepository.class);
        findEventUseCase = new FindEventUseCase(eventRepository);

        when(eventRepository.getEventById(EVENT_ID))
                .thenReturn(event);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        findEventUseCase = null;
        event = null;
        violations = null;
    }

    @Test
    public void findEventById_nullArgumentPassed_throwException() throws Exception {
        Method method = FindEventUseCase.class.getMethod("findEventById", String.class);
        Object[] parameters = new Object[]{null};

        violations = executableValidator.validateParameters(findEventUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test
    public void findEventById_invalidIdPassed_throwException() throws Exception {
        Method method = FindEventUseCase.class.getMethod("findEventById", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = executableValidator.validateParameters(findEventUseCase, method, parameters);

        assertFalse("Invalid id argument should not pass.", violations.isEmpty());
    }

    @Test(expected = EventNotFoundException.class)
    public void findEventById_unknownIdPassed_throwException() {
        findEventUseCase.findEventById("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void findEventById_validIdPassed_returnEvent() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Event event = findEventUseCase.findEventById(EVENT_ID);

        verify(eventRepository, times(1))
                .getEventById(argumentCaptor.capture());

        List<Object> anyOfLocationOnlineUrl = Arrays.asList(event.getOnlineUrl(), event.getLocation());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(event.getId(), is(EVENT_ID));
        assertThat(event.getName(), is("Angular Connect"));
        assertThat(event.getSessions().size(), is(1));
        assertThat(event.getPrice(), is(1.00));
        assertThat(anyOfLocationOnlineUrl, anyOf(hasItem(notNullValue())));

        // Assert event location
        Location location = event.getLocation();
        if (location != null) {
            assertThat(location.getCountry(), is("United States"));
            assertThat(location.getCity(), is("New York"));
            assertThat(location.getAddress(), is("2695 Frederick Douglass Blvd"));
        }

        // Assert event first session
        Session session = event.getSessions().iterator().next();
        assertThat(session.getId(), is("f50425ee-dca3-4ada-93cc-09993db07311"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getVoters().size(), is(2));
    }
}
