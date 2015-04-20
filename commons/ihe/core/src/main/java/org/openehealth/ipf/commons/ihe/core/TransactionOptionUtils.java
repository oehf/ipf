/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.core;

import java.lang.reflect.Array;
import java.util.*;

/**
 *
 */
public final class TransactionOptionUtils {

    private TransactionOptionUtils() {
    }

    public static <T extends Enum<T> & TransactionOptions> T[] split(String optionString, Class<T> clazz) {
        if (optionString == null || optionString.isEmpty()) return null;
        String[] optionStrings = optionString.split("\\s*,\\s*");
        T[] options = (T[])Array.newInstance(clazz, optionStrings.length);
        for (int i = 0; i < optionStrings.length; i++) {
            options[i] = Enum.valueOf(clazz, optionStrings[i]);
        }
        return options;
    }

    public static <T extends TransactionOptions> String[] concat(T options, String... suffix) {
        if (suffix != null && suffix.length > 0) {
            Set<String> events = new HashSet<>(Arrays.asList(options.getSupportedEvents()));
            events.addAll(Arrays.asList(suffix));
            return events.toArray(new String[events.size()]);
        }
        return options.getSupportedEvents();
    }

    public static <T extends TransactionOptions> String[] concat(T options, T otherOptions, String... suffix) {
        String[] events = concat(otherOptions, suffix);
        return concat(options, events);
    }

    public static <T extends TransactionOptions> String[] concatAll(T... options) {
        String[] events = null;
        for (T option : options) {
            events = concat(option, events);
        }
        return events;
    }

    public static <T extends TransactionOptions> String[] concatAllToString(T... options) {
        String[] events = concatAll(options);
        StringBuilder builder = new StringBuilder();
        for (String event : events) {
            builder.append(event).append(" ");
        }
        return new String[] { builder.toString().trim() };
    }
}
