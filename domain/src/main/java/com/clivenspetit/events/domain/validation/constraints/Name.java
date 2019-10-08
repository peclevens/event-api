package com.clivenspetit.events.domain.validation.constraints;

import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Name.List.class)
@Pattern(regexp = "^[a-zA-Z]+(?:(?:[',. -][a-zA-Z ])?[a-zA-Z]*)*$")
@ReportAsSingleViolation
public @interface Name {

    String message() default "{com.clivenspetit.events.domain.validation.constraints.Name.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
            ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {

        Name[] value();
    }
}