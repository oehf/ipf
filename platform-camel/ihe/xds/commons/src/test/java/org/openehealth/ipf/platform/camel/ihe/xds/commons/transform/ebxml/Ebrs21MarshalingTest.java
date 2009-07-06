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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.ExtrinsicObject21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21.SubmitObjectsRequest21;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Address;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Author;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Name;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.PatientInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Person;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryEntryType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.requests.RegisterDocumentSetTransformer;

/**
 * Tests for marshaling objects created with our ebxml 2.1 classes.
 * @author Jens Riemschneider
 */
public class Ebrs21MarshalingTest {
    private SubmitObjectsRequest request;
    private ExtrinsicObject docEntry;
    private EbXMLFactory21 factory;
    private ObjectLibrary objectLibrary;

    @Before
    public void setUp() {        
        factory = new EbXMLFactory21();
        
        objectLibrary = factory.createObjectLibrary();
        
        request = new SubmitObjectsRequest();
        LeafRegistryObjectListType objListElement = new LeafRegistryObjectListType();
        request.setLeafRegistryObjectList(objListElement);
        List<Object> objList = objListElement.getObjectRefOrAssociationOrAuditableEvent();
        objList.addAll(objectLibrary.getObjects());

        docEntry = factory.createExtrinsic("Document01", objectLibrary);
        docEntry.setObjectType(Vocabulary.DOC_ENTRY_CLASS_NODE);
        objList.add(((ExtrinsicObject21)docEntry).getInternal());
    }
    
