package com.clivenspetit.events.usecase.session;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.exception.SessionNotFoundException;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class FindSessionUseCaseTest {

    private static final String SESSION_ID = "f50425ee-dca3-4ada-93cc-09993db07311";

    private SessionRepository sessionRepository;
    private FindSessionUseCase findSessionUseCase;
    private Session session;

    @Before
    public void setUp() throws Exception {
        session = new Session();
        session.setVersion(1);
        session.setId(SESSION_ID);
        session.setName("Using Angular 4 Pipes");
        session.setDescription("Learn all about the new pipes in Angular 4, both how to write them.");
        session.setLevel(Level.BEGINNER);
        session.setPresenter("John Doe");
        session.setDuration(LocalTime.of(1, 0));
        session.setVoters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));

        sessionRepository = mock(SessionRepository.class);
        findSessionUseCase = new FindSessionUseCase(sessionRepository);

        when(sessionRepository.getSessionById(SESSION_ID))
                .thenReturn(session);
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        findSessionUseCase = null;
        session = null;
    }

    @Test
    public void findSessionById_nullArgumentPassed_throwException() {

    }

    @Test(expected = SessionNotFoundException.class)
    public void findSessionById_invalidIdPassed_throwException() {
        findSessionUseCase.findSessionById("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void findSessionById_validIdPassed_returnSession() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Session session = findSessionUseCase.findSessionById(SESSION_ID);

        verify(sessionRepository, times(1))
                .getSessionById(argumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(SESSION_ID));
        assertThat(session.getId(), is(SESSION_ID));
        assertThat(session.getVoters().size(), is(2));
        assertThat(session.getLevel(), is(Level.BEGINNER));
    }
}
