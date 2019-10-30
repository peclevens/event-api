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

import com.clivenspetit.events.domain.validation.constraints.Email;
import com.clivenspetit.events.domain.validation.constraints.Name;
import com.clivenspetit.events.domain.validation.constraints.Password;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User data class
 *
 * @author Clivens Petit
 */
public class User implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * User id
     */
    @UUID(message = "User id should be a valid v4 UUID.")
    private String id;

    /**
     * The user first name
     */
    @NotBlank(message = "First name is required.")
    @Name(message = "First name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 60, message = "First name should be between {min} and {max} characters.")
    private String firstName;

    /**
     * The user last name
     */
    @NotBlank(message = "Last name is required.")
    @Name(message = "Last name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 60, message = "Last name should be between {min} and {max} characters.")
    private String lastName;

    @Email
    private String email;

    @Password
    private String password;

    private User(User.Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
    }

    public static User.Builder builder() {
        return new User.Builder();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static final class Builder {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        private Builder() {

        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            User user = new User(this);
            user.lastName = this.lastName;
            user.id = this.id;
            user.password = this.password;
            user.firstName = this.firstName;
            user.email = this.email;
            return user;
        }
    }
}
