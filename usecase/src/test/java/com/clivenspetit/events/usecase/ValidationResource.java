package com.clivenspetit.events.usecase;

import org.junit.rules.ExternalResource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

/**
 * @author Clivens Petit
 */
public final class ValidationResource extends ExternalResource {

    public ValidatorFactory validatorFactory;
    public ExecutableValidator executableValidator;
    public Validator validator;

    @Override
    protected void before() throws Throwable {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        executableValidator = validator.forExecutables();
    }

    @Override
    protected void after() {
        validatorFactory.close();
        executableValidator = null;
        validatorFactory = null;
    }
}
