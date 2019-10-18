/*
 * Copyright 2019 MAGIC SOFTWARE BAY, SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
