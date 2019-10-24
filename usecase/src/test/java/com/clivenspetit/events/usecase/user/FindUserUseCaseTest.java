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
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * @author Clivens Petit
 */
public class FindUserUseCaseTest {

    private static final String USER_ID = "2cb4601f-bd11-4d01-98f5-b8a249e2b0ed";
    private static final String USER_EMAIL = "john.doe@gmail.com";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<FindUserUseCase>> violations;
    private UserRepository userRepository;
    private FindUserUseCase findUserUseCase;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        findUserUseCase = new FindUserUseCase(userRepository);

        when(userRepository.getUserById(USER_ID))
                .thenReturn(UserMother.validUser().build());

        when(userRepository.getUserEmail(USER_EMAIL))
                .thenReturn(UserMother.validUser().build());
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        findUserUseCase = null;
        violations = null;
    }

    @Test
    public void findUserById_nullArgumentPassed_throwException() throws Exception {
        Method method = FindUserUseCase.class.getMethod("findUserById", String.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(findUserUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test
    public void findUserById_invalidIdPassed_throwException() throws Exception {
        Method method = FindUserUseCase.class.getMethod("findUserById", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = validationResource.executableValidator.validateParameters(findUserUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserById_unknownIdPassed_throwException() {
        findUserUseCase.findUserById("909b6ea4-1975-4c5c-ac4e-11db13eea89a");
    }

    @Test
    public void findUserById_validIdPassed_returnUser() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        User user = findUserUseCase.findUserById(USER_ID);

        verify(userRepository, times(1))
                .getUserById(argumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(USER_ID));
        assertThat(user.getId(), is(USER_ID));
        assertThat(user.getEmail(), is(USER_EMAIL));
        assertThat(user.getFirstName(), is("John"));
        assertThat(user.getLastName(), is("Doe"));
        assertThat(user.getPassword(), is("Hello123++"));
    }

    @Test
    public void findUserByEmail_nullArgumentPassed_throwException() throws Exception {
        Method method = FindUserUseCase.class.getMethod("findUserByEmail", String.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(findUserUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test
    public void findUserByEmail_invalidIdPassed_throwException() throws Exception {
        Method method = FindUserUseCase.class.getMethod("findUserByEmail", String.class);
        Object[] parameters = new Object[]{"id"};

        violations = validationResource.executableValidator.validateParameters(findUserUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserByEmail_unknownEmailPassed_throwException() {
        findUserUseCase.findUserByEmail("john.doe@domain.com");
    }

    @Test
    public void findUserByEmail_validEmailPassed_returnUser() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        User user = findUserUseCase.findUserByEmail(USER_EMAIL);

        verify(userRepository, times(1))
                .getUserEmail(argumentCaptor.capture());

        assertThat(argumentCaptor.getAllValues().get(0), is(USER_EMAIL));
        assertThat(user.getId(), is(USER_ID));
        assertThat(user.getEmail(), is(USER_EMAIL));
        assertThat(user.getFirstName(), is("John"));
        assertThat(user.getLastName(), is("Doe"));
        assertThat(user.getPassword(), is("Hello123++"));
    }
}
