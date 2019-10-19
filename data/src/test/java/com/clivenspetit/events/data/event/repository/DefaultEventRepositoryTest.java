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

import com.clivenspetit.events.data.event.entity.EventEntity;
import com.clivenspetit.events.data.event.mapper.EventMapper;
import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.repository.EventRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import static com.clivenspetit.events.data.event.repository.DefaultEventRepository.EVENT_CACHE_KEY_TPL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Clivens Petit
 */
@DataJpaTest
@RunWith(SpringRunner.class)
public class DefaultEventRepositoryTest {

    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";

    private static CacheManager cacheManager;
    private static MutableConfiguration<String, Event> eventMutableConfiguration = new MutableConfiguration<>();
    private static Cache<String, Event> eventCache;

    @Autowired
    private JpaEventRepository jpaEventRepository;

    private EventRepository eventRepository;

    @BeforeClass
    public static void beforeClass() throws Throwable {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();

        eventCache = cacheManager.createCache("eventCache", eventMutableConfiguration);
    }

    @AfterClass
    public static void afterClass() {
        cacheManager.close();
    }

    @Before
    public void setUp() throws Exception {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventId(EVENT_ID);
        eventEntity.setName("Angular Connect");

        eventRepository = new DefaultEventRepository(jpaEventRepository, eventCache, EventMapper.INSTANCE);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getEventById() {
        String cacheKey = String.format(EVENT_CACHE_KEY_TPL, EVENT_ID);

        Event event = eventRepository.getEventById(EVENT_ID);

        assertThat(event, is(notNullValue()));
        assertThat(EVENT_ID, is(event.getId()));
        assertTrue("Event should be in the cache.", eventCache.containsKey(cacheKey));
    }

    @Test
    public void eventExists() {

    }

    @Test
    public void getAllEvents() {

    }

    @Test
    public void createEvent() {

    }

    @Test
    public void updateEvent() {

    }

    @Test
    public void deleteEventById() {

    }

    @Test
    public void deleteAllEvents() {

    }
}
