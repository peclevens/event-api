package com.clivenspetit.events.domain.validation.constraints;

import com.clivenspetit.events.domain.validation.validators.AnyOfValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AnyOfValidator.class})
public @interface AnyOf {

    String message() default "{com.clivenspetit.events.domain.validation.constraints.RequireEventLocationOrOnlineUrl.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields() default {};
}
