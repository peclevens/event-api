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

import com.clivenspetit.events.domain.validation.constraints.Email;
import com.clivenspetit.events.domain.validation.constraints.Password;

import java.io.Serializable;

/**
 * Login data class
 *
 * @author Clivens Petit
 */
public class Login implements Serializable {

    private static final long serialVersionUID = 0L;

    @Email
    private String email;

    @Password
    private String password;

    private Login(Login.Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
    }

    public static Login.Builder builder() {
        return new Login.Builder();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static final class Builder {
        private String email;
        private String password;

        private Builder() {

        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Login build() {
            Login user = new Login(this);
            user.password = this.password;
            user.email = this.email;
            return user;
        }
    }
}
