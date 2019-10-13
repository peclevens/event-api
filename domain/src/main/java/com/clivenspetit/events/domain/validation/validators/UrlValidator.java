package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.Url;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class UrlValidator implements ConstraintValidator<Url, String> {

    private static final String URL_PATTERN = "^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    @Override
    public boolean isValid(String inputUrl, ConstraintValidatorContext context) {
        return inputUrl != null && !inputUrl.trim().isEmpty() && inputUrl.matches(URL_PATTERN);
    }
}