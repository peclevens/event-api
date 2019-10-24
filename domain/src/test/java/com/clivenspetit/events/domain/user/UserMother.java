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

package com.clivenspetit.events.domain.user;

/**
 * @author Clivens Petit
 */
public class UserMother {

    public static User.Builder validUser() {
        return User.builder()
                .id("2cb4601f-bd11-4d01-98f5-b8a249e2b0ed")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .password("Hello123++");
    }

    public static User.Builder invalidUser() {
        return User.builder()
                .id("2cb4601f-bd11-4d01-98f5")
                .firstName("Jo*hn")
                .lastName(null)
                .email("john.doe")
                .password("");
    }
}
