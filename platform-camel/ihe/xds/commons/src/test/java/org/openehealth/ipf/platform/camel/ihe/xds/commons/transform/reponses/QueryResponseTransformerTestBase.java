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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.reponses;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.FactoryCreator;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.QueryResponseTransformer;

/**
 * Tests for {@link QueryResponseTransformer}.
 * @author Jens Riemschneider
 */
public abstract class QueryResponseTransformerTestBase implements FactoryCreator {
    private QueryResponseTransformer transformer;
    private QueryResponse response;    
    
    @Before
    public void setUp() {        
        EbXMLFactory factory = createFactory();
        transformer = new QueryResponseTransformer(factory);        

        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setEntryUUID("submissionSet01");
        submissionSet.setTitle(new LocalizedString("Submission Set 01", "en-US", "UTF8"));
        
        DocumentEntry docEntry = new DocumentEntry();
        docEntry.setEntryUUID("document01");
        docEntry.setTitle(new LocalizedString("Document 01", "en-US", "UTF8"));
        docEntry.setMimeType("application/octet-stream");
        
        Folder folder = new Folder();
        folder.setEntryUUID("folder01");
        folder.setTitle(new LocalizedString("Folder 01", "en-US", "UTF8"));
        
        Association docAssociation = new Association();
        docAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docAssociation.setSourceUUID("submissionSet01");
        docAssociation.setTargetUUID("document01");        

        Association folderAssociation = new Association();
        folderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        folderAssociation.setSourceUUID("submissionSet01");
        folderAssociation.setTargetUUID("folder01");
        
        Association docFolderAssociation = new Association();
        docFolderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docFolderAssociation.setSourceUUID("folder01");
        docFolderAssociation.setTargetUUID("document01");
        
        response = new QueryResponse();
        response.getSubmissionSets().add(submissionSet);
        response.getDocumentEntries().add(docEntry);
        response.getFolders().add(folder);
        response.getAssociations().add(docAssociation);
        response.getAssociations().add(folderAssociation);
        response.getAssociations().add(docFolderAssociation);
    }
    
    @Test
    public void testToEbXML() {
        EbXMLQueryResponse ebXML = transformer.toEbXML(response);
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
        
        List<ExtrinsicObject> docEntries = ebXML.getExtrinsicObjects(Vocabulary.DOC_ENTRY_CLASS_NODE);
        assertEquals(1, docEntries.size());
        assertEquals("document01", docEntries.get(0).getId());
        assertEquals("Document 01", docEntries.get(0).getName().getValue());
        
        List<RegistryPackage> folders = ebXML.getRegistryPackages(Vocabulary.FOLDER_CLASS_NODE);
        assertEquals(1, folders.size());
        assertEquals("Folder 01", folders.get(0).getName().getValue());
        
        List<RegistryPackage> submissionSets = ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        assertEquals(1, submissionSets.size());
        assertEquals("Submission Set 01", submissionSets.get(0).getName().getValue());
    }
    
    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }
    
    @Test
    public void testToEbXMLEmpty() {
        EbXMLQueryResponse result = transformer.toEbXML(new QueryResponse());
        assertNotNull(result);
        assertEquals(0, result.getAssociations().size());
        assertEquals(0, result.getExtrinsicObjects().size());
        assertEquals(0, result.getRegistryPackages().size());
    }
    

    
    
    @Test
    public void testFromEbXML() {
        EbXMLQueryResponse ebXML = transformer.toEbXML(response);
        QueryResponse result = transformer.fromEbXML(ebXML);
        
        assertEquals(response, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.toEbXML(null));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        EbXMLQueryResponse ebXML = transformer.toEbXML(new QueryResponse());
        assertEquals(new QueryResponse(), transformer.fromEbXML(ebXML));
    }
}
