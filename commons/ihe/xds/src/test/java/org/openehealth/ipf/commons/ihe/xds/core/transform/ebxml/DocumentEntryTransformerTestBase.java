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
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.EbrsTestUtils.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

/**
 * Tests for {@link DocumentEntryTransformer}.
 * @author Jens Riemschneider
 */
public abstract class DocumentEntryTransformerTestBase implements FactoryCreator {
    private DocumentEntryTransformer transformer;
    private DocumentEntry documentEntry;
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
        transformer = new DocumentEntryTransformer(factory);
        objectLibrary = factory.createObjectLibrary();
        
        Author author1 = new Author();
        author1.setAuthorPerson(createPerson(1));
        author1.getAuthorInstitution().add(new Organization("inst1"));
        author1.getAuthorInstitution().add(new Organization("inst2"));
        author1.getAuthorRole().add("role1");
        author1.getAuthorRole().add("role2");
        author1.getAuthorSpecialty().add("spec1");
        author1.getAuthorSpecialty().add("spec2");
        
        Author author2 = new Author();
        author2.setAuthorPerson(createPerson(30));
        author2.getAuthorInstitution().add(new Organization("inst3"));
        author2.getAuthorInstitution().add(new Organization("inst4"));
        author2.getAuthorRole().add("role3");
        author2.getAuthorRole().add("role4");
        author2.getAuthorSpecialty().add("spec3");
        author2.getAuthorSpecialty().add("spec4");
        
        Address address = new Address();
        address.setCity("city");
        address.setCountry("country");
        address.setCountyParishCode("countyParishCode");
        address.setOtherDesignation("otherDesignation");
        address.setStateOrProvince("stateOrProvince");
        address.setStreetAddress("streetAddress");
        address.setZipOrPostalCode("zipOrPostalCode");
        
        PatientInfo sourcePatientInfo = new PatientInfo();
        sourcePatientInfo.setAddress(address);
        sourcePatientInfo.setDateOfBirth("dateOfBirth");
        sourcePatientInfo.setGender("F");
        sourcePatientInfo.setName(createName(3));
        sourcePatientInfo.getIds().add(createIdentifiable(5));
        sourcePatientInfo.getIds().add(createIdentifiable(6));

        documentEntry = new DocumentEntry();
        documentEntry.getAuthors().add(author1);
        documentEntry.getAuthors().add(author2);
        documentEntry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        documentEntry.setClassCode(createCode(1));
        documentEntry.setComments(createLocal(10));
        documentEntry.setCreationTime("123");
        documentEntry.setEntryUuid("uuid");
        documentEntry.setFormatCode(createCode(2));
        documentEntry.setHash("hash");
        documentEntry.setHealthcareFacilityTypeCode(createCode(3));
        documentEntry.setLanguageCode("languageCode");
        documentEntry.setLegalAuthenticator(createPerson(2));
        documentEntry.setMimeType("text/plain");
        documentEntry.setPatientId(createIdentifiable(3));
        documentEntry.setPracticeSettingCode(createCode(4));
        documentEntry.setServiceStartTime("234");
        documentEntry.setServiceStopTime("345");
        documentEntry.setSize(174L);
        documentEntry.setSourcePatientId(createIdentifiable(4));
        documentEntry.setSourcePatientInfo(sourcePatientInfo);
        documentEntry.setTitle(createLocal(11));
        documentEntry.setTypeCode(createCode(5));
        documentEntry.setUniqueId("uniqueId");
        documentEntry.setUri("uri");
        documentEntry.getConfidentialityCodes().add(createCode(6));
        documentEntry.getConfidentialityCodes().add(createCode(7));
        documentEntry.getEventCodeList().add(createCode(8));
        documentEntry.getEventCodeList().add(createCode(9));
        documentEntry.setRepositoryUniqueId("repo1");
        
