package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.IterableOfStringPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Clivens Petit
 */
public class IterableOfStringPatternValidator
        implements ConstraintValidator<IterableOfStringPattern, Iterable<String>> {

    private String regexp;

    @Override
    public void initialize(IterableOfStringPattern annotation) {
        regexp = annotation.regexp();
    }

    @Override
    public boolean isValid(Iterable<String> items, ConstraintValidatorContext context) {
        if (items == null) return true;

        for (String item : items) {
            if (!item.matches(this.regexp)) return false;
        }

        return true;
    }
}
