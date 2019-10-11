package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.AnyOf;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class AnyOfValidator implements ConstraintValidator<AnyOf, Object> {

    private String[] fields;

    @Override
    public void initialize(AnyOf annotation) {
        fields = annotation.fields();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        BeanWrapper wrapper = new BeanWrapperImpl(object);

        // Make sure at least one field has value
        for (String field : fields) {
            Object value = wrapper.getPropertyValue(field);
            if (value != null) return true;
        }

        return false;
    }
}
