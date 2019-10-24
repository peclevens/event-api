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

package com.clivenspetit.events.usecase.user.login;

import com.clivenspetit.events.domain.user.User;
import com.clivenspetit.events.domain.user.login.Login;
import com.clivenspetit.events.domain.user.login.exception.InvalidCredentialsException;
import com.clivenspetit.events.domain.user.repository.UserRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Clivens Petit
 */
public class LoginUseCase {

    private final UserRepository userRepository;

    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(@NotNull @Valid Login login) {
        final User user = userRepository.getUserEmail(login.getEmail());

        // Validate the user exists
        if (user == null) throw new InvalidCredentialsException();

        // Validate password matches


        return user;
    }
}
