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

package com.clivenspetit.events.domain.user.login;

/**
 * @author Clivens Petit
 */
public class LoginMother {

    public static Login.Builder validLogin() {
        return Login.builder()
                .email("john.doe@gmail.com")
                .password("Hello123++");
    }

    public static Login.Builder invalidLogin() {
        return Login.builder()
                .email("john.doe")
                .password("");
    }
}
