/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v2;

import org.openehealth.ipf.commons.ihe.core.TransactionOptions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Christian Ohr
 */
public interface Hl7v2TransactionOptions extends TransactionOptions<String> {


    static List<String> concat(Hl7v2TransactionOptions options, List<String> suffix) {
        if (suffix != null && !suffix.isEmpty()) {
            // Remove potential duplicates
            var events = new HashSet<>(options.getSupportedThings());
            events.addAll(suffix);
            return new ArrayList<>(events);
        }
        return options.getSupportedThings();
    }

    static List<String> concat(Hl7v2TransactionOptions option, Hl7v2TransactionOptions otherOption, List<String> suffix) {
        var events = concat(otherOption, suffix);
        return concat(option, events);
    }

    /**
     * Provides a concatenated string with all things the options support, separated with spaces
     *
     * @param options transaction options
     * @param <T>     TransactionOptions type
     * @return concatenated string
     */
    static <T extends Hl7v2TransactionOptions> String concatAllToString(List<? extends T> options) {
        return options.stream()
                .flatMap(o -> o.getSupportedThings().stream())
                .distinct()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}
