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

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLProvideAndRegisterDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.FactoryCreator;

/**
 * Tests for {@link ProvideAndRegisterDocumentSetTransformer}.
 * @author Jens Riemschneider
 */
public abstract class ProvideAndRegisterDocumentSetTransformerTestBase implements FactoryCreator {
    private ProvideAndRegisterDocumentSetTransformer transformer;
    private ProvideAndRegisterDocumentSet request;
    private DataHandler dataHandler;    
    
    @Before
    public void setUp() throws Exception {        
        EbXMLFactory factory = createFactory();
        transformer = new ProvideAndRegisterDocumentSetTransformer(factory);        

        request = SampleData.createProvideAndRegisterDocumentSet();
        dataHandler = request.getDocuments().get(0).getContent(DataHandler.class);
    }

    @Test
    public void testToEbXML() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        assertNotNull(ebXML);
        assertEquals(1, ebXML.getExtrinsicObjects().size());
        assertEquals(2, ebXML.getRegistryPackages().size());

        List<EbXMLAssociation> associations = ebXML.getAssociations();
        assertEquals(3, associations.size());
        assertEquals(AssociationType.HAS_MEMBER, associations.get(0).getAssociationType());
        assertEquals("submissionSet01", associations.get(0).getSource());
        assertEquals("document01", associations.get(0).getTarget());
        
        assertEquals(AssociationType.HAS_MEMBER, associations.get(1).getAssociationType());
        assertEquals("submissionSet01", associations.get(1).getSource());
        assertEquals("folder01", associations.get(1).getTarget());
        
        assertEquals(AssociationType.HAS_MEMBER, associations.get(2).getAssociationType());
        assertEquals("folder01", associations.get(2).getSource());
        assertEquals("document01", associations.get(2).getTarget());
        
        List<EbXMLExtrinsicObject> docEntries = ebXML.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND);
        assertEquals(1, docEntries.size());
        assertEquals("document01", docEntries.get(0).getId());
        assertEquals("Document 01", docEntries.get(0).getName().getValue());
        
        List<EbXMLRegistryPackage> folders = ebXML.getRegistryPackages(Vocabulary.FOLDER_CLASS_NODE);
        assertEquals(1, folders.size());
        assertEquals("Folder 01", folders.get(0).getName().getValue());
        
        List<EbXMLRegistryPackage> submissionSets = ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        assertEquals(1, submissionSets.size());
        assertEquals("Submission Set 01", submissionSets.get(0).getName().getValue());
        
        Map<String, DataHandler> documents = ebXML.getDocuments();
        assertEquals(1, documents.size());
        assertSame(dataHandler, documents.get("document01"));
    }
    
    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }
    
    @Test
    public void testToEbXMLEmpty() {
        EbXMLProvideAndRegisterDocumentSetRequest result = transformer.toEbXML(new ProvideAndRegisterDocumentSet());
        assertNotNull(result);
        assertEquals(0, result.getAssociations().size());
        assertEquals(0, result.getExtrinsicObjects().size());
        assertEquals(0, result.getRegistryPackages().size());
        assertEquals(0, result.getDocuments().size());
    }
    

    
    
    @Test
    public void testFromEbXML() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(request);
        ProvideAndRegisterDocumentSet result = transformer.fromEbXML(ebXML);
        
        assertEquals(request.toString(), result.toString());
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        EbXMLProvideAndRegisterDocumentSetRequest ebXML = transformer.toEbXML(new ProvideAndRegisterDocumentSet());
        assertEquals(new ProvideAndRegisterDocumentSet(), transformer.fromEbXML(ebXML));
    }
}
