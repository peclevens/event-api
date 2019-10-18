package com.clivenspetit.events.domain.validation.constraints;

import com.clivenspetit.events.domain.validation.validators.IterableOfStringPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Clivens Petit
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(IterableOfStringPattern.List.class)
@Constraint(validatedBy = {IterableOfStringPatternValidator.class})
public @interface IterableOfStringPattern {

    String message() default "{com.clivenspetit.events.domain.validation.constraints.IterableOfStringPattern.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String regexp() default ".*";

    @Documented
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
            ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {

        IterableOfStringPattern[] value();
    }
}
