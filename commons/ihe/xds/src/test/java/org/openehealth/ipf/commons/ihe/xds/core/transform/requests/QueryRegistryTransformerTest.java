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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link QueryRegistryTransformer}.
 * @author Jens Riemschneider
 */
public class QueryRegistryTransformerTest {
    private QueryRegistryTransformer transformer;
    
    @BeforeEach
    public void setUp() {
        transformer = new QueryRegistryTransformer();
    }
    
    @Test
    public void testQueryTypes() {
        checkForQuery(new FindDocumentsQuery());
        checkForQuery(new FindFoldersQuery());
        checkForQuery(new FindSubmissionSetsQuery());
        checkForQuery(new GetAllQuery());
        checkForQuery(new GetAssociationsQuery());
        checkForQuery(new GetDocumentsAndAssociationsQuery());
        checkForQuery(new GetDocumentsQuery());
        checkForQuery(new GetFolderAndContentsQuery());
        checkForQuery(new GetFoldersForDocumentQuery());
        checkForQuery(new GetFoldersQuery());
        checkForQuery(new GetRelatedDocumentsQuery());
        checkForQuery(new GetSubmissionSetAndContentsQuery());
        checkForQuery(new GetSubmissionSetsQuery());
    }
    
    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }

    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }

    private void checkForQuery(Query query) {
        var request = new QueryRegistry(query);
        var ebXML = transformer.toEbXML(request);
        var result = transformer.fromEbXML(ebXML);
        assertEquals(request, result);
    }
}
