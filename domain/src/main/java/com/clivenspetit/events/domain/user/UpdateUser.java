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

import com.clivenspetit.events.domain.validation.constraints.Name;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Update user data class
 *
 * @author Clivens Petit
 */
public class UpdateUser implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * The user first name
     */
    @Name(message = "First name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 60, message = "First name should be between {min} and {max} characters.")
    private String firstName;

    /**
     * The user last name
     */
    @Name(message = "Last name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 60, message = "Last name should be between {min} and {max} characters.")
    private String lastName;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9!#$%&()*+,.;<=>?@^_~]{8,80}$",
            message = "Password should be between 8 to 80 characters. Only letters, numbers and optionally special " +
                    "characters (!#$%&()*+,.;<=>?@^_~) are allowed.")
    @Size(max = 80, message = "Password should be {max} characters long.")
    private String password;

    private UpdateUser(UpdateUser.Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.password = builder.password;
    }

    public static UpdateUser.Builder builder() {
        return new UpdateUser.Builder();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public static final class Builder {
        private String firstName;
        private String lastName;
        private String password;

        private Builder() {

        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public UpdateUser build() {
            return new UpdateUser(this);
        }
    }
}
