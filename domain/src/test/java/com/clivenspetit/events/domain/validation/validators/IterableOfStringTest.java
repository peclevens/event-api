package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.IterableOfStringPattern;

import java.util.List;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public class IterableOfStringTest {

    @IterableOfStringPattern(regexp = "[a-z]+", message = "Invalid item name.")
    private List<String> items;

    public IterableOfStringTest() {

    }

    public IterableOfStringTest(List<String> items) {
        this.items = items;
    }
}
