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

import com.clivenspetit.events.domain.ValidationResource;
import com.clivenspetit.events.domain.security.crypto.password.PasswordEncoder;
import com.clivenspetit.events.domain.user.User;
import com.clivenspetit.events.domain.user.UserMother;
import com.clivenspetit.events.domain.user.login.Login;
import com.clivenspetit.events.domain.user.login.LoginMother;
import com.clivenspetit.events.domain.user.login.exception.InvalidCredentialsException;
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
public class LoginUseCaseTest {

    private static final String USER_EMAIL = "john.doe@gmail.com";
    private static final String USER_PASSWORD = "Hello123++";
    private static final String USER_PASSWORD_ENCODED = "$2y$10$ikaoHK.A4Nfgkn2g3iKCOu8Zha6b/FoltoDU3BMwH5wqoykD/D0Aa";

    @ClassRule
    public static final ValidationResource validationResource = new ValidationResource();

    private Set<ConstraintViolation<LoginUseCase>> violations;
    private UserRepository userRepository;
    private LoginUseCase loginUseCase;
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        loginUseCase = new LoginUseCase(userRepository, passwordEncoder);

        when(userRepository.getUserEmail(USER_EMAIL))
                .thenReturn(UserMother.validUser()
                        .password(USER_PASSWORD_ENCODED)
                        .build()
                );

        when(passwordEncoder.matches(USER_PASSWORD, USER_PASSWORD_ENCODED))
                .thenReturn(Boolean.TRUE);
    }

    @After
    public void tearDown() throws Exception {
        userRepository = null;
        loginUseCase = null;
        violations = null;
    }

    @Test
    public void login_nullArgumentPassed_throwException() throws Exception {
        Method method = LoginUseCase.class.getMethod("login", Login.class);
        Object[] parameters = new Object[]{null};

        violations = validationResource.executableValidator.validateParameters(loginUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test
    public void login_invalidLoginPassed_throwException() throws Exception {
        Method method = LoginUseCase.class.getMethod("login", Login.class);
        Object[] parameters = new Object[]{new Login(null, null)};

        violations = validationResource.executableValidator.validateParameters(loginUseCase, method, parameters);

        assertFalse("Null argument should not pass.", violations.isEmpty());
    }

    @Test(expected = InvalidCredentialsException.class)
    public void login_unknownLoginPassed_throwException() {
        loginUseCase.login(new Login("john.doe@domain.com", USER_PASSWORD));
    }

    @Test
    public void login_validLoginPassed_returnUser() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        User user = loginUseCase.login(LoginMother.validLogin().build());

        verify(userRepository, times(1))
                .getUserEmail(argumentCaptor.capture());

        verify(passwordEncoder, times(1))
                .matches(USER_PASSWORD, USER_PASSWORD_ENCODED);

        assertThat(argumentCaptor.getAllValues().get(0), is(USER_EMAIL));
        assertThat(user.getEmail(), is(USER_EMAIL));
        assertThat(user.getFirstName(), is("John"));
        assertThat(user.getLastName(), is("Doe"));
        assertThat(user.getPassword(), is(USER_PASSWORD_ENCODED));
    }
}
