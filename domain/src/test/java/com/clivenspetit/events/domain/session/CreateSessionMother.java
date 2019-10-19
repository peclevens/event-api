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

package com.clivenspetit.events.domain.session;

import com.clivenspetit.events.domain.common.Level;

import java.time.LocalTime;

/**
 * @author Clivens Petit
 */
public class CreateSessionMother {

    public static CreateSession.Builder validSession() {
        return CreateSession.builder()
                .name("Using Angular 4 Pipes")
                .description("Learn all about the new pipes in Angular 4, both how to write them.")
                .level(Level.BEGINNER)
                .presenter("John Doe")
                .duration(LocalTime.of(1, 0));
    }

    public static CreateSession.Builder invalidSession() {
        return CreateSession.builder()
                .name("A")
                .description("Learn all about it.")
                .level(null)
                .presenter("John/Doe")
                .duration(null);
    }
}
