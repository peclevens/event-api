package com.clivenspetit.events.domain.event.validation.validators;

import com.clivenspetit.events.domain.event.Event;
import com.clivenspetit.events.domain.event.validation.constraints.RequireEventLocationOrOnlineUrl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class EventLocationValidator implements ConstraintValidator<RequireEventLocationOrOnlineUrl, Event> {

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }

    @Override
    public void initialize(RequireEventLocationOrOnlineUrl constraintAnnotation) {

    }
}
