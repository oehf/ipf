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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebrs21;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Address;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Author;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.EntryUUID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Name;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.PatientInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.UniqueID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LocalizedStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;

/**
 * Tests for {@link DocumentEntryTransformer}.
 * @author Jens Riemschneider
 */
public class DocumentEntryTransformerTest {
    private DocumentEntryTransformer transformer;
    private DocumentEntry documentEntry;
    
    @Before
    public void setUp() {
        transformer = new DocumentEntryTransformer();

        Author author = new Author();
        author.setAuthorPerson(createPerson(1));
        author.getAuthorInstitution().add("inst1");
        author.getAuthorInstitution().add("inst2");
        author.getAuthorRole().add("role1");
        author.getAuthorRole().add("role2");
        author.getAuthorSpecialty().add("spec1");
        author.getAuthorSpecialty().add("spec2");
        
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
        documentEntry.setAuthor(author);
        documentEntry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        documentEntry.setClassCode(createCode(1));
        documentEntry.setComments(createLocalized(10));
        documentEntry.setCreationTime("123");
        documentEntry.setEntryUUID(new EntryUUID("uuid"));
        documentEntry.setFormatCode(createCode(2));
        documentEntry.setHash("hash");
        documentEntry.setHealthcareFacilityTypeCode(createCode(3));
        documentEntry.setLanguageCode("languageCode");
        documentEntry.setLegalAuthenticator(createPerson(2));
        documentEntry.setMimeType("text/plain");
        documentEntry.setPatientID(createIdentifiable(3));
        documentEntry.setPracticeSettingCode(createCode(4));
        documentEntry.setServiceStartTime("234");
        documentEntry.setServiceStopTime("345");
        documentEntry.setSize(174L);
        documentEntry.setSourcePatientID(createIdentifiable(4));
        documentEntry.setSourcePatientInfo(sourcePatientInfo);
        documentEntry.setTitle(createLocalized(11));
        documentEntry.setTypeCode(createCode(5));
        documentEntry.setUniqueID(new UniqueID("uniqueId"));
        documentEntry.setUri("uri");
        documentEntry.getConfidentialityCodes().add(createCode(6));
        documentEntry.getConfidentialityCodes().add(createCode(7));
        documentEntry.getEventCodeList().add(createCode(8));
        documentEntry.getEventCodeList().add(createCode(9));
    }

    @Test
    public void testToEbXML21() {
        ExtrinsicObjectType ebXML = transformer.toEbXML21(documentEntry);        
        assertNotNull(ebXML);
        
        assertEquals("Approved", ebXML.getStatus());
        assertEquals("text/plain", ebXML.getMimeType());
        assertEquals("uuid", ebXML.getId());
        
        assertLocalized(10, getSingleLocalized(ebXML.getDescription()));        
        assertLocalized(11, getSingleLocalized(ebXML.getName()));
        
        assertSlot(Vocabulary.SLOT_NAME_CREATION_TIME, ebXML.getSlot(), "123");
        assertSlot(Vocabulary.SLOT_NAME_HASH, ebXML.getSlot(), "hash");
        assertSlot(Vocabulary.SLOT_NAME_LANGUAGE_CODE, ebXML.getSlot(), "languageCode");
        assertSlot(Vocabulary.SLOT_NAME_SERVICE_START_TIME, ebXML.getSlot(), "234");
        assertSlot(Vocabulary.SLOT_NAME_SERVICE_STOP_TIME, ebXML.getSlot(), "345");
        assertSlot(Vocabulary.SLOT_NAME_SIZE, ebXML.getSlot(), "174");
        assertSlot(Vocabulary.SLOT_NAME_SOURCE_PATIENT_ID, ebXML.getSlot(), "id 4^^^namespace 4&uni 4&uniType 4");
        assertSlot(Vocabulary.SLOT_NAME_URI, ebXML.getSlot(), "1|uri");
        assertSlot(Vocabulary.SLOT_NAME_LEGAL_AUTHENTICATOR, ebXML.getSlot(), "id 2^familyName 2^givenName 2^prefix 2^second 2^suffix 2^^^namespace 2&uni 2&uniType 2");
        assertSlot(Vocabulary.SLOT_NAME_SOURCE_PATIENT_INFO, ebXML.getSlot(), 
                "PID-3|id 5^^^namespace 5&uni 5&uniType 5~id 6^^^namespace 6&uni 6&uniType 6",
                "PID-5|familyName 3^givenName 3^prefix 3^second 3^suffix 3",
                "PID-7|dateOfBirth",
                "PID-8|F",
                "PID-11|streetAddress^otherDesignation^city^stateOrProvince^zipOrPostalCode^country^^^countyParishCode");
        
        
        ClassificationType classification = assertClassification(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "", -1);
        assertSlot(Vocabulary.SLOT_NAME_AUTHOR_PERSON, classification.getSlot(), "id 1^familyName 1^givenName 1^prefix 1^second 1^suffix 1^^^namespace 1&uni 1&uniType 1");
        assertSlot(Vocabulary.SLOT_NAME_AUTHOR_INSTITUTION, classification.getSlot(), "inst1", "inst2");
        assertSlot(Vocabulary.SLOT_NAME_AUTHOR_ROLE, classification.getSlot(), "role1", "role2");
        assertSlot(Vocabulary.SLOT_NAME_AUTHOR_SPECIALTY, classification.getSlot(), "spec1", "spec2");
        
        classification = assertClassification(Vocabulary.DOC_ENTRY_CLASS_CODE_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "code 1", 1);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 1");
        
        classification = assertClassification(Vocabulary.DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "code 6", 6);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 6");

        classification = assertClassification(Vocabulary.DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME, ebXML.getClassification(), 1, ebXML, "code 7", 7);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 7");

        classification = assertClassification(Vocabulary.DOC_ENTRY_EVENT_CODE_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "code 8", 8);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 8");

        classification = assertClassification(Vocabulary.DOC_ENTRY_EVENT_CODE_CLASS_SCHEME, ebXML.getClassification(), 1, ebXML, "code 9", 9);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 9");
        
        classification = assertClassification(Vocabulary.DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "code 2", 2);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 2");

        classification = assertClassification(Vocabulary.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "code 3", 3);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 3");
        
        classification = assertClassification(Vocabulary.DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "code 4", 4);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 4");

        classification = assertClassification(Vocabulary.DOC_ENTRY_TYPE_CODE_CLASS_SCHEME, ebXML.getClassification(), 0, ebXML, "code 5", 5);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlot(), "scheme 5");
        
