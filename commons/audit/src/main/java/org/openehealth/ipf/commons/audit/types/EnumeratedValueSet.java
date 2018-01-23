/*
 * Copyright 2017 the original author or authors.
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

package org.openehealth.ipf.commons.audit.types;

import java.util.stream.Stream;

/**
 * This interface is intended to be implemented by enum classes in order to provide a
 * set of code values. Complex codes (of type @link {@link CodedValueType}) should be
 * inherited from {@link EnumeratedCodedValue}.
 *
 * @see EnumeratedCodedValue
 *
 * @author Christian Ohr
 * @since 3.5
 */
public interface EnumeratedValueSet<T> {

    T getValue();

    default boolean matches(Object code) {
        return getValue().equals(code);
    }

    static <T, E extends Enum<E> & EnumeratedValueSet<T>> E enumForCode(Class<E> clazz, Object code) {
        return Stream.of(clazz.getEnumConstants())
                .filter(e -> e.matches(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(code.toString() + " is not a code for " + clazz.getSimpleName()));
    }


}
