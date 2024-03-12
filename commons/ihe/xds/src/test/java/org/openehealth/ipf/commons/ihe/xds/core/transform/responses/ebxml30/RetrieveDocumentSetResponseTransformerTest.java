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
package org.openehealth.ipf.commons.ihe.xds.core.transform.responses.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.responses.RetrieveDocumentSetResponseTransformer;

import javax.activation.DataHandler;

import static org.junit.jupiter.api.Assertions.*;
 
/**
 * Tests for {@link RetrieveDocumentSetResponseTransformer}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetResponseTransformerTest {
    private RetrieveDocumentSetResponseTransformer transformer;
    private RetrievedDocumentSet response;
    private DataHandler dataHandler1;
    private DataHandler dataHandler2;
    
    @BeforeEach
    public void setUp() {
        EbXMLFactory factory = new EbXMLFactory30();
        transformer = new RetrieveDocumentSetResponseTransformer(factory);
        
        response = SampleData.createRetrievedDocumentSet();
        dataHandler1 = response.getDocuments().get(0).getDataHandler();
        dataHandler2 = response.getDocuments().get(1).getDataHandler();
    }
    
    @Test
    public void testToEbXML() {
        var ebXML = transformer.toEbXML(response);
        assertNotNull(ebXML);
        
        assertEquals(2, ebXML.getDocuments().size());

        var doc = ebXML.getDocuments().get(0);
        var requestData = doc.getRequestData();
        assertEquals("doc1", requestData.getDocumentUniqueId());
        assertEquals("urn:oid:1.2.3", requestData.getHomeCommunityId());
        assertEquals("repo1", requestData.getRepositoryUniqueId());
        assertEquals("application/test1", doc.getMimeType());
        assertSame(dataHandler1, doc.getDataHandler());
 
        doc = ebXML.getDocuments().get(1);
        requestData = doc.getRequestData();
        assertEquals("doc2", requestData.getDocumentUniqueId());
        assertEquals("urn:oid:1.2.4", requestData.getHomeCommunityId());
        assertEquals("repo2", requestData.getRepositoryUniqueId());
        assertEquals("repo2-new", doc.getNewRepositoryUniqueId());
        assertEquals("doc2-new", doc.getNewDocumentUniqueId());
        assertEquals("application/test2", doc.getMimeType());
        assertSame(dataHandler2, doc.getDataHandler());
     }
    
     @Test
     public void testToEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testToEbXMLEmpty() {
         var ebXML = transformer.toEbXML(new RetrievedDocumentSet());
         assertNotNull(ebXML);
         assertEquals(0, ebXML.getDocuments().size());
     }
     
     
     
     @Test
     public void testFromEbXML() {
         var ebXML = transformer.toEbXML(response);
         var result = transformer.fromEbXML(ebXML);
         assertEquals(response, result);
     }
     
     @Test
     public void testFromEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testFromEbXMLEmpty() {
         var ebXML = transformer.toEbXML(new RetrievedDocumentSet());
         var result = transformer.fromEbXML(ebXML);
         assertEquals(new RetrievedDocumentSet(), result);
     }
}
