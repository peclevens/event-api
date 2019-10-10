package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.Word;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class WordTest {

    @Word(min = 5, max = 10, message = "Description should be between {min} to {max} words.")
    private String description;

    public WordTest() {

    }

    public WordTest(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
