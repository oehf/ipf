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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.QueryRegistry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindFoldersQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.FindSubmissionSetsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetAllQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetAssociationsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetDocumentsAndAssociationsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetFolderAndContentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetFoldersForDocumentQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetFoldersQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetRelatedDocumentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetSubmissionSetAndContentsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.GetSubmissionSetsQuery;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.Query;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.SqlQuery;

/**
 * Tests for {@link QueryRegistryTransformer}.
 * @author Jens Riemschneider
 */
public class QueryRegistryTransformerTest {
    private QueryRegistryTransformer transformer;
    
    @Before
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
        checkForQuery(new SqlQuery());
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
        QueryRegistry request = new QueryRegistry(query);
        AdhocQueryRequest ebXML = transformer.toEbXML(request);
        QueryRegistry result = transformer.fromEbXML(ebXML);
        assertEquals(request, result);
    }
}
