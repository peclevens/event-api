package com.clivenspetit.events.usecase.event;

import com.clivenspetit.events.domain.event.repository.EventRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class DeleteEventsUseCaseTest {

    private EventRepository eventRepository;
    private DeleteEventsUseCase deleteEventsUseCase;

    @Before
    public void setUp() throws Exception {
        eventRepository = mock(EventRepository.class);
        deleteEventsUseCase = new DeleteEventsUseCase(eventRepository);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        deleteEventsUseCase = null;
    }

    @Test
    public void deleteAllEvents_allEventDeleted() {
        deleteEventsUseCase.deleteAllEvents();

        verify(eventRepository, times(1))
                .deleteAllEvents();
    }
}
