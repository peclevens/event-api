package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.Word;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Clivens Petit
 */
public class WordValidator implements ConstraintValidator<Word, String> {

    private int min;
    private int max;

    @Override
    public void initialize(Word word) {
        min = word.min();
        max = word.max();
    }

    @Override
    public boolean isValid(String text, ConstraintValidatorContext context) {
        // Get words list
        String[] words = (text != null && !text.trim().isEmpty())
                ? text.split("\\s+") : new String[]{};

        return words.length >= min && words.length <= max;
    }
}
