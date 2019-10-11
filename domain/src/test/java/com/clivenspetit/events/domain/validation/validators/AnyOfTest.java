package com.clivenspetit.events.domain.validation.validators;

import com.clivenspetit.events.domain.validation.constraints.AnyOf;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
@AnyOf(fields = {"fieldOne", "fieldTwo"}, message = "Field one or field two is required. Both of them work fine too.")
public class AnyOfTest {

    private String fieldOne;
    private String fieldTwo;

    public AnyOfTest() {

    }

    public AnyOfTest(String fieldOne, String fieldTwo) {
        this.fieldOne = fieldOne;
        this.fieldTwo = fieldTwo;
    }

    public String getFieldOne() {
        return fieldOne;
    }

    public void setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
    }

    public String getFieldTwo() {
        return fieldTwo;
    }

    public void setFieldTwo(String fieldTwo) {
        this.fieldTwo = fieldTwo;
    }
}