    @Test
    public void testCreateClassification() throws Exception {        
        Classification classification = factory.createClassification(objectLibrary);
        classification.setClassifiedObject(docEntry.getId());
        docEntry.addClassification(classification, Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        
        SubmitObjectsRequest received = send();
        
        ExtrinsicObject docEntryResult = getDocumentEntry(received);
        assertEquals(1, docEntryResult.getClassifications().size());
        Classification classificationResult = docEntryResult.getClassifications().get(0);
        assertNotNull(classificationResult);
        
        assertEquals(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME, classificationResult.getClassificationScheme());
        assertNull(classificationResult.getNodeRepresentation());
        assertSame(docEntryResult.getId(), classificationResult.getClassifiedObject());
    }
    
    private ExtrinsicObject getDocumentEntry(SubmitObjectsRequest received) {
        ObjectLibrary objectLibraryOfRequest = new ObjectLibrary();
        
        for (Object obj : received.getLeafRegistryObjectList().getObjectRefOrAssociationOrAuditableEvent()) {
            if (obj instanceof ObjectRefType) {
                objectLibraryOfRequest.put(((ObjectRefType) obj).getId(), obj);
            }
            if (obj instanceof RegistryEntryType) {
                objectLibraryOfRequest.put(((RegistryEntryType) obj).getId(), obj);
            }
            
            if (obj instanceof ExtrinsicObjectType) {                
                return ExtrinsicObject21.create((ExtrinsicObjectType)obj, objectLibraryOfRequest);
            }
        }
        fail("Document entry not found");
        return null;
    }

    @Test
    public void testAddSlot() throws Exception {
        Classification classification = factory.createClassification(objectLibrary);
        docEntry.addClassification(classification, Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        
        classification.addSlot("something", "a", "b", "c");

        SubmitObjectsRequest received = send();        
        ExtrinsicObject docEntryResult = getDocumentEntry(received);
        List<Slot> slots = docEntryResult.getClassifications().get(0).getSlots();        
        assertEquals(1, slots.size());        
        
        Slot slot = slots.get(0);
        assertEquals("something", slot.getName());
        
        List<String> values = slot.getValueList();
        assertEquals(3, values.size());
        assertTrue(values.contains("a"));
        assertTrue(values.contains("b"));
        assertTrue(values.contains("c"));
    }

    private SubmitObjectsRequest send() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs");
        Marshaller marshaller = context.createMarshaller();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
        marshaller.marshal(request, outputStream);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        SubmitObjectsRequest received = (SubmitObjectsRequest) unmarshaller.unmarshal(inputStream);
        return received;
    }
    
    @Test
    public void testFromRealEbXML() throws Exception {
        Author author1 = new Author();
        author1.setAuthorPerson(new Person(null, new Name("Smitty", "Gerald", null, null, null)));
        author1.getAuthorInstitution().add("Cleveland Clinic");
        author1.getAuthorInstitution().add("Parma Community");
        author1.getAuthorRole().add("Attending");
        author1.getAuthorSpecialty().add("Orthopedic");

        Author author2 = new Author();
        author2.setAuthorPerson(new Person(null, new Name("Dopplemeyer", "Sherry", null, null, null)));
        author2.getAuthorInstitution().add("Cleveland Clinic");
        author2.getAuthorInstitution().add("Berea Community");
        author2.getAuthorRole().add("Primary Surgon");
        author2.getAuthorSpecialty().add("Orthopedic");
        
        Address address = new Address();
        address.setCity("Metropolis");
        address.setStreetAddress("100 Main St");
        address.setCountry("USA");
        address.setStateOrProvince("Il");
        address.setZipOrPostalCode("44130");
        
        PatientInfo sourcePatientInfo = new PatientInfo();
        sourcePatientInfo.getIds().add(new Identifiable("pid1", new AssigningAuthority("domain", null, null)));
        sourcePatientInfo.setAddress(address);
        sourcePatientInfo.setDateOfBirth("19560527");
        sourcePatientInfo.setGender("M");
        sourcePatientInfo.setName(new Name("Doe", "John", null, null, null));
        
        DocumentEntry docEntry = new DocumentEntry();
        docEntry.getAuthors().add(author1);
        docEntry.getAuthors().add(author2);        
        docEntry.setClassCode(new Code("Communication", new LocalizedString("Communication", "en-us", "UTF-8"), "Connect-a-thon classCodes"));
        docEntry.getConfidentialityCodes().add(new Code("1.3.6.1.4.1.21367.2006.7.101", new LocalizedString("Clinical-Staff", "en-us", "UTF-8"), "Connect-a-thon confidentialityCodes"));
        docEntry.setCreationTime("20041224");
        docEntry.getEventCodeList().add(new Code("Colonoscopy", new LocalizedString("Colonoscopy", "en-us", "UTF-8"), "Connect-a-thon eventCodeList"));
        docEntry.setFormatCode(new Code("CDAR2/IHE 1.0", new LocalizedString("CDAR2/IHE 1.0", "en-us", "UTF-8"), "Connect-a-thon formatCodes"));
        docEntry.setHash("fbe2351a6a8ceba1a04ba3f832a12a53befeb04c");
        docEntry.setHealthcareFacilityTypeCode(new Code("Assisted Living", new LocalizedString("Assisted Living", "en-us", "UTF-8"), "Connect-a-thon healthcareFacilityTypeCodes"));
        docEntry.setLanguageCode("en-us");
        docEntry.setMimeType("text/plain");
        docEntry.setPracticeSettingCode(new Code("Cardiology", new LocalizedString("Cardiology", "en-us", "UTF-8"), "Connect-a-thon practiceSettingCodes"));
        docEntry.setServiceStartTime("200412230800");
        docEntry.setServiceStopTime("200412241600");
        docEntry.setSize(36L);
        docEntry.setSourcePatientID(new Identifiable("89765a87b", new AssigningAuthority("fj34r", null, null)));
        docEntry.setSourcePatientInfo(sourcePatientInfo);
        docEntry.setTypeCode(new Code("34108-1", new LocalizedString("Outpatient Evaluation And Management", "en-us", "UTF-8"), "LOINC"));
        docEntry.setEntryUUID("Document01");
        docEntry.setPatientID(new Identifiable("SELF-5", new AssigningAuthority(null, "1.3.6.1.4.1.21367.2005.3.7", "ISO")));
        docEntry.setTitle(new LocalizedString("Physical", "en-us", "UTF-8"));
        docEntry.setUniqueID("Document01_uniqueid");

        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.setAuthor(author2);
        submissionSet.setContentTypeCode(new Code("History and Physical", new LocalizedString("History and Physical", "en-us", "UTF-8"), "Connect-a-thon contentTypeCodes"));
        submissionSet.setSourceID("129.6.58.92.1.1");
        submissionSet.setSubmissionTime("20041225235050");
        submissionSet.setComments(new LocalizedString("Annual physical", "en-us", "UTF-8"));
        submissionSet.setEntryUUID("SubmissionSet01");
        submissionSet.setPatientID(new Identifiable("SELF-5", new AssigningAuthority(null, "1.3.6.1.4.1.21367.2005.3.7", "ISO")));
        submissionSet.setTitle(new LocalizedString("Physical", "en-us", "UTF-8"));
        submissionSet.setUniqueID("SubmissionSet01_uniqueid");

        Folder folder = new Folder();
        folder.setComments(new LocalizedString("comments go here", "en-us", "UTF-8"));
        folder.setEntryUUID("Folder");
        folder.setPatientID(new Identifiable("ST-1000", new AssigningAuthority(null, "1.3.6.1.4.1.21367.2005.3.7", "ISO")));
        folder.setUniqueID("folder_uniqueid");
        folder.setTitle(new LocalizedString("FOLDER", "en-us", "UTF-8"));
        
        Association assoc1 = new Association();
        assoc1.setAssociationType(AssociationType.HAS_MEMBER);
        assoc1.setSourceUUID("SubmissionSet01");
        assoc1.setTargetUUID("Folder");
        
        Association assoc2 = new Association();
        assoc2.setAssociationType(AssociationType.HAS_MEMBER);
        assoc2.setSourceUUID("Folder");
        assoc2.setTargetUUID("Document01");

        Association assoc3 = new Association();
        assoc3.setAssociationType(AssociationType.HAS_MEMBER);
        assoc3.setSourceUUID("SubmissionSet01");
        assoc3.setTargetUUID("Document01");
        
        RegisterDocumentSet expected = new RegisterDocumentSet();
        expected.setSubmissionSet(submissionSet);
        expected.getDocumentEntries().add(docEntry);
        expected.getFolders().add(folder);
        expected.getAssociations().add(assoc1);
        expected.getAssociations().add(assoc2);
        expected.getAssociations().add(assoc3);
        
        File file = new File(getClass().getClassLoader().getResource("SubmitObjectsRequest_ebrs21.xml").toURI());
        
        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        
        Object unmarshalled = unmarshaller.unmarshal(file);
        SubmitObjectsRequest original = (SubmitObjectsRequest) unmarshalled;
        
        RegisterDocumentSetTransformer transformer = new RegisterDocumentSetTransformer(factory);;
        RegisterDocumentSet result = transformer.fromEbXML(SubmitObjectsRequest21.create(original));
        assertEquals(expected, result);
        SubmitObjectsRequest21 ebXML = (SubmitObjectsRequest21) transformer.toEbXML(result);
        
        SubmitObjectsRequest transformed = ebXML.getInternal();
        
        RegisterDocumentSet resultTransformed = transformer.fromEbXML(SubmitObjectsRequest21.create(transformed));        
        assertEquals(expected.toString(), resultTransformed.toString());
    }
}
