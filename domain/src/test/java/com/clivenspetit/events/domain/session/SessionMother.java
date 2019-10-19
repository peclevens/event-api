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
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * @author Clivens Petit
 */
public class SessionMother {

    public static Session.Builder validSession() {
        return Session.builder()
                .version(1)
                .id("f50425ee-dca3-4ada-93cc-09993db07311")
                .name("Using Angular 4 Pipes")
                .description("Learn all about the new pipes in Angular 4, both how to write them.")
                .level(Level.BEGINNER)
                .presenter("John Doe")
                .duration(LocalTime.of(1, 0))
                .voters(new LinkedHashSet<>(Arrays.asList("johnpapa", "bradgreen")));
    }

    public static Session.Builder invalidSession() {
        return Session.builder()
                .version(-1)
                .id("f50425ee")
                .name("A")
                .description("Learn all about it.")
                .level(null)
                .presenter("John/Doe")
                .duration(null)
                .voters(new LinkedHashSet<>(Arrays.asList("voter/1", "voter2")));
    }
}
