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

package com.clivenspetit.events.data.event.repository;

import com.clivenspetit.events.data.event.mapper.EventMapper;
import com.clivenspetit.events.data.session.mapper.SessionMapper;
import com.clivenspetit.events.data.session.repository.DefaultSessionRepository;
import com.clivenspetit.events.data.session.repository.JpaSessionRepository;
import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.common.LocationMother;
import com.clivenspetit.events.domain.event.*;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.util.UUID;

import static com.clivenspetit.events.data.event.repository.DefaultEventRepository.EVENT_CACHE_KEY_TPL;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Clivens Petit
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class DefaultEventRepositoryIT {

    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";

    private static CacheManager cacheManager;
    private static MutableConfiguration<String, Event> eventMutableConfiguration = new MutableConfiguration<>();
    private static MutableConfiguration<String, Session> sessionMutableConfiguration = new MutableConfiguration<>();
    private static Cache<String, Event> eventCache;
    private static Cache<String, Session> sessionCache;

    @Autowired
    private JpaEventRepository jpaEventRepository;

    @Autowired
    private JpaSessionRepository jpaSessionRepository;

    private EventRepository eventRepository;
    private SessionRepository sessionRepository;

    @BeforeClass
    public static void beforeClass() throws Throwable {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();

        eventCache = cacheManager.createCache("eventCache", eventMutableConfiguration);
        sessionCache = cacheManager.createCache("sessionCache", sessionMutableConfiguration);
    }

    @AfterClass
    public static void afterClass() {
        cacheManager.close();
    }

    @Before
    public void setUp() throws Exception {
        sessionRepository = new DefaultSessionRepository(jpaSessionRepository, jpaEventRepository, sessionCache, SessionMapper.INSTANCE);

        eventRepository = new DefaultEventRepository(jpaEventRepository, sessionRepository, eventCache,
                EventMapper.INSTANCE);
    }

    @After
    public void tearDown() throws Exception {
        eventRepository = null;
        sessionCache.clear();
        eventCache.clear();
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void getEventById_validIdPassed_returnEvent() {
        String cacheKey = String.format(EVENT_CACHE_KEY_TPL, EVENT_ID);

        // Find event in database
        Event event = eventRepository.getEventById(EVENT_ID);

        assertThat(event, is(notNullValue()));
        assertTrue("Event should be in the cache.", eventCache.containsKey(cacheKey));

        // Event should be found in cache
        event = eventRepository.getEventById(EVENT_ID);

        assertThat(event, is(notNullValue()));

        Session session = event.getSessions().iterator().next();

        assertThat(session, is(notNullValue()));
        assertThat(EVENT_ID, is(event.getId()));
        assertThat(event.getLocation().getCountry(), is("United States"));
        assertThat(event.getLocation().getCity(), is("New York"));
        assertThat(session.getName(), is("Using Angular 4 Pipes"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getPresenter(), is("John Doe"));
        assertThat(session.getVoters(), hasItems("johnpapa", "bradgreen"));
    }

    @Test
    public void getEventById_unknownIdPassed_returnNull() {
        Event event = eventRepository.getEventById(UUID.randomUUID().toString());

        assertThat(event, is(nullValue()));
    }

    @Test
    @Sql("classpath:db/sample/create-event.sql")
    public void eventExists_validIdPassed_returnTrue() {
        Boolean exists = eventRepository.eventExists(EVENT_ID);

        assertThat(exists, is(notNullValue()));
        assertTrue("Event should exist", exists);
    }

    @Test
    public void eventExists_invalidIdPassed_returnFalse() {
        Boolean exists = eventRepository.eventExists(EVENT_ID);

        assertThat(exists, is(notNullValue()));
        assertFalse("Event should not exist", exists);
    }

    @Test
    public void getAllEvents() {

    }

    @Test
    public void createEvent_eventWithSessionWithLocationAndOnlineUrl_returnNewEventId() {
        CreateEvent createEvent = CreateEventMother.validEvent().build();

        // Create the event
        String id = eventRepository.createEvent(createEvent);

        assertThat(id, is(notNullValue()));
        assertThat(id, instanceOf(String.class));

        // Find the event
        Event event = eventRepository.getEventById(id);

        assertThat(event, is(notNullValue()));

        Session session = event.getSessions().iterator().next();

        assertThat(session, is(notNullValue()));
        assertThat(event.getLocation().getCountry(), is("United States"));
        assertThat(event.getLocation().getCity(), is("New York"));
        assertThat(session.getName(), is("Using Angular 4 Pipes"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getPresenter(), is("John Doe"));
    }

    @Test
    public void createEvent_eventWithLocation_returnNewEventId() {
        CreateEvent createEvent = CreateEventMother.validEvent()
                .onlineUrl(null)
                .build();

        // Create the event
        String id = eventRepository.createEvent(createEvent);

        assertThat(id, is(notNullValue()));
        assertThat(id, instanceOf(String.class));

        // Find the event
        Event event = eventRepository.getEventById(id);

        assertThat(event, is(notNullValue()));

        Session session = event.getSessions().iterator().next();

        assertThat(session, is(notNullValue()));
        assertThat(event.getLocation().getCountry(), is("United States"));
        assertThat(event.getLocation().getCity(), is("New York"));
        assertThat(session.getName(), is("Using Angular 4 Pipes"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getPresenter(), is("John Doe"));
    }

    @Test
    public void createEvent_eventWithOnlineUrl_returnNewEventId() {
        CreateEvent createEvent = CreateEventMother.validEvent()
                .location(null)
                .build();

        // Create the event
        String id = eventRepository.createEvent(createEvent);

        assertThat(id, is(notNullValue()));
        assertThat(id, instanceOf(String.class));

        // Find the event
        Event event = eventRepository.getEventById(id);

        assertThat(event, is(notNullValue()));

        Session session = event.getSessions().iterator().next();

        assertThat(session, is(notNullValue()));
        assertThat(session.getName(), is("Using Angular 4 Pipes"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getPresenter(), is("John Doe"));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void updateEvent_eventNameAndLocationCityUpdated_returnUpdatedEvent() {
        String eventName = "New event name";
        String locationCity = "New location city";

        // Modify event
        UpdateEvent modifiedEvent = UpdateEventMother.validEvent()
                .name(eventName)
                .location(LocationMother.validLocation()
                        .city(locationCity)
                        .build())
                .build();

        Event updatedEvent = eventRepository.updateEvent(EVENT_ID, modifiedEvent);

        Session session = updatedEvent.getSessions().iterator().next();

        assertThat(session, is(notNullValue()));
        assertThat(updatedEvent.getName(), is(eventName));
        assertThat(updatedEvent.getLocation().getCountry(), is("United States"));
        assertThat(updatedEvent.getLocation().getCity(), is(locationCity));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getPresenter(), is("John Doe"));
    }

    @Test
    @Sql("classpath:db/sample/create-event.sql")
    public void updateEvent_updateEventNameAndAddNewLocation_returnUpdatedEvent() {
        String eventName = "New event name";
        String locationCity = "New location city";

        // Modify event
        UpdateEvent modifiedEvent = UpdateEventMother.validEvent()
                .name(eventName)
                .location(LocationMother.validLocation()
                        .city(locationCity)
                        .build())
                .build();

        Event updatedEvent = eventRepository.updateEvent(EVENT_ID, modifiedEvent);

        assertThat(updatedEvent.getName(), is(eventName));
        assertThat(updatedEvent.getLocation().getCountry(), is("United States"));
        assertThat(updatedEvent.getLocation().getCity(), is(locationCity));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void deleteEventById_eventWithSessionAndLocation_completed() {
        // Delete event
        eventRepository.deleteEventById(EVENT_ID);

        assertThat(jpaEventRepository.count(), is(0L));
        assertThat(jpaSessionRepository.count(), is(0L));
    }

    @Test
    @Sql("classpath:db/sample/create-event.sql")
    public void deleteEventById_eventWithoutSessionAndLocation_completed() {
        // Delete event
        eventRepository.deleteEventById(EVENT_ID);

        assertThat(jpaEventRepository.count(), is(0L));
        assertThat(jpaSessionRepository.count(), is(0L));
    }

    @Test
    @Sql("classpath:db/sample/create-event.sql")
    public void deleteEventById_eventNotFound_completed() {
        // Delete event
        eventRepository.deleteEventById(UUID.randomUUID().toString());

        // Process new count
        assertThat(jpaEventRepository.count(), is(1L));
        assertThat(jpaSessionRepository.count(), is(0L));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-event2.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void deleteAllEvents() {
        // Process new count
        assertThat(jpaEventRepository.count(), is(2L));

        // Delete event
        eventRepository.deleteAllEvents();

        // Process new count
        assertThat(jpaEventRepository.count(), is(0L));
    }
}
