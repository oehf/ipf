/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.core.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;

/**
 * Matchers for {@link Optional}, so that an API returning one can be asserted on without unwrapping
 * it first.
 * <pre>
 * assertThat(mappings.map("gender", "M"), hasValue("male"));
 * assertThat(mappings.map("gender", "X"), hasNoValue());
 * assertThat(loaders.forFormat("xml"), hasValue(instanceOf(XmlMappingLoader.class)));
 * </pre>
 * {@code is(Optional.of("male"))} says the same thing, but reports a mismatch as two opaque
 * {@code Optional} literals; these name which half is wrong - an empty Optional where a value was
 * expected reads differently from the wrong value.
 * <p>
 * Ships in this module's test jar. Depend on it with
 * {@code <artifactId>ipf-commons-core</artifactId><type>test-jar</type><scope>test</scope>}.
 *
 * @since 6.0
 */
public final class OptionalMatchers {

    private OptionalMatchers() {
    }

    /**
     * @param value the value the Optional is expected to hold
     */
    public static <T> Matcher<Optional<T>> hasValue(T value) {
        return hasValue(equalTo(value));
    }

    /**
     * @param matcher what the value it holds is expected to satisfy
     */
    public static <T> Matcher<Optional<T>> hasValue(Matcher<? super T> matcher) {
        return new TypeSafeDiagnosingMatcher<>() {

            @Override
            protected boolean matchesSafely(Optional<T> actual, Description mismatch) {
                if (actual.isEmpty()) {
                    mismatch.appendText("was empty");
                    return false;
                }
                if (!matcher.matches(actual.get())) {
                    mismatch.appendText("value ");
                    matcher.describeMismatch(actual.get(), mismatch);
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a value that is ").appendDescriptionOf(matcher);
            }
        };
    }

    /**
     * The counterpart of {@link #hasValue(Object)}: nothing, and it says what it found instead.
     */
    public static <T> Matcher<Optional<T>> hasNoValue() {
        return new TypeSafeDiagnosingMatcher<>() {

            @Override
            protected boolean matchesSafely(Optional<T> actual, Description mismatch) {
                if (actual.isPresent()) {
                    mismatch.appendText("was ").appendValue(actual.get());
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("no value");
            }
        };
    }
}
