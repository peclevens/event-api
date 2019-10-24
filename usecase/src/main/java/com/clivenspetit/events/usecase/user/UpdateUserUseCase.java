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

package com.clivenspetit.events.usecase.user;

import com.clivenspetit.events.domain.user.UpdateUser;
import com.clivenspetit.events.domain.user.User;
import com.clivenspetit.events.domain.user.exception.UserNotFoundException;
import com.clivenspetit.events.domain.user.repository.UserRepository;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author Clivens Petit
 */
public class UpdateUserUseCase {

    private final UserRepository userRepository;

    public UpdateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUser(@UUID(message = "Id should be a valid v4 UUID.") String id,
                           @NotNull @Valid UpdateUser user) {

        User updatedUser = null;

        // Make sure the user exists
        Boolean found = userRepository.userExists(id);
        if (found != null && found) {
            updatedUser = userRepository.updateUser(id, user);
        }

        return Optional.ofNullable(updatedUser)
                .orElseThrow(UserNotFoundException::new);
    }
}
