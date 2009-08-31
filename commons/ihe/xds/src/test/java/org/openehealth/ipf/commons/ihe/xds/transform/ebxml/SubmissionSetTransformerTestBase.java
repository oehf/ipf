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
package org.openehealth.ipf.commons.ihe.xds.transform.ebxml;

import static org.junit.Assert.*;
import static org.openehealth.ipf.commons.ihe.xds.transform.ebxml.EbrsTestUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.metadata.Address;
import org.openehealth.ipf.commons.ihe.xds.metadata.Author;
import org.openehealth.ipf.commons.ihe.xds.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.metadata.Organization;
import org.openehealth.ipf.commons.ihe.xds.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.metadata.Recipient;
import org.openehealth.ipf.commons.ihe.xds.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.transform.ebxml.SubmissionSetTransformer;

/**
 * Tests for {@link SubmissionSetTransformer}.
 * @author Jens Riemschneider
 */
public abstract class SubmissionSetTransformerTestBase implements FactoryCreator {
    private SubmissionSetTransformer transformer;
    private SubmissionSet set;
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
        transformer = new SubmissionSetTransformer(factory);
        objectLibrary = factory.createObjectLibrary();
        
        Author author = new Author();
        author.setAuthorPerson(createPerson(1));
        author.getAuthorInstitution().add(new Organization("inst1"));
        author.getAuthorInstitution().add(new Organization("inst2"));
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

        set = new SubmissionSet();
        set.setAuthor(author);
        set.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        set.setComments(createLocal(10));
        set.setSubmissionTime("123");
        set.setEntryUuid("uuid");
        set.setPatientId(createIdentifiable(3));
        set.setTitle(createLocal(11));
        set.setUniqueId("uniqueId");
        set.setContentTypeCode(createCode(6));
        set.setSourceId("sourceId");
        set.getIntendedRecipients().add(new Recipient(createOrganization(20), createPerson(22)));
        set.getIntendedRecipients().add(new Recipient(createOrganization(21), null));
        set.getIntendedRecipients().add(new Recipient(null, createPerson(23)));

        if (homeAware) {
            set.setHomeCommunityId("123.456");
        }
    }

    @Test
    public void testToEbXML() {
        EbXMLRegistryPackage ebXML = transformer.toEbXML(set, objectLibrary);        
        assertNotNull(ebXML);
        
        assertEquals(AvailabilityStatus.APPROVED, ebXML.getStatus());
        assertEquals("uuid", ebXML.getId());
        assertNull(ebXML.getObjectType());
        if (homeAware) {
            assertEquals("123.456", ebXML.getHome());
        }

        assertEquals(createLocal(10), ebXML.getDescription());        
        assertEquals(createLocal(11), ebXML.getName());
        
        assertSlot(Vocabulary.SLOT_NAME_SUBMISSION_TIME, ebXML.getSlots(), "123");
        
        assertSlot(Vocabulary.SLOT_NAME_INTENDED_RECIPIENT, ebXML.getSlots(), 
                "orgName 20^^^^^namespace 20&uni 20&uniType 20^^^^id 20|id 22^familyName 22^givenName 22^prefix 22^second 22^suffix 22^^^namespace 22&uni 22&uniType 22",
                "orgName 21^^^^^namespace 21&uni 21&uniType 21^^^^id 21",
                "|id 23^familyName 23^givenName 23^prefix 23^second 23^suffix 23^^^namespace 23&uni 23&uniType 23");

        
        EbXMLClassification classification = assertClassification(Vocabulary.SUBMISSION_SET_AUTHOR_CLASS_SCHEME, ebXML, 0, "", -1);
        assertSlot(Vocabulary.SLOT_NAME_AUTHOR_PERSON, classification.getSlots(), "id 1^familyName 1^givenName 1^prefix 1^second 1^suffix 1^^^namespace 1&uni 1&uniType 1");
        assertSlot(Vocabulary.SLOT_NAME_AUTHOR_INSTITUTION, classification.getSlots(), "inst1", "inst2");
        assertSlot(Vocabulary.SLOT_NAME_AUTHOR_ROLE, classification.getSlots(), "role1", "role2");
        assertSlot(Vocabulary.SLOT_NAME_AUTHOR_SPECIALTY, classification.getSlots(), "spec1", "spec2");
        
        classification = assertClassification(Vocabulary.SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME, ebXML, 0, "code 6", 6);
        assertSlot(Vocabulary.SLOT_NAME_CODING_SCHEME, classification.getSlots(), "scheme 6");
        
        
        assertExternalIdentifier(Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID, ebXML, 
                "id 3^^^namespace 3&uni 3&uniType 3", Vocabulary.SUBMISSION_SET_LOCALIZED_STRING_PATIENT_ID);

        assertExternalIdentifier(Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID, ebXML, 
                "uniqueId", Vocabulary.SUBMISSION_SET_LOCALIZED_STRING_UNIQUE_ID);

        assertExternalIdentifier(Vocabulary.SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID, ebXML, 
                "sourceId", Vocabulary.SUBMISSION_SET_LOCALIZED_STRING_SOURCE_ID);
        
        assertEquals(2, ebXML.getClassifications().size());
        assertEquals(2, ebXML.getSlots().size());
        assertEquals(3, ebXML.getExternalIdentifiers().size());
    }

    @Test
    public void testToEbXMLNull() {
        assertNull(transformer.toEbXML(null, objectLibrary));
    }
   
    @Test
    public void testToEbXMLEmpty() {
        EbXMLRegistryPackage ebXML = transformer.toEbXML(new SubmissionSet(), objectLibrary);        
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
        EbXMLRegistryPackage ebXML = transformer.toEbXML(set, objectLibrary);
        SubmissionSet result = transformer.fromEbXML(ebXML);
        
        assertNotNull(result);
        assertEquals(set, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        assertNull(transformer.fromEbXML(null));
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        EbXMLRegistryPackage ebXML = transformer.toEbXML(new SubmissionSet(), objectLibrary);
        SubmissionSet result = transformer.fromEbXML(ebXML);
        assertEquals(new SubmissionSet(), result);
    }
}
