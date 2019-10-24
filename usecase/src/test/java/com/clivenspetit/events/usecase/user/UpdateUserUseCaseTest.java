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

import com.clivenspetit.events.domain.ValidationResource;
import com.clivenspetit.events.domain.user.UpdateUser;
import com.clivenspetit.events.domain.user.UpdateUserMother;
import com.clivenspetit.events.domain.user.User;
import com.clivenspetit.events.domain.user.UserMother;
import com.clivenspetit.events.domain.user.exception.UserNotFoundException;
import com.clivenspetit.events.domain.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Method;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class UpdateUserUseCaseTest {

    private static final String USER_ID = "2cb4601f-bd11-4d01-98f5-b8a249e2b0ed";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<UpdateUserUseCase>> violations;
    private UserRepository userRepository;
    private UpdateUserUseCase updateUserUseCase;
    private User user;
    private UpdateUser modifiedUser;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        updateUserUseCase = new UpdateUserUseCase(userRepository);

        user = UserMother.validUser()
                .firstName("Jane")
                .lastName("Peter")
                .build();

        modifiedUser = UpdateUserMother.validUser()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        when(userRepository.updateUser(USER_ID, modifiedUser))
                .thenReturn(user);

        when(userRepository.userExists(USER_ID))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        updateUserUseCase = null;
        violations = null;
    }

    @Test
    public void updateUser_nullArgumentPassed_throwException() throws Exception {
        Method method = UpdateUserUseCase.class.getMethod("updateUser", String.class, UpdateUser.class);
        Object[] parameters = new Object[]{null, null};

        violations = validationResource.executableValidator.validateParameters(updateUserUseCase,
                method, parameters);

        assertThat("Null arguments should not pass.", violations.size(), is(2));
    }

    @Test
    public void updateUser_invalidArgumentPassed_throwException() throws Exception {
        Method method = UpdateUserUseCase.class.getMethod("updateUser", String.class, UpdateUser.class);
        Object[] parameters = new Object[]{"id", UpdateUserMother.invalidUser().build()};

        violations = validationResource.executableValidator.validateParameters(updateUserUseCase,
                method, parameters);

        assertThat("Invalid arguments should not pass.", violations, hasSize(3));
    }

    @Test(expected = UserNotFoundException.class)
    public void updateUser_unknownIdPassed_throwException() {
        updateUserUseCase.updateUser("cd4c770a-e53c-4d19-8393-3b37ec811b66", modifiedUser);
    }

    @Test
    public void updateUser_invalidModifiedUserPassed_throwException() throws Exception {
        UpdateUser updateUser = UpdateUserMother.invalidUser().build();

        Method method = UpdateUserUseCase.class.getMethod("updateUser", String.class, UpdateUser.class);
        Object[] parameters = new Object[]{USER_ID, updateUser};

        violations = validationResource.executableValidator.validateParameters(updateUserUseCase,
                method, parameters);

        assertThat("Invalid arguments should not pass.", violations, hasSize(2));
    }

    @Test
    public void updateUser_validModifiedUserPassed_returnUpdatedUser() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UpdateUser> userArgumentCaptor = ArgumentCaptor.forClass(UpdateUser.class);

        User updatedUser = updateUserUseCase.updateUser(USER_ID, modifiedUser);

        verify(userRepository, times(1))
                .userExists(argumentCaptor.capture());

        verify(userRepository, times(1))
                .updateUser(argumentCaptor.capture(), userArgumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(USER_ID));
        assertThat(argumentCaptor.getAllValues().get(1), is(USER_ID));
        assertThat(userArgumentCaptor.getAllValues().get(0).getFirstName(), is(user.getFirstName()));
        assertThat(updatedUser.getFirstName(), is(user.getFirstName()));
        assertThat(updatedUser.getLastName(), is(user.getLastName()));
    }
}
