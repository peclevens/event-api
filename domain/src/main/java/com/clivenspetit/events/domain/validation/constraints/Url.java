package com.clivenspetit.events.domain.validation.constraints;

import com.clivenspetit.events.domain.validation.validators.UrlValidator;

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
@Repeatable(Url.List.class)
@Constraint(validatedBy = {UrlValidator.class})
public @interface Url {

    String message() default "{com.clivenspetit.events.domain.validation.constraints.URL.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
            ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {

        Url[] value();
    }
}
