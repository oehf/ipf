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
package org.openehealth.ipf.tutorials.xds.store;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Author;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Person;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XcnName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Author person matching, which takes SQL LIKE patterns (ITI TF-2: 3.18.4.1.2.3.7.1).
 *
 * @author Christian Ohr
 */
class ComparatorsTest {

    /** A character outside the BMP is one character to LIKE, and two UTF-16 code units in Java. */
    @Test
    void aSupplementaryCharacterMatchesItself() {
        var authors = List.of(author(new XcnName("Müller😀", "Hans", null, null, null, null)));

        assertTrue(Comparators.matchesAuthors(List.of("%Müller😀%"), authors));
        // _ stands for exactly one character, which the emoji is
        assertTrue(Comparators.matchesAuthors(List.of("%Müller_^Hans%"), authors));
        assertFalse(Comparators.matchesAuthors(List.of("%Müller😁%"), authors));
    }

    /** An author person without a populated field has no XCN value, which matches no pattern. */
    @Test
    void anEmptyAuthorPersonMatchesNothing() {
        var empty = new Author();
        empty.setAuthorPerson(new Person());

        assertFalse(Comparators.matchesAuthors(List.of("%"), List.of(empty)));
        assertTrue(Comparators.matchesAuthors(List.of("%Hans%"),
                List.of(empty, author(new XcnName("Müller", "Hans", null, null, null, null)))));
    }

    private static Author author(XcnName name) {
        var person = new Person();
        person.setName(name);
        var author = new Author();
        author.setAuthorPerson(person);
        return author;
    }
}
