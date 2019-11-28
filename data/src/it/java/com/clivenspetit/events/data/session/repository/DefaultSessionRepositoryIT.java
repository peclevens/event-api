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

package com.clivenspetit.events.data.session.repository;

import com.clivenspetit.events.data.event.repository.JpaEventRepository;
import com.clivenspetit.events.data.session.mapper.SessionMapper;
import com.clivenspetit.events.data.user.repository.JpaUserRepository;
import com.clivenspetit.events.domain.common.Level;
import com.clivenspetit.events.domain.session.*;
import com.clivenspetit.events.domain.session.repository.SessionRepository;
import org.hamcrest.collection.IsEmptyCollection;
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

import static com.clivenspetit.events.data.session.repository.DefaultSessionRepository.SESSION_CACHE_KEY_TPL;
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
public class DefaultSessionRepositoryIT {

    private static final String EVENT_ID = "eb3a377c-3742-43ac-8d87-35534de2db8f";
    private static final String SESSION_ID = "f50425ee-dca3-4ada-93cc-09993db07311";
    private static final String USER_ID = "2cb4601f-bd11-4d01-98f5-b8a249e2b0ed";

    private static CacheManager cacheManager;
    private static MutableConfiguration<String, Session> sessionMutableConfiguration = new MutableConfiguration<>();
    private static Cache<String, Session> sessionCache;

    @Autowired
    private JpaSessionRepository jpaSessionRepository;

    @Autowired
    private JpaEventRepository jpaEventRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaSessionVoteRepository jpaSessionVoteRepository;

    private SessionRepository sessionRepository;

    @BeforeClass
    public static void beforeClass() throws Throwable {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();

        sessionCache = cacheManager.createCache("sessionCache2", sessionMutableConfiguration);
    }

    @AfterClass
    public static void afterClass() {
        cacheManager.close();
    }

    @Before
    public void setUp() throws Exception {
        sessionRepository = new DefaultSessionRepository(jpaSessionRepository, jpaEventRepository,
                jpaUserRepository, jpaSessionVoteRepository, sessionCache, SessionMapper.INSTANCE);
    }

    @After
    public void tearDown() throws Exception {
        sessionRepository = null;
        sessionCache.clear();
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void getSessionById_validIdPassed_returnSession() {
        String cacheKey = String.format(SESSION_CACHE_KEY_TPL, "*", SESSION_ID);

        // Find session in database
        Session session = sessionRepository.getSessionById(SESSION_ID);

        assertThat(session, is(notNullValue()));
        assertTrue("Session should be in the cache.", sessionCache.containsKey(cacheKey));

        // Session should be found in cache
        session = sessionRepository.getSessionById(SESSION_ID);

        assertThat(session, is(notNullValue()));
        assertThat(SESSION_ID, is(session.getId()));
        assertThat(session.getName(), is("Using Angular 4 Pipes"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getPresenter(), is("John Doe"));
        assertThat(session.getVoters(), IsEmptyCollection.empty());
    }

    @Test
    public void getSessionById_unknownIdPassed_returnNull() {
        Session session = sessionRepository.getSessionById(UUID.randomUUID().toString());

        assertThat(session, is(nullValue()));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void sessionExists_validIdPassed_returnTrue() {
        Boolean exists = sessionRepository.sessionExists(SESSION_ID);

        assertThat(exists, is(notNullValue()));
        assertTrue("Session should exist", exists);
    }

    @Test
    public void sessionExists_invalidIdPassed_returnFalse() {
        Boolean exists = sessionRepository.sessionExists(SESSION_ID);

        assertThat(exists, is(notNullValue()));
        assertFalse("Session should not exist", exists);
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-user.sql"),
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql")
    })
    public void createSession_sessionPassed_returnNewSessionId() {
        CreateSession createSession = CreateSessionMother.validSession().build();

        // Create the session
        String id = sessionRepository.createSession(EVENT_ID, createSession);

        assertThat(id, is(notNullValue()));
        assertThat(id, instanceOf(String.class));

        // Find the session
        Session session = sessionRepository.getSessionById(id);

        assertThat(session, is(notNullValue()));
        assertThat(session.getName(), is("Using Angular 4 Pipes"));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getPresenter(), is("John Doe"));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-user.sql"),
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void updateSession_sessionNameUpdated_returnUpdatedSession() {
        String sessionName = "New session name";

        // Modify session
        UpdateSession modifiedSession = UpdateSessionMother.validSession()
                .name(sessionName)
                .build();

        Session session = sessionRepository.updateSession(SESSION_ID, modifiedSession);

        assertThat(session, is(notNullValue()));
        assertThat(session.getName(), is(sessionName));
        assertThat(session.getLevel(), is(Level.BEGINNER));
        assertThat(session.getPresenter(), is("John Doe"));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void deleteSessionById_validSessionIdPassed_completed() {
        // Delete session
        sessionRepository.deleteSessionById(SESSION_ID);

        assertThat(jpaSessionRepository.count(), is(0L));
    }

    @Test
    @Sql("classpath:db/sample/create-event.sql")
    public void deleteSessionById_invalidSessionIdPassed_completed() {
        // Delete session
        sessionRepository.deleteSessionById(UUID.randomUUID().toString());

        // Process new count
        assertThat(jpaSessionRepository.count(), is(0L));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void deleteAllSessionsByEventId_validIdPassed_completed() {
        // Process old count
        assertThat(jpaSessionRepository.count(), is(1L));

        // Delete session
        sessionRepository.deleteAllSessionsByEventId(EVENT_ID);

        // Process new count
        assertThat(jpaSessionRepository.count(), is(0L));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-user.sql"),
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void upVoteSession_validSessionIdPassed_completed() {
        // Process old count
        assertThat(jpaSessionVoteRepository.count(), is(0L));

        // Upvote session
        sessionRepository.upVoteSession(SESSION_ID, USER_ID);

        // Process new count
        assertThat(jpaSessionVoteRepository.count(), is(1L));
    }

    @Test
    @SqlGroup({
            @Sql("classpath:db/sample/create-user.sql"),
            @Sql("classpath:db/sample/create-event.sql"),
            @Sql("classpath:db/sample/create-location.sql"),
            @Sql("classpath:db/sample/create-session.sql")
    })
    public void downVoteSession_validSessionIdPassed_completed() {
        // Process old count
        assertThat(jpaSessionVoteRepository.count(), is(0L));

        // Upvote session
        sessionRepository.upVoteSession(SESSION_ID, USER_ID);

        // Process new count
        assertThat(jpaSessionVoteRepository.count(), is(1L));

        // Upvote session
        sessionRepository.downVoteSession(SESSION_ID, USER_ID);

        // Process new count
        assertThat(jpaSessionVoteRepository.count(), is(0L));
    }
}
