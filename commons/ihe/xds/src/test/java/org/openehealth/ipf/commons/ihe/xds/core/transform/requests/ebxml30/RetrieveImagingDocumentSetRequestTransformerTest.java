/*
 * Copyright 2012 the original author or authors.
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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveImagingDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveImagingDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RetrieveImagingDocumentSetRequestTransformer;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for {@link org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RetrieveImagingDocumentSetRequestTransformer}.
 * @author Clay Sebourn
 */
public class RetrieveImagingDocumentSetRequestTransformerTest
{
    private RetrieveImagingDocumentSetRequestTransformer transformer;
    private RetrieveImagingDocumentSet request;
    
    @Before
    public void setUp() {
        EbXMLFactory factory = new EbXMLFactory30();
        transformer = new RetrieveImagingDocumentSetRequestTransformer(factory);
        
        request = SampleData.createRetrieveImagingDocumentSet();
    }

    @Test
    public void testToEbXML() {
        EbXMLRetrieveImagingDocumentSetRequest ebXML = transformer.toEbXML(request);
        assertNotNull(ebXML);
        
        assertEquals(2, ebXML.getRetrieveStudies().size());
        assertEquals("urn:oid:1.1.1", ebXML.getRetrieveStudies().get(0).getStudyInstanceUID());

        assertEquals(2, ebXML.getRetrieveStudies().get(0).getRetrieveSerieses().size());
        assertEquals("urn:oid:1.2.1", ebXML.getRetrieveStudies().get(0).getRetrieveSerieses().get(0).getSeriesInstanceUID());

        List<RetrieveDocument> documents = ebXML.getRetrieveStudies().get(0).getRetrieveSerieses().get(0).getDocuments();
        assertEquals(2, documents.size());

        RetrieveDocument doc = documents.get(0);
        assertEquals("doc1", doc.getDocumentUniqueId());
        assertEquals("urn:oid:1.2.3", doc.getHomeCommunityId());
        assertEquals("repo1", doc.getRepositoryUniqueId());
 
        doc = documents.get(1);
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
         EbXMLRetrieveImagingDocumentSetRequest ebXML = transformer.toEbXML(new RetrieveImagingDocumentSet());
         assertNotNull(ebXML);
         assertEquals(0, ebXML.getRetrieveStudies().size());
     }

     @Test
     public void testFromEbXML() {
         EbXMLRetrieveImagingDocumentSetRequest ebXML = transformer.toEbXML(request);
         RetrieveImagingDocumentSet result = transformer.fromEbXML(ebXML);
         assertEquals(request, result);
     }
     
     @Test
     public void testFromEbXMLNull() {
         assertNull(transformer.toEbXML(null));
     }

     @Test
     public void testFromEbXMLEmpty() {
         EbXMLRetrieveImagingDocumentSetRequest ebXML = transformer.toEbXML(new RetrieveImagingDocumentSet());
         RetrieveImagingDocumentSet result = transformer.fromEbXML(ebXML);
         assertEquals(new RetrieveImagingDocumentSet(), result);
     }
}
