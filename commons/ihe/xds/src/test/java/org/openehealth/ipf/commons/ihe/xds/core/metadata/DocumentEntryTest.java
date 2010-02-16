/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.DocumentEntryTransformerTestBase;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DocumentEntry}.
 * Note that most of the accessors of {@code DocumentEntry} are tested via
 * the tests of its transformer {@link DocumentEntryTransformerTestBase}.
 */
public class DocumentEntryTest {
    private Author author1;
    private Author author2;
    private Author author3;
    private DocumentEntry docEntry;

    @Before
    public void setUp() {
        author1 = new Author();
        author2 = new Author();
        author3 = new Author();
        docEntry = new DocumentEntry();
    }

    @Test
    public void testSetAuthorResetsAuthorList() {
        docEntry.getAuthors().add(author1);
        docEntry.getAuthors().add(author2);
        docEntry.setAuthor(author3);
        assertEquals(Collections.singletonList(author3), docEntry.getAuthors());
    }

    @Test
    public void testGetAuthorOnEmptyAuthorList() {
        assertEquals(null, docEntry.getAuthor());
    }
    
    @Test
    public void testGetAuthorOnAuthorListContainingMultipleAuthors() {
        docEntry.getAuthors().add(author1);
        docEntry.getAuthors().add(author2);
        assertEquals(author1, docEntry.getAuthor());
    }
}

