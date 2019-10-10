package com.clivenspetit.events.domain.event.validation.constraints;

import com.clivenspetit.events.domain.event.validation.validators.EventLocationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EventLocationValidator.class})
public @interface EventLocation {

    String message() default "{com.clivenspetit.events.domain.validation.constraints.EventLocation.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
