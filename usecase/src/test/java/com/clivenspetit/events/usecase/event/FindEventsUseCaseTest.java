package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.Location;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.usecase.DataStubResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class FindEventsUseCaseTest {

    private static final String UNKNOWN_QUERY = "name=test";
    private static final Sort sort = Sort.by(Sort.Direction.ASC, "name", "startDate");
    private static final Pageable pageable = PageRequest.of(0, 25, sort);

    @Rule
    public DataStubResource stubResource = new DataStubResource();

    private EventRepository eventRepository;
    private FindEventsUseCase findEventsUseCase;

    @Before
    public void setUp() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2036, 9, 26,
                10, 0, 0);

        eventRepository = mock(EventRepository.class);
        findEventsUseCase = new FindEventsUseCase(eventRepository);

        when(eventRepository.getAllEvents(null, pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(stubResource.event)));

        when(eventRepository.getAllEvents(UNKNOWN_QUERY, pageable))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        findEventsUseCase = null;
    }

    @Test
    public void findAllEvents_unknownQueryArgumentPassed_returnEmptyEvent() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Pageable> argumentCaptorPage = ArgumentCaptor.forClass(Pageable.class);

        Page<Event> eventPage = findEventsUseCase.findAllEvents(UNKNOWN_QUERY, pageable);

        verify(eventRepository, times(1))
                .getAllEvents(argumentCaptor.capture(), argumentCaptorPage.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(UNKNOWN_QUERY));
        assertThat(argumentCaptorPage.getAllValues().get(0), is(pageable));
        assertTrue("No event page should return for invalid query.", eventPage.getContent().isEmpty());
    }

    @Test
    public void findEventById_validIdPassed_returnEvent() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Pageable> argumentCaptorPage = ArgumentCaptor.forClass(Pageable.class);

        Page<Event> eventPage = findEventsUseCase.findAllEvents(null, pageable);

        verify(eventRepository, times(1))
                .getAllEvents(argumentCaptor.capture(), argumentCaptorPage.capture());

        assertThat(eventPage.getContent().size(), is(1));

        // Assert first found event
        Event foundEvent = eventPage.getContent().get(0);

        List<Object> anyOfLocationOnlineUrl = Arrays.asList(foundEvent.getOnlineUrl(), foundEvent.getLocation());

        assertThat(argumentCaptor.getAllValues().get(0), is(nullValue()));
        assertThat(foundEvent.getId(), is(stubResource.event.getId()));
        assertThat(foundEvent.getName(), is("Angular Connect"));
        assertThat(foundEvent.getSessions().size(), is(1));
        assertThat(foundEvent.getPrice(), is(1.00));
        assertThat(anyOfLocationOnlineUrl, anyOf(hasItem(notNullValue())));

        // Assert event location
        Location location = foundEvent.getLocation();
        if (location != null) {
            assertThat(location.getCountry(), is("United States"));
            assertThat(location.getCity(), is("New York"));
            assertThat(location.getAddress(), is("2695 Frederick Douglass Blvd"));
        }

        // Assert event first session
        Session session = foundEvent.getSessions().iterator().next();
        assertThat(session.getId(), is("f50425ee-dca3-4ada-93cc-09993db07311"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getVoters().size(), is(2));
    }
}
