/*
 * Copyright 2019 MAGIC SOFTWARE BAY, SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
