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

package com.clivenspetit.events.data.user.entity;

import com.clivenspetit.events.domain.validation.constraints.Email;
import com.clivenspetit.events.domain.validation.constraints.Name;
import com.clivenspetit.events.domain.validation.constraints.Password;
import com.clivenspetit.events.domain.validation.constraints.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Clivens Petit
 */
@Entity
@Table(name = "user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 0L;

    /**
     * User id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * User external id
     */
    @UUID(message = "User id should be a valid v4 UUID.")
    @Column(name = "user_id")
    private String userId;

    /**
     * The user first name
     */
    @NotBlank(message = "First name is required.")
    @Name(message = "First name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 60, message = "First name should be between {min} and {max} characters.")
    @Column(name = "first_name")
    private String firstName;

    /**
     * The user last name
     */
    @NotBlank(message = "Last name is required.")
    @Name(message = "Last name should contain only characters from a-zA-Z and symbols like ',. -")
    @Size(min = 1, max = 60, message = "Last name should be between {min} and {max} characters.")
    @Column(name = "last_name")
    private String lastName;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Password
    @Column(name = "password", nullable = false)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PrePersist
    public void prePersist() {
        userId = java.util.UUID.randomUUID().toString();
    }
}
