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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSlot;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLExtrinsicObject21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLSubmitObjectsRequest21;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.RegistryEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RegisterDocumentSetTransformer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for marshaling objects created with our ebxml 2.1 classes.
 * @author Jens Riemschneider
 */
public class Ebrs21MarshalingTest {
    private SubmitObjectsRequest request;
    private EbXMLExtrinsicObject docEntry;
    private EbXMLFactory21 factory;
    private EbXMLObjectLibrary objectLibrary;

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
        docEntry.setObjectType(DocumentEntryType.STABLE.getUuid());
        objList.add(docEntry.getInternal());
    }
    
    @Test
    public void testCreateClassification() throws Exception {        
        EbXMLClassification classification = factory.createClassification(objectLibrary);
        classification.setClassifiedObject(docEntry.getId());
        docEntry.addClassification(classification, Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        
        SubmitObjectsRequest received = send();
        
        EbXMLExtrinsicObject docEntryResult = getDocumentEntry(received);
        assertEquals(1, docEntryResult.getClassifications().size());
        EbXMLClassification classificationResult = docEntryResult.getClassifications().get(0);
        assertNotNull(classificationResult);
        
        assertEquals(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME, classificationResult.getClassificationScheme());
        assertNull(classificationResult.getNodeRepresentation());
        assertSame(docEntryResult.getId(), classificationResult.getClassifiedObject());
    }
    
    private EbXMLExtrinsicObject getDocumentEntry(SubmitObjectsRequest received) {
        EbXMLObjectLibrary objectLibraryOfRequest = new EbXMLObjectLibrary();
        
        for (Object obj : received.getLeafRegistryObjectList().getObjectRefOrAssociationOrAuditableEvent()) {
            if (obj instanceof ObjectRefType) {
                objectLibraryOfRequest.put(((ObjectRefType) obj).getId(), obj);
            }
            if (obj instanceof RegistryEntryType) {
                objectLibraryOfRequest.put(((RegistryEntryType) obj).getId(), obj);
            }
            
            if (obj instanceof ExtrinsicObjectType) {                
                return new EbXMLExtrinsicObject21((ExtrinsicObjectType)obj, objectLibraryOfRequest);
            }
        }
        fail("Document entry not found");
        return null;
    }

    @Test
    public void testAddSlot() throws Exception {
        EbXMLClassification classification = factory.createClassification(objectLibrary);
        docEntry.addClassification(classification, Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        
        classification.addSlot("something", "a", "b", "c");

        SubmitObjectsRequest received = send();        
        EbXMLExtrinsicObject docEntryResult = getDocumentEntry(received);
        List<EbXMLSlot> slots = docEntryResult.getClassifications().get(0).getSlots();        
        assertEquals(1, slots.size());        
        
        EbXMLSlot slot = slots.get(0);
        assertEquals("something", slot.getName());
        
        List<String> values = slot.getValueList();
        assertEquals(3, values.size());
        assertTrue(values.contains("a"));
        assertTrue(values.contains("b"));
        assertTrue(values.contains("c"));
    }

    private SubmitObjectsRequest send() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs");
        Marshaller marshaller = context.createMarshaller();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
        marshaller.marshal(request, outputStream);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return (SubmitObjectsRequest) unmarshaller.unmarshal(inputStream);
    }
    
    @Test
    public void testFromRealEbXML() throws Exception {
        Author author1 = new Author();
        author1.setAuthorPerson(new Person(null, new XcnName("Smitty", "Gerald", null, null, null, null)));
        author1.getAuthorInstitution().add(new Organization("Cleveland Clinic"));
        author1.getAuthorInstitution().add(new Organization("Parma Community"));
        author1.getAuthorRole().add("Attending");
        author1.getAuthorSpecialty().add("Orthopedic");

        Author author2 = new Author();
        author2.setAuthorPerson(new Person(null, new XcnName("Dopplemeyer", "Sherry", null, null, null, null)));
        author2.getAuthorInstitution().add(new Organization("Cleveland Clinic"));
        author2.getAuthorInstitution().add(new Organization("Berea Community"));
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
        sourcePatientInfo.setName(new XcnName("Doe", "John", null, null, null, null));
        
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
        docEntry.setSourcePatientId(new Identifiable("89765a87b", new AssigningAuthority("fj34r", null, null)));
        docEntry.setSourcePatientInfo(sourcePatientInfo);
        docEntry.setTypeCode(new Code("34108-1", new LocalizedString("Outpatient Evaluation And Management", "en-us", "UTF-8"), "LOINC"));
        docEntry.setEntryUuid("Document01");
        docEntry.setPatientId(new Identifiable("SELF-5", new AssigningAuthority(null, "1.3.6.1.4.1.21367.2005.3.7", "ISO")));
        docEntry.setTitle(new LocalizedString("Physical", "en-us", "UTF-8"));
        docEntry.setUniqueId("Document01_uniqueid");
        docEntry.setUri("http://129.6.58.92:9080/Repository/129.6.58.92.3492.txt");
        docEntry.setRepositoryUniqueId("42");

        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.getAuthors().add(author2);
        submissionSet.setContentTypeCode(new Code("History and Physical", new LocalizedString("History and Physical", "en-us", "UTF-8"), "Connect-a-thon contentTypeCodes"));
        submissionSet.setSourceId("129.6.58.92.1.1");
        submissionSet.setSubmissionTime("20041225235050");
        submissionSet.setComments(new LocalizedString("Annual physical", "en-us", "UTF-8"));
        submissionSet.setEntryUuid("SubmissionSet01");
        submissionSet.setPatientId(new Identifiable("SELF-5", new AssigningAuthority(null, "1.3.6.1.4.1.21367.2005.3.7", "ISO")));
        submissionSet.setTitle(new LocalizedString("Physical", "en-us", "UTF-8"));
        submissionSet.setUniqueId("SubmissionSet01_uniqueid");

        Folder folder = new Folder();
        folder.setComments(new LocalizedString("comments go here", "en-us", "UTF-8"));
        folder.setEntryUuid("Folder");
        folder.setPatientId(new Identifiable("ST-1000", new AssigningAuthority(null, "1.3.6.1.4.1.21367.2005.3.7", "ISO")));
        folder.setUniqueId("folder_uniqueid");
        folder.setTitle(new LocalizedString("FOLDER", "en-us", "UTF-8"));
        
        Association assoc1 = new Association();
        assoc1.setAssociationType(AssociationType.HAS_MEMBER);
        assoc1.setSourceUuid("SubmissionSet01");
        assoc1.setTargetUuid("Folder");
        
        Association assoc2 = new Association();
        assoc2.setAssociationType(AssociationType.HAS_MEMBER);
        assoc2.setSourceUuid("Folder");
        assoc2.setTargetUuid("Document01");

        Association assoc3 = new Association();
        assoc3.setAssociationType(AssociationType.HAS_MEMBER);
        assoc3.setSourceUuid("SubmissionSet01");
        assoc3.setTargetUuid("Document01");
        assoc3.setLabel(AssociationLabel.ORIGINAL);
        
        RegisterDocumentSet expected = new RegisterDocumentSet();
        expected.setSubmissionSet(submissionSet);
        expected.getDocumentEntries().add(docEntry);
        expected.getFolders().add(folder);
        expected.getAssociations().add(assoc1);
        expected.getAssociations().add(assoc2);
        expected.getAssociations().add(assoc3);
        
        File file = new File(getClass().getClassLoader().getResource("SubmitObjectsRequest_ebrs21.xml").toURI());
        
        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        
        Object unmarshalled = unmarshaller.unmarshal(file);
        SubmitObjectsRequest original = (SubmitObjectsRequest) unmarshalled;
        
        RegisterDocumentSetTransformer transformer = new RegisterDocumentSetTransformer(factory);
        RegisterDocumentSet result = transformer.fromEbXML(new EbXMLSubmitObjectsRequest21(original));
        assertEquals(expected.toString(), result.toString());
        EbXMLSubmitObjectsRequest21 ebXML = (EbXMLSubmitObjectsRequest21) transformer.toEbXML(result);
        
        SubmitObjectsRequest transformed = ebXML.getInternal();
        
        RegisterDocumentSet resultTransformed = transformer.fromEbXML(new EbXMLSubmitObjectsRequest21(transformed));        
        assertEquals(expected.toString(), resultTransformed.toString());
    }
}
