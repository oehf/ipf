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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import static org.junit.Assert.*;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.EbrsTestUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;

/**
 * Tests for {@link FolderTransformer}.
 * @author Jens Riemschneider
 */
public abstract class FolderTransformerTestBase implements FactoryCreator {
    private FolderTransformer transformer;
    private Folder folder;
    private EbXMLObjectLibrary objectLibrary;
    private boolean homeAware = true;

    /**
     * @param homeAware
     *          <code>true</code> to enable comparison of the homeCommunityId.
     */
    protected void setHomeAware(boolean homeAware) {
        this.homeAware = homeAware;
    }
    
    @Before
    public final void baseSetUp() {
        EbXMLFactory factory = createFactory();
        transformer = new FolderTransformer(factory);
        objectLibrary = factory.createObjectLibrary();
        
        folder = new Folder();
        folder.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        folder.setComments(createLocal(10));
        folder.setEntryUuid("uuid");
        folder.setLastUpdateTime("123");
        folder.setPatientId(createIdentifiable(3));
        folder.setTitle(createLocal(11));
        folder.setUniqueId("uniqueId");
        folder.getCodeList().add(createCode(6));
        folder.getCodeList().add(createCode(7));

        if (homeAware) {
            folder.setHomeCommunityId("123.456");
        }
    }

    @Test
    public void testToEbXML() {
        EbXMLRegistryPackage ebXML = transformer.toEbXML(folder, objectLibrary);        
        assertNotNull(ebXML);
        
        assertEquals(AvailabilityStatus.APPROVED, ebXML.getStatus());
        assertEquals("uuid", ebXML.getId());
        assertNull(ebXML.getObjectType());
        if (homeAware) {
            assertEquals("123.456", ebXML.getHome());
        }
        
        assertEquals(createLocal(10), ebXML.getDescription());        
        assertEquals(createLocal(11), ebXML.getName());
        
        assertSlot(Vocabulary.SLOT_NAME_LAST_UPDATE_TIME, ebXML.getSlots(), "123");
        
        EbXMLClassification classification = 
            assertClassification(Vocabulary.FOLDER_CODE_LIST_CLASS_SCHEME, ebXML, 0, "code 6", 6);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 6");

        classification = assertClassification(Vocabulary.FOLDER_CODE_LIST_CLASS_SCHEME, ebXML, 1, "code 7", 7);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 7");
        
        assertExternalIdentifier(Vocabulary.FOLDER_PATIENT_ID_EXTERNAL_ID, ebXML, 
                "id 3^^^&uni 3&uniType 3", Vocabulary.FOLDER_LOCALIZED_STRING_PATIENT_ID);

        assertExternalIdentifier(Vocabulary.FOLDER_UNIQUE_ID_EXTERNAL_ID, ebXML, 
                "uniqueId", Vocabulary.FOLDER_LOCALIZED_STRING_UNIQUE_ID);
        
        assertEquals(2, ebXML.getClassifications().size());
        assertEquals(1, ebXML.getSlots().size());
        assertEquals(2, ebXML.getExternalIdentifiers().size());
    }

    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null, objectLibrary));
    }
   
    @Test
    public void testToEbXMLEmpty() {
        EbXMLRegistryPackage ebXML = transformer.toEbXML(new Folder(), objectLibrary);        
        assertNotNull(ebXML);
        
        assertNull(ebXML.getStatus());
        assertNull(ebXML.getId());
        
        assertNull(ebXML.getDescription());        
        assertNull(ebXML.getName());
        
        assertEquals(0, ebXML.getSlots().size());
        assertEquals(0, ebXML.getClassifications().size());
        assertEquals(0, ebXML.getExternalIdentifiers().size());
    }
    
    
    
    
    @Test
    public void testFromEbXML() {
        EbXMLRegistryPackage ebXML = transformer.toEbXML(folder, objectLibrary);
        Folder result = transformer.fromEbXML(ebXML);
        
        assertNotNull(result);
        assertEquals(folder, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        EbXMLRegistryPackage ebXML = transformer.toEbXML(new Folder(), objectLibrary);
        Folder result = transformer.fromEbXML(ebXML);
        assertEquals(new Folder(), result);
    }
}
