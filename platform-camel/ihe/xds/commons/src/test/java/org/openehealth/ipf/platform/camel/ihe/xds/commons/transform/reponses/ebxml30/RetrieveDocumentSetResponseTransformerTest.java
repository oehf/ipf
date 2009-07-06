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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.reponses.ebxml30;

import static org.junit.Assert.*;

import javax.activation.DataHandler;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.SampleData;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRetrieveDocumentSetResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocument;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.RetrievedDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.RetrieveDocumentSetResponseTransformer;
 
/**
 * Tests for {@link RetrieveDocumentSetResponseTransformer}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetResponseTransformerTest {
    private EbXMLFactory factory;
    private RetrieveDocumentSetResponseTransformer transformer;
    private RetrievedDocumentSet response;
    private DataHandler dataHandler1;
    private DataHandler dataHandler2;
    
    @Before
    public void setUp() throws Exception {
        factory = new EbXMLFactory30();
        transformer = new RetrieveDocumentSetResponseTransformer(factory);
        
        response = SampleData.createRetrievedDocumentSet();
        dataHandler1 = response.getDocuments().get(0).getDataHandler();
        dataHandler2 = response.getDocuments().get(1).getDataHandler();
    }
    
    @Test
    public void testToEbXML() {
        EbXMLRetrieveDocumentSetResponse ebXML = transformer.toEbXML(response);
        assertNotNull(ebXML);
        
        assertEquals(2, ebXML.getDocuments().size());
        
        RetrievedDocument doc = ebXML.getDocuments().get(0);        
        RetrieveDocument requestData = doc.getRequestData();
        assertEquals("doc1", requestData.getDocumentUniqueID());
        assertEquals("home1", requestData.getHomeCommunityID());
        assertEquals("repo1", requestData.getRepositoryUniqueID());
        assertSame(dataHandler1, doc.getDataHandler());
 
        doc = ebXML.getDocuments().get(1);
        requestData = doc.getRequestData();
        assertEquals("doc2", requestData.getDocumentUniqueID());
        assertEquals("home2", requestData.getHomeCommunityID());
        assertEquals("repo2", requestData.getRepositoryUniqueID());
        assertSame(dataHandler2, doc.getDataHandler());
     }
    
     @Test
     public void testToEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testToEbXMLEmpty() {
         EbXMLRetrieveDocumentSetResponse ebXML = transformer.toEbXML(new RetrievedDocumentSet());
         assertNotNull(ebXML);
         assertEquals(0, ebXML.getDocuments().size());
     }
     
     
     
     @Test
     public void testFromEbXML() {
         EbXMLRetrieveDocumentSetResponse ebXML = transformer.toEbXML(response);
         RetrievedDocumentSet result = transformer.fromEbXML(ebXML);
         assertEquals(response, result);
     }
     
     @Test
     public void testFromEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testFromEbXMLEmpty() {
         EbXMLRetrieveDocumentSetResponse ebXML = transformer.toEbXML(new RetrievedDocumentSet());
         RetrievedDocumentSet result = transformer.fromEbXML(ebXML);
         assertEquals(new RetrievedDocumentSet(), result);
     }
}
