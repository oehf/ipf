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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RetrieveDocumentSetRequestTransformer;

/**
 * Tests for {@link RetrieveDocumentSetRequestTransformer}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetRequestTransformerTest {
    private RetrieveDocumentSetRequestTransformer transformer;
    private RetrieveDocumentSet request;
    
    @Before
    public void setUp() {
        EbXMLFactory factory = new EbXMLFactory30();
        transformer = new RetrieveDocumentSetRequestTransformer(factory);
        
        request = SampleData.createRetrieveDocumentSet();
    }

    @Test
    public void testToEbXML() {
        EbXMLRetrieveDocumentSetRequest ebXML = transformer.toEbXML(request);
        assertNotNull(ebXML);
        
        assertEquals(2, ebXML.getDocuments().size());
        
        RetrieveDocument doc = ebXML.getDocuments().get(0);        
        assertEquals("doc1", doc.getDocumentUniqueId());
        assertEquals("urn:oid:1.2.3", doc.getHomeCommunityId());
        assertEquals("repo1", doc.getRepositoryUniqueId());
 
        doc = ebXML.getDocuments().get(1);
        assertEquals("doc2", doc.getDocumentUniqueId());
        assertEquals("urn:oid:1.2.4", doc.getHomeCommunityId());
        assertEquals("repo2", doc.getRepositoryUniqueId());
     }
    
     @Test
     public void testToEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testToEbXMLEmpty() {
         EbXMLRetrieveDocumentSetRequest ebXML = transformer.toEbXML(new RetrieveDocumentSet());
         assertNotNull(ebXML);
         assertEquals(0, ebXML.getDocuments().size());
     }
     
     
     
     @Test
     public void testFromEbXML() {
         EbXMLRetrieveDocumentSetRequest ebXML = transformer.toEbXML(request);
         RetrieveDocumentSet result = transformer.fromEbXML(ebXML);
         assertEquals(request, result);
     }
     
     @Test
     public void testFromEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testFromEbXMLEmpty() {
         EbXMLRetrieveDocumentSetRequest ebXML = transformer.toEbXML(new RetrieveDocumentSet());
         RetrieveDocumentSet result = transformer.fromEbXML(ebXML);
         assertEquals(new RetrieveDocumentSet(), result);
     }
}
