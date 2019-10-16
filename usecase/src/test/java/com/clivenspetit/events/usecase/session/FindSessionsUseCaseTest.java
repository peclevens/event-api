package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import com.clivenspetit.events.usecase.DataStubResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Rule
    public DataStubResource stubResource = new DataStubResource();

    private SessionRepository sessionRepository;
    private FindSessionsUseCase findSessionsUseCase;
    private List<Session> sessions;

    @Before
    public void setUp() throws Exception {
        sessionRepository = mock(SessionRepository.class);
        findSessionsUseCase = new FindSessionsUseCase(sessionRepository);

        sessions = Collections.singletonList(stubResource.session);

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
