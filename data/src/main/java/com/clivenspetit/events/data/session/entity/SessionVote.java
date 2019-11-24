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

package com.clivenspetit.events.data.session.entity;

import com.clivenspetit.events.data.user.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Clivens Petit
 */
@Entity
@Table(name = "user_session_votes")
public class SessionVote implements Serializable {

    private static final long serialVersionUID = 0L;

    @Id
    private SessionVotePk id = new SessionVotePk();

    public SessionVote() {

    }

    public SessionVote(UserEntity userId, SessionEntity sessionId) {
        id.setUserId(userId);
        id.setSessionId(sessionId);
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public SessionVotePk getId() {
        return id;
    }

    public void setId(SessionVotePk id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
