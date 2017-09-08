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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLAdhocQueryRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLExtrinsicObject30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ExtrinsicObjectType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.RegistryObjectListType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for marshaling objects created with our ebxml 2.1 classes.
 * @author Jens Riemschneider
 */
public class Ebrs30MarshalingTest {
    private SubmitObjectsRequest request;
    private EbXMLExtrinsicObject docEntry;
    private EbXMLFactory30 factory;
    private EbXMLObjectLibrary objectLibrary;

    private static final QName EXTRINSIC_OBJECT_QNAME = 
        new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject", "rim"); 

    @Before
    public void setUp() {
        factory = new EbXMLFactory30();
        objectLibrary = factory.createObjectLibrary();
        
        request = new SubmitObjectsRequest();
        RegistryObjectListType objListElement = new RegistryObjectListType();
        request.setRegistryObjectList(objListElement);
        List<JAXBElement<? extends IdentifiableType>> objList = objListElement.getIdentifiable();

        docEntry = factory.createExtrinsic("Document01", objectLibrary);
        docEntry.setObjectType(DocumentEntryType.STABLE.getUuid());
        objList.add(getJaxbElement(EXTRINSIC_OBJECT_QNAME, ((EbXMLExtrinsicObject30)docEntry).getInternal()));
    }
    
    @SuppressWarnings("unchecked")
    private static JAXBElement<IdentifiableType> getJaxbElement(QName qname, IdentifiableType object) {
        return new JAXBElement<>(qname, (Class)object.getClass(), object);
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
        assertEquals(docEntryResult.getId(), classificationResult.getClassifiedObject());
    }
    
    private EbXMLExtrinsicObject getDocumentEntry(SubmitObjectsRequest received) {
        for(JAXBElement<? extends IdentifiableType> obj : received.getRegistryObjectList().getIdentifiable()) {
            if(obj.getDeclaredType() == ExtrinsicObjectType.class) {
                return new EbXMLExtrinsicObject30((ExtrinsicObjectType)obj.getValue(), objectLibrary);
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

    @Test
    public void testFromRealEbXML() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("SubmitObjectsRequest_ebrs30.xml").toURI());

        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs");
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Object unmarshalled = unmarshaller.unmarshal(file);
        SubmitObjectsRequest original = (SubmitObjectsRequest) unmarshalled;

        RegisterDocumentSetTransformer transformer = new RegisterDocumentSetTransformer(factory);
        RegisterDocumentSet result = transformer.fromEbXML(new EbXMLSubmitObjectsRequest30(original));

        DocumentEntry documentEntry = result.getDocumentEntries().get(0);
        assertEquals("Document01", documentEntry.getEntryUuid());
        assertEquals(DocumentEntryType.STABLE, documentEntry.getType());
        assertEquals("Gerald Smitty", documentEntry.getAuthor().getAuthorPerson().getId().getId());

        SubmissionSet submissionSet = result.getSubmissionSet();
        assertEquals("SubmissionSet01", submissionSet.getEntryUuid());
        assertEquals(1, submissionSet.getAuthors().size());
        assertEquals("Sherry Dopplemeyer", submissionSet.getAuthors().get(0).getAuthorPerson().getId().getId());

        assertEquals(result.getAssociations().get(0).getAssociationType(), AssociationType.HAS_MEMBER);
        assertEquals(result.getAssociations().get(1).getAssociationType(), AssociationType.IS_SNAPSHOT_OF);
    }

    @Test
    public void testPatientIdSlotRegexp() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("iti18request.xml").toURI());

        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs");
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Object unmarshalled = unmarshaller.unmarshal(file);
        AdhocQueryRequest original = (AdhocQueryRequest) unmarshalled;

        QuerySlotHelper slotHelper = new QuerySlotHelper(new EbXMLAdhocQueryRequest30(original));
        List<String> patientIdList = slotHelper.toStringList(QueryParameter.DOC_ENTRY_PATIENT_ID);
        assertEquals(1, patientIdList.size());
    }

    @Test
    public void testPatientIdMPQSlotRegexp() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("iti51request.xml").toURI());

        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs");
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Object unmarshalled = unmarshaller.unmarshal(file);
        AdhocQueryRequest original = (AdhocQueryRequest) unmarshalled;

        QuerySlotHelper slotHelper = new QuerySlotHelper(new EbXMLAdhocQueryRequest30(original));
        List<String> patientIdList = slotHelper.toStringList(QueryParameter.DOC_ENTRY_PATIENT_ID);
        assertEquals(2, patientIdList.size());
    }

    private SubmitObjectsRequest send() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs");
        Marshaller marshaller = context.createMarshaller();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
        marshaller.marshal(request, outputStream);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return (SubmitObjectsRequest) unmarshaller.unmarshal(inputStream);
    }
}
