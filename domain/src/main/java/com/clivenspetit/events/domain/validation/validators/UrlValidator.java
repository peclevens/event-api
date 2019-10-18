package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.Url;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Clivens Petit
 */
public class UrlValidator implements ConstraintValidator<Url, String> {

    private static final String URL_PATTERN = "^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    @Override
    public boolean isValid(String inputUrl, ConstraintValidatorContext context) {
        if (inputUrl == null) return true;

        return !inputUrl.trim().isEmpty() && inputUrl.matches(URL_PATTERN);
    }
}
