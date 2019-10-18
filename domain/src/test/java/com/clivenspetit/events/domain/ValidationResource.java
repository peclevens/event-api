package com.clivenspetit.events.domain;

import org.junit.rules.ExternalResource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author Clivens Petit
 */
public final class ValidationResource extends ExternalResource {

    public ValidatorFactory validatorFactory;
    public Validator validator;

    @Override
    protected void before() throws Throwable {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    protected void after() {
        validatorFactory.close();
        validatorFactory = null;
    }
}
