/*
 * Copyright 2013 the original author or authors.
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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocumentSet;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RemoveDocumentSetTransformer}.
 * @author Boris Stanojevic
 */
public class RemoveDocumentSetTransformerTest {
    private RemoveDocumentSetTransformer transformer;
    private RemoveDocumentSet request;

    
    @Before
    public void setUp() throws Exception {        
        transformer = new RemoveDocumentSetTransformer();
        request = SampleData.createRemoveDocumentSet();
    }

    @Test
    public void testToEbXML() {
        QueryRegistry queryRegistry = SampleData.createFindDocumentsQuery();
        request.setQuery(queryRegistry.getQuery());
        EbXMLRemoveObjectsRequest ebXML = transformer.toEbXML(request);
        assertNotNull(ebXML);
        assertEquals(2, ebXML.getReferences().size());
        assertEquals("1.2.3", ebXML.getReferences().get(0).getHome());
        assertEquals("5.6.7", ebXML.getReferences().get(1).getHome());
        assertEquals(RemoveDocumentSet.DEFAULT_DELETION_SCOPE, ebXML.getDeletionScope());
        assertNotNull("12.21.41", ebXML.getHome());
        assertEquals("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d", ebXML.getId());
    }

    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }
    
    @Test
    public void testToEbXMLEmpty() {
        EbXMLRemoveObjectsRequest result = transformer.toEbXML(new RemoveDocumentSet());
        assertNotNull(result);
        assertNotNull(result.getReferences());
        assertEquals(0, result.getReferences().size());
        assertEquals(RemoveDocumentSet.DEFAULT_DELETION_SCOPE, result.getDeletionScope());
    }
    
    @Test
    public void testFromEbXML() {
        EbXMLRemoveObjectsRequest ebXML = transformer.toEbXML(request);
        RemoveDocumentSet result = transformer.fromEbXML(ebXML);
        
        assertEquals(request.toString(), result.toString());
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        EbXMLRemoveObjectsRequest ebXML = transformer.toEbXML(new RemoveDocumentSet());
        assertEquals(new RemoveDocumentSet(), transformer.fromEbXML(ebXML));
    }
}
