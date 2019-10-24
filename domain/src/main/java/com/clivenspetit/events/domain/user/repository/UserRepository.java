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

package com.clivenspetit.events.domain.user.repository;

import com.clivenspetit.events.domain.user.UpdateUser;
import com.clivenspetit.events.domain.user.User;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * User management class.
 *
 * @author Clivens Petit
 */
public interface UserRepository {

    /**
     * Find user by id.
     *
     * @param id The user id.
     * @return The user matching the id passed.
     */
    User getUserById(@UUID(message = "User id should be a valid v4 UUID.") String id);

    /**
     * Find user by email.
     *
     * @param email The user email.
     * @return The user matching the email passed.
     */
    User getUserEmail(@Email String email);

    /**
     * Find out whether a user exists or not by id.
     *
     * @param id The user id.
     * @return Return true if exists, false otherwise.
     */
    Boolean userExists(@UUID(message = "User id should be a valid v4 UUID.") String id);

    /**
     * Update existing user.
     *
     * @param id   The user id.
     * @param user A modified user object to store to a datasource.
     * @return The newly updated user.
     */
    User updateUser(@UUID String id, @NotNull @Valid UpdateUser user);
}
