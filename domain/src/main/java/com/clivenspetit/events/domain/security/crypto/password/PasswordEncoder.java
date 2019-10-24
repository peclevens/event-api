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

package com.clivenspetit.events.domain.security.crypto.password;

/**
 * Service interface for encoding passwords. The preferred implementation is BCryptPasswordEncoder.
 *
 * @author Clivens Petit
 */
public interface PasswordEncoder {

    /**
     * Encode the raw password.
     *
     * @param rawPassword The raw password to encode
     * @return The encoded password
     */
    String encode(CharSequence rawPassword);

    /**
     * Verify the encoded password obtained from storage matches the submitted raw password after it too is encoded.
     *
     * @param rawPassword     The raw password to encode and match
     * @param encodedPassword The encoded password from storage to compare with
     * @return true if the raw password, after encoding, matches the encoded password from storage
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
