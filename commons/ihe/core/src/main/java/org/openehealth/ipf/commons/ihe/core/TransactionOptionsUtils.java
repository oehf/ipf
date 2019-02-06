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

package org.openehealth.ipf.commons.ihe.core;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Some utilities for {@link TransactionOptions}
 */
public interface TransactionOptionsUtils {

    /**
     * Splits a string containing comma-separated {@link TransactionOptions transaction option enum names}
     * into a list of {@link TransactionOptions}.
     *
     * @param optionString option string, comma-separated
     * @param clazz        TransactionOptions enum
     * @param <T>          TransactionOptions type
     * @return list of TransactionOptions
     */
    static <T extends Enum<T> & TransactionOptions<?>> List<T> split(String optionString, Class<T> clazz) {
        if (optionString == null || optionString.isEmpty()) return null;
        String[] optionStrings = optionString.split("\\s*,\\s*");
        return Stream.of(optionStrings)
                .map(s -> Enum.valueOf(clazz, s))
                .collect(Collectors.toList());
    }

}
