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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.ebxml30;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RetrieveDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.RetrieveDocumentSetRequestTransformer;

/**
 * Tests for {@link RetrieveDocumentSetRequestTransformer}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetRequestTransformerTest {
    private EbXMLFactory factory;
    private RetrieveDocumentSetRequestTransformer transformer;
    private RetrieveDocumentSet request;
    
    @Before
    public void setUp() {
        factory = new EbXMLFactory30();
        transformer = new RetrieveDocumentSetRequestTransformer(factory);
        
        RetrieveDocument doc1 = new RetrieveDocument();
        doc1.setDocumentUniqueID("doc1");
        doc1.setHomeCommunityID("home1");
        doc1.setRepositoryUniqueID("repo1");

        RetrieveDocument doc2 = new RetrieveDocument();
        doc2.setDocumentUniqueID("doc2");
        doc2.setHomeCommunityID("home2");
        doc2.setRepositoryUniqueID("repo2");
        
        request = new RetrieveDocumentSet();
        request.getDocuments().add(doc1);
        request.getDocuments().add(doc2);
    }
    
    @Test
    public void testToEbXML() {
        RetrieveDocumentSetRequest ebXML = transformer.toEbXML(request);
        assertNotNull(ebXML);
        
        assertEquals(2, ebXML.getDocuments().size());
        
        RetrieveDocument doc = ebXML.getDocuments().get(0);        
        assertEquals("doc1", doc.getDocumentUniqueID());
        assertEquals("home1", doc.getHomeCommunityID());
        assertEquals("repo1", doc.getRepositoryUniqueID());
 
        doc = ebXML.getDocuments().get(1);
        assertEquals("doc2", doc.getDocumentUniqueID());
        assertEquals("home2", doc.getHomeCommunityID());
        assertEquals("repo2", doc.getRepositoryUniqueID());
     }
    
     @Test
     public void testToEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testToEbXMLEmpty() {
         RetrieveDocumentSetRequest ebXML = transformer.toEbXML(new RetrieveDocumentSet());
         assertNotNull(ebXML);
         assertEquals(0, ebXML.getDocuments().size());
     }
     
     
     
     @Test
     public void testFromEbXML() {
         RetrieveDocumentSetRequest ebXML = transformer.toEbXML(request);
         RetrieveDocumentSet result = transformer.fromEbXML(ebXML);
         assertEquals(request, result);
     }
     
     @Test
     public void testFromEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testFromEbXMLEmpty() {
         RetrieveDocumentSetRequest ebXML = transformer.toEbXML(new RetrieveDocumentSet());
         RetrieveDocumentSet result = transformer.fromEbXML(ebXML);
         assertEquals(new RetrieveDocumentSet(), result);
     }
}
