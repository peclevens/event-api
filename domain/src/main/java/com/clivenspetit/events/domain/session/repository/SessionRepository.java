package com.clivenspetit.events.domain.session.repository;

import com.clivenspetit.events.domain.common.Id;
import com.clivenspetit.events.domain.session.CreateSession;
import com.clivenspetit.events.domain.session.Session;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.validation.Valid;
import java.util.List;

/**
 * Sessions CRUD operations management.
 *
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public interface SessionRepository {

    /**
     * Find session by id.
     *
     * @param id
     * @return
     */
    Session getSessionById(@UUID String id);

    /**
     * Find all sessions by id.
     *
     * @param eventId
     * @return
     */
    List<Session> getSessionsByEventId(@UUID String eventId);

    /**
     * Create new session.
     *
     * @param session
     * @return
     */
    Id createSession(@Valid CreateSession session);

    /**
     * Update existing session.
     *
     * @param id
     * @param session
     * @return
     */
    Session updateSession(@UUID String id, @Valid Session session);

    /**
     * Delete session by id.
     *
     * @param id
     */
    void deleteSessionById(@UUID String id);

    /**
     * Delete all sessions for a specific event.
     *
     * @param eventId
     */
    void deleteAllSessionsByEventId(@UUID String eventId);

    /**
     * Upvote session by id.
     *
     * @param id
     */
    void upVoteSession(@UUID String id);

    /**
     * Downvote session by id.
     *
     * @param id
     */
    void downVoteSession(@UUID String id);
}
