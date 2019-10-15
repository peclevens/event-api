package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Sort;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class FindSessionsUseCaseTest {

    private static final String UNKNOWN_QUERY = "name=test";
    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";
    private static final Sort sort = Sort.by(Sort.Direction.ASC, "level", "duration");

    private static ValidatorFactory validatorFactory;
    private static ExecutableValidator executableValidator;
    private Set<ConstraintViolation<FindSessionsUseCase>> violations;
    private SessionRepository sessionRepository;
    private FindSessionsUseCase findSessionsUseCase;
    private List<Session> sessions;

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
        Session session = new Session();
        session.setVersion(1);
        session.setId("f50425ee-dca3-4ada-93cc-09993db07311");
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));
        session.setVoters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));

        sessionRepository = mock(SessionRepository.class);
        findSessionsUseCase = new FindSessionsUseCase(sessionRepository);

        sessions = Collections.singletonList(session);

        when(sessionRepository.getSessionsByEventId(EVENT_ID, null, sort))
                .thenReturn(sessions);

        when(sessionRepository.getSessionsByEventId(EVENT_ID, UNKNOWN_QUERY, sort))
                .thenReturn(new ArrayList<>());
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        findSessionsUseCase = null;
        sessions = null;
        violations = null;
    }

    @Test
    public void findSessionsByEventId_unknownQueryArgumentPassed_returnEmptySession() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Sort> argumentCaptorSort = ArgumentCaptor.forClass(Sort.class);

        List<Session> sessionList = findSessionsUseCase.findSessionsByEventId(EVENT_ID, UNKNOWN_QUERY, sort);

        verify(sessionRepository, times(1))
                .getSessionsByEventId(argumentCaptor.capture(), argumentCaptor.capture(), argumentCaptorSort.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(UNKNOWN_QUERY));
        assertThat(argumentCaptorSort.getAllValues().get(0), is(sort));

        assertTrue("No sessions should return for invalid query.", sessionList.isEmpty());
    }

    @Test
    public void findSessionsByEventId_validArgumentPassed_returnSessions() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Sort> argumentCaptorSort = ArgumentCaptor.forClass(Sort.class);

        List<Session> sessionList = findSessionsUseCase.findSessionsByEventId(EVENT_ID, null, sort);

        verify(sessionRepository, times(1))
                .getSessionsByEventId(argumentCaptor.capture(), argumentCaptor.capture(), argumentCaptorSort.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(EVENT_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(nullValue()));
        assertThat(argumentCaptorSort.getAllValues().get(0), is(sort));

        // Assert event first foundSession
        Session session = sessionList.get(0);
        Session foundSession = sessionList.get(0);
        assertThat(foundSession.getId(), is(session.getId()));
        assertThat(foundSession.getLevel(), is(session.getLevel()));
        assertThat(foundSession.getVoters().size(), is(session.getVoters().size()));
    }
}