        assertExternalIdentifier(Vocabulary.DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, ebXML.getExternalIdentifier(), 
                "id 3^^^namespace 3&uni 3&uniType 3", Vocabulary.LOCALIZED_STRING_PATIENT_ID);

        assertExternalIdentifier(Vocabulary.DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID, ebXML.getExternalIdentifier(), 
                "uniqueId", Vocabulary.LOCALIZED_STRING_UNIQUE_ID);
        
        assertEquals(10, ebXML.getClassification().size());
        assertEquals(10, ebXML.getSlot().size());
        assertEquals(2, ebXML.getExternalIdentifier().size());
    }


    
    
    
    private void assertExternalIdentifier(String scheme, List<ExternalIdentifierType> identifiers, 
            String expectedValue, String name) {
        int found = 0; 
        for (ExternalIdentifierType identifier : identifiers) {
            if (identifier.getIdentificationScheme().equals(scheme)) {
                ++found;
                assertEquals(expectedValue, identifier.getValue());
                LocalizedStringType localized = getSingleLocalized(identifier.getName());
                assertEquals(name, localized.getValue());
                assertEquals("UTF-8", localized.getCharset());
                assertEquals("en-us", localized.getLang());
            }
        }
        
        assertEquals(1, found);
    }

    private ClassificationType assertClassification(String scheme, List<ClassificationType> classifications, int occurrence, Object classifiedObject, String nodeRepresentation, int localizedIdx) {
        int found = 0;        
        ClassificationType foundClassification = null;
        for (ClassificationType classification : classifications) {
            if (classification.getClassificationScheme().equals(scheme)) {
                if (occurrence == 0) {
                    assertSame(classifiedObject, classification.getClassifiedObject());
                    assertEquals(nodeRepresentation, classification.getNodeRepresentation());
                    if (localizedIdx > 0) {
                        assertLocalized(localizedIdx, getSingleLocalized(classification.getName()));
                    }
                    foundClassification = classification;
                    ++found;
                }
                --occurrence;
            }
        }
        
        assertEquals(1, found);
        return foundClassification;
    }

    private void assertSlot(String slotName, List<SlotType1> slots, String... expectedSlotValues) {
        int found = 0;
        for (SlotType1 slot : slots) {
            if (slot.getName().equals(slotName)) {
                List<String> values = slot.getValueList().getValue();
                for (String expectedValue : expectedSlotValues) {
                    assertTrue("Not found: " + expectedValue + ", was: " + values, values.contains(expectedValue));
                }
                assertEquals(values.size(), expectedSlotValues.length);
                ++found;
            }
        }
        
        assertEquals(1, found);
    }

    private LocalizedStringType getSingleLocalized(InternationalStringType value) {
        List<LocalizedStringType> localizedList = value.getLocalizedString();
        assertEquals(1, localizedList.size());
        return localizedList.get(0);
    }
    
    private void assertLocalized(int idx, LocalizedStringType localized) {
        assertEquals("charset " + idx, localized.getCharset());
        assertEquals("lang " + idx, localized.getLang());
        assertEquals("value " + idx, localized.getValue());
    }

    private Person createPerson(int idx) {
        Person person = new Person();
        person.setId(createIdentifiable(idx));
        person.setName(createName(idx));
        return person;
    }

    private Name createName(int idx) {
        return new Name("familyName " + idx, "givenName " + idx, "prefix " + idx, "second " + idx, "suffix " + idx);
    }

    private Identifiable createIdentifiable(int idx) {
        return new Identifiable("id " + idx, createAssigningAuthority(idx));
    }

    private AssigningAuthority createAssigningAuthority(int idx) {
        return new AssigningAuthority("namespace " + idx, "uni " + idx, "uniType " + idx);
    }

    private Code createCode(int idx) {
        return new Code("code " + idx, createLocalized(idx), "scheme " + idx);
    }

    private LocalizedString createLocalized(int idx) {
        return new LocalizedString("value " + idx, "lang " + idx, "charset " + idx);
    }    
}
