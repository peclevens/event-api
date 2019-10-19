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

package com.clivenspetit.events.domain.event;

import com.clivenspetit.events.domain.common.LocationMother;
import com.clivenspetit.events.domain.session.SessionMother;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Clivens Petit
 */
public class EventMother {

    public static Event.Builder validEvent() {
        return Event.builder()
                .id("eb3a377c-3742-43ac-8d87-35534de2db8f")
                .version(3)
                .name("Angular Connect")
                .imageUrl("http://localhost/images/angularconnect-shield.png")
                .onlineUrl("https://hangouts.google.com")
                .location(LocationMother.validLocation().build())
                .price(1.00)
                .startDate(LocalDateTime.of(2036, 9, 26, 10, 0, 0))
                .sessions(Collections.singleton(SessionMother.validSession().build()));
    }

    public static Event.Builder invalidEvent() {
        return Event.builder()
                .id("eb3a377c-3742-43ac-8d87")
                .version(-2)
                .name("E")
                .imageUrl("localhost/images/angularconnect-shield.png")
                .onlineUrl("hangouts.google.com")
                .location(LocationMother.allFieldNullLocation().build())
                .price(1.00)
                .startDate(LocalDateTime.of(2018, 9, 26, 10, 0, 0))
                .sessions(Collections.singleton(SessionMother.invalidSession().build()));
    }
}
