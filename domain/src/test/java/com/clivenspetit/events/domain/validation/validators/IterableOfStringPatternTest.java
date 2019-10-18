package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.IterableOfStringPattern;

import java.util.List;

/**
 * @author Clivens Petit
 */
public class IterableOfStringPatternTest {

    @IterableOfStringPattern(regexp = "[a-z]+", message = "Invalid item name.")
    private List<String> items;

    public IterableOfStringPatternTest() {

    }

    public IterableOfStringPatternTest(List<String> items) {
        this.items = items;
    }
}