        if (homeAware) {
            documentEntry.setHomeCommunityId("123.456");
        }
    }

    @Test
    public void testToEbXML() {
        EbXMLExtrinsicObject ebXML = transformer.toEbXML(documentEntry, objectLibrary);        
        assertNotNull(ebXML);
        
        assertEquals(AvailabilityStatus.APPROVED, ebXML.getStatus());
        assertEquals("text/plain", ebXML.getMimeType());
        assertEquals("uuid", ebXML.getId());
        assertEquals(DocumentEntryType.STABLE.getUuid(), ebXML.getObjectType());
        if (homeAware) {
            assertEquals("123.456", ebXML.getHome());
        }
        
        assertEquals(createLocal(10), ebXML.getDescription());        
        assertEquals(createLocal(11), ebXML.getName());
        
        List<EbXMLSlot> slots = ebXML.getSlots();
        assertSlot(SLOT_NAME_CREATION_TIME, slots, "123");
        assertSlot(SLOT_NAME_HASH, slots, "hash");
        assertSlot(SLOT_NAME_LANGUAGE_CODE, slots, "languageCode");
        assertSlot(SLOT_NAME_SERVICE_START_TIME, slots, "234");
        assertSlot(SLOT_NAME_SERVICE_STOP_TIME, slots, "345");
        assertSlot(SLOT_NAME_SIZE, slots, "174");
        assertSlot(SLOT_NAME_SOURCE_PATIENT_ID, slots, "id 4^^^namespace 4&uni 4&uniType 4");
        assertSlot(SLOT_NAME_URI, slots, "1|uri");
        assertSlot(SLOT_NAME_LEGAL_AUTHENTICATOR, slots, "id 2^familyName 2^givenName 2^prefix 2^second 2^suffix 2^^^namespace 2&uni 2&uniType 2");
        assertSlot(SLOT_NAME_REPOSITORY_UNIQUE_ID, slots, "repo1");
        assertSlot(SLOT_NAME_SOURCE_PATIENT_INFO, slots, 
                "PID-3|id 5^^^namespace 5&uni 5&uniType 5",
                "PID-3|id 6^^^namespace 6&uni 6&uniType 6",
                "PID-5|familyName 3^givenName 3^prefix 3^second 3^suffix 3",
                "PID-7|dateOfBirth",
                "PID-8|F",
                "PID-11|streetAddress^otherDesignation^city^stateOrProvince^zipOrPostalCode^country^^^countyParishCode");
        
        
        EbXMLClassification classification = assertClassification(DOC_ENTRY_AUTHOR_CLASS_SCHEME, ebXML, 0, "", -1);
        assertSlot(SLOT_NAME_AUTHOR_PERSON, classification.getSlots(), "id 1^familyName 1^givenName 1^prefix 1^second 1^suffix 1^^^namespace 1&uni 1&uniType 1");
        assertSlot(SLOT_NAME_AUTHOR_INSTITUTION, classification.getSlots(), "inst1", "inst2");
        assertSlot(SLOT_NAME_AUTHOR_ROLE, classification.getSlots(), "role1", "role2");
        assertSlot(SLOT_NAME_AUTHOR_SPECIALTY, classification.getSlots(), "spec1", "spec2");
        
        classification = assertClassification(DOC_ENTRY_AUTHOR_CLASS_SCHEME, ebXML, 1, "", -1);
        assertSlot(SLOT_NAME_AUTHOR_PERSON, classification.getSlots(), "id 30^familyName 30^givenName 30^prefix 30^second 30^suffix 30^^^namespace 30&uni 30&uniType 30");
        assertSlot(SLOT_NAME_AUTHOR_INSTITUTION, classification.getSlots(), "inst3", "inst4");
        assertSlot(SLOT_NAME_AUTHOR_ROLE, classification.getSlots(), "role3", "role4");
        assertSlot(SLOT_NAME_AUTHOR_SPECIALTY, classification.getSlots(), "spec3", "spec4");
        
        classification = assertClassification(DOC_ENTRY_CLASS_CODE_CLASS_SCHEME, ebXML, 0, "code 1", 1);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 1");
        
        classification = assertClassification(DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME, ebXML, 0, "code 6", 6);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 6");

        classification = assertClassification(DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME, ebXML, 1, "code 7", 7);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 7");

        classification = assertClassification(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME, ebXML, 0, "code 8", 8);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 8");

        classification = assertClassification(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME, ebXML, 1, "code 9", 9);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 9");
        
        classification = assertClassification(DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME, ebXML, 0, "code 2", 2);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 2");

        classification = assertClassification(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, ebXML, 0, "code 3", 3);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 3");
        
        classification = assertClassification(DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME, ebXML, 0, "code 4", 4);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 4");

        classification = assertClassification(DOC_ENTRY_TYPE_CODE_CLASS_SCHEME, ebXML, 0, "code 5", 5);
        assertSlot(SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 5");
        
        assertExternalIdentifier(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, ebXML, 
                "id 3^^^namespace 3&uni 3&uniType 3", DOC_ENTRY_LOCALIZED_STRING_PATIENT_ID);

        assertExternalIdentifier(DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID, ebXML, 
                "uniqueId", DOC_ENTRY_LOCALIZED_STRING_UNIQUE_ID);
        
        assertEquals(11, ebXML.getClassifications().size());
        assertEquals(11, ebXML.getSlots().size());
        assertEquals(2, ebXML.getExternalIdentifiers().size());
    }

    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null, objectLibrary));
    }
   
    @Test
    public void testToEbXMLEmpty() {
        EbXMLExtrinsicObject ebXML = transformer.toEbXML(new DocumentEntry(), objectLibrary);        
        assertNotNull(ebXML);
        
        assertNull(ebXML.getStatus());
        assertEquals("application/octet-stream", ebXML.getMimeType());
        assertNull(ebXML.getId());
        
        assertNull(ebXML.getDescription());        
        assertNull(ebXML.getName());
        
        assertEquals(0, ebXML.getSlots().size());
        assertEquals(0, ebXML.getClassifications().size());
        assertEquals(0, ebXML.getExternalIdentifiers().size());
    }
    
    
    
    @Test
    public void testFromEbXML() {
        EbXMLExtrinsicObject ebXML = transformer.toEbXML(documentEntry, objectLibrary);
        DocumentEntry result = transformer.fromEbXML(ebXML);
        
        assertNotNull(result);
        assertEquals(documentEntry.toString(), result.toString());
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        EbXMLExtrinsicObject ebXML = transformer.toEbXML(new DocumentEntry(), objectLibrary);        
        DocumentEntry result = transformer.fromEbXML(ebXML);
        
        DocumentEntry expected = new DocumentEntry();
        expected.setMimeType("application/octet-stream");
        assertEquals(expected, result);
    }
}
