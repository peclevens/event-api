package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.exception.EventNotFoundException;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class DeleteEventUseCaseTest {

    private static final String EVENT_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    private static ValidatorFactory validatorFactory;
    private static ExecutableValidator executableValidator;
    private Set<ConstraintViolation<DeleteEventUseCase>> violations;
    private EventRepository eventRepository;
    private DeleteEventUseCase deleteEventUseCase;

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
        eventRepository = mock(EventRepository.class);
        deleteEventUseCase = new DeleteEventUseCase(eventRepository);

        when(eventRepository.eventExists(EVENT_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        deleteEventUseCase = null;
        violations = null;
    }

    @Test
    public void deleteEventById_nullArgumentPassed_throwException() throws Exception {
        Method method = DeleteEventUseCase.class.getMethod("deleteEventById", String.class);
        Object[] parameters = new Object[]{null};

        violations = executableValidator.validateParameters(deleteEventUseCase, method, parameters);

        assertThat("Null id argument should not pass.", violations.size(), is(1));
    }

    @Test
    public void deleteEventById_invalidIdPassed_throwException() throws Exception {
        Method method = DeleteEventUseCase.class.getMethod("deleteEventById", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = executableValidator.validateParameters(deleteEventUseCase, method, parameters);

        assertThat("Invalid id argument should not pass.", violations.size(), is(1));
    }

    @Test(expected = EventNotFoundException.class)
    public void deleteEventById_unknownIdPassed_throwException() {
        deleteEventUseCase.deleteEventById("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void deleteEventById_validIdPassed_return() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        deleteEventUseCase.deleteEventById(EVENT_ID);

        verify(eventRepository, times(1))
                .eventExists(argumentCaptor.capture());

        verify(eventRepository, times(1))
                .deleteEventById(argumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(EVENT_ID));
    }
}
