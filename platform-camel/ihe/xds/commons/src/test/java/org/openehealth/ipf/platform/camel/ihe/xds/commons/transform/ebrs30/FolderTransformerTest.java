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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30;

import static org.junit.Assert.*;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs30.Ebrs30TestUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.EntryUUID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.UniqueID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryPackageType;

/**
 * Tests for {@link FolderTransformer}.
 * @author Jens Riemschneider
 */
public class FolderTransformerTest {
    private FolderTransformer transformer;
    private Folder folder;
    
    @Before
    public void setUp() {
        transformer = new FolderTransformer();

        folder = new Folder();
        folder.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        folder.setComments(createLocal(10));
        folder.setEntryUUID(new EntryUUID("uuid"));
        folder.setLastUpdateTime("123");
        folder.setPatientID(createIdentifiable(3));
        folder.setTitle(createLocal(11));
        folder.setUniqueID(new UniqueID("uniqueId"));
        folder.getCodeList().add(createCode(6));
        folder.getCodeList().add(createCode(7));
    }

    @Test
    public void testToEbXML30() {
        RegistryPackageType ebXML = transformer.toEbXML30(folder);        
        assertNotNull(ebXML);
        
        assertEquals("Approved", ebXML.getStatus());
        assertEquals("uuid", ebXML.getId());
        assertNull(ebXML.getObjectType());
        
        assertEquals(createLocal(10), toLocal(ebXML.getDescription()));        
        assertEquals(createLocal(11), toLocal(ebXML.getName()));
        
        assertSlot(Vocabulary.SLOT_NAME_LAST_UPDATE_TIME, ebXML.getSlot(), "123");
        
        ClassificationType classification = 
            assertClassification(Vocabulary.FOLDER_CODE_LIST_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "code 6", 6);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 6");

        classification = assertClassification(Vocabulary.FOLDER_CODE_LIST_CLASS_SCHEME, ebXML.getClassification(), 1, ebXML, "code 7", 7);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 7");
        
        assertExternalIdentifier(Vocabulary.FOLDER_PATIENT_ID_EXTERNAL_ID, ebXML.getExternalIdentifier(), 
                "id 3^^^namespace 3&uni 3&uniType 3", Vocabulary.FOLDER_LOCALIZED_STRING_PATIENT_ID);

        assertExternalIdentifier(Vocabulary.FOLDER_UNIQUE_ID_EXTERNAL_ID, ebXML.getExternalIdentifier(), 
                "uniqueId", Vocabulary.FOLDER_LOCALIZED_STRING_UNIQUE_ID);
        
        assertEquals(2, ebXML.getClassification().size());
        assertEquals(1, ebXML.getSlot().size());
        assertEquals(2, ebXML.getExternalIdentifier().size());
    }

    @Test
    public void testToEbXML30Null() {
        assertNull(transformer.toEbXML30(null));
    }
   
    @Test
    public void testToEbXML30Empty() {
        RegistryPackageType ebXML = transformer.toEbXML30(new Folder());        
        assertNotNull(ebXML);
        
        assertNull(ebXML.getStatus());
        assertNull(ebXML.getId());
        
        assertNull(ebXML.getDescription());        
        assertNull(ebXML.getName());
        
        assertEquals(0, ebXML.getSlot().size());
        assertEquals(0, ebXML.getClassification().size());
        assertEquals(0, ebXML.getExternalIdentifier().size());
    }
    
    
    
    
    @Test
    public void testFromEbXML30() {
        RegistryPackageType ebXML = transformer.toEbXML30(folder);
        Folder result = transformer.fromEbXML30(ebXML);
        
        assertNotNull(result);
        assertEquals(folder, result);
    }
    
    @Test
    public void testFromEbXML30Null() {
        assertNull(transformer.fromEbXML30(null));
    }
    
    @Test
    public void testFromEbXML30Empty() {
        Folder result = transformer.fromEbXML30(new RegistryPackageType());
        assertEquals(new Folder(), result);
    }
}
