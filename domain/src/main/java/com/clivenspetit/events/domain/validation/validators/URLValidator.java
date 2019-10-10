package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.URL;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.MalformedURLException;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class URLValidator implements ConstraintValidator<URL, String> {

    @Override
    public void initialize(URL constraintAnnotation) {

    }

    @Override
    public boolean isValid(String inputUrl, ConstraintValidatorContext context) {
        if (inputUrl == null || inputUrl.trim().isEmpty()) return false;

        try {
            new java.net.URL(inputUrl);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
