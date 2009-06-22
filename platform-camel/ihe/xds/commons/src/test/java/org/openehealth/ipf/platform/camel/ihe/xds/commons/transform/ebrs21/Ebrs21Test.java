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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.EntryUUID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LocalizedStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectRefType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * Tests for {@link Ebrs21}.
 * @author Jens Riemschneider
 */
public class Ebrs21Test {
    private SubmitObjectsRequest request;
    private ExtrinsicObjectType docEntry;

    @Before
    public void setUp() {
        request = new SubmitObjectsRequest();
        LeafRegistryObjectListType objListElement = new LeafRegistryObjectListType();
        request.setLeafRegistryObjectList(objListElement);
        List<Object> objList = objListElement.getObjectRefOrAssociationOrAuditableEvent();
        objList.addAll(Ebrs21.getObjLib());

        EntryUUID docEntryId = new EntryUUID("Document01");
        docEntry = new ExtrinsicObjectType();
        docEntry.setId(docEntryId.getValue());
        docEntry.setObjectType(Vocabulary.DOC_ENTRY_CLASS_NODE);
        objList.add(docEntry);
    }
    
    @Test
    public void testCreateClassification() throws Exception {        
        ClassificationType classification = 
            Ebrs21.createClassification(Ebrs21.getObjFromLib(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME));
        docEntry.getClassification().add(classification);
        classification.setClassifiedObject(docEntry);
        
        SubmitObjectsRequest received = send();
        
        ObjectRefType resultScheme = getObjectRef(received, Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        
        ExtrinsicObjectType docEntryResult = getDocumentEntry(received);
        assertEquals(1, docEntryResult.getClassification().size());
        ClassificationType classificationResult = docEntryResult.getClassification().get(0);
        assertNotNull(classificationResult);
        
        assertSame(resultScheme, classificationResult.getClassificationScheme());
        assertEquals("", classificationResult.getNodeRepresentation());
        assertSame(docEntryResult, classificationResult.getClassifiedObject());
    }
    
    private ObjectRefType getObjectRef(SubmitObjectsRequest received, String id) {
        for (Object obj : received.getLeafRegistryObjectList().getObjectRefOrAssociationOrAuditableEvent()) {
            if (obj instanceof ObjectRefType) {
                ObjectRefType objRef = (ObjectRefType) obj;
                if (objRef.getId().equals(id)) {
                    return objRef;
                }
            }
        }
        fail("ObjectRef not found: " + id);
        return null;
    }
    
    private ExtrinsicObjectType getDocumentEntry(SubmitObjectsRequest received) {
        for (Object obj : received.getLeafRegistryObjectList().getObjectRefOrAssociationOrAuditableEvent()) {
            if (obj instanceof ExtrinsicObjectType) {
                return (ExtrinsicObjectType) obj;
            }
        }
        fail("Document entry not found");
        return null;
    }

    @Test
    public void testAddSlot() throws Exception {
        ClassificationType classification = 
            Ebrs21.createClassification(Ebrs21.getObjFromLib(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME));
        docEntry.getClassification().add(classification);
        classification.setClassifiedObject(docEntry);

        Ebrs21.addSlot(classification, "something", "a", "b", "c");

        SubmitObjectsRequest received = send();        
        ExtrinsicObjectType docEntryResult = getDocumentEntry(received);
        List<SlotType1> slots = docEntryResult.getClassification().get(0).getSlot();        
        assertEquals(1, slots.size());        
        
        SlotType1 slot = slots.get(0);
        assertEquals("something", slot.getName());
        
        List<String> values = slot.getValueList().getValue();
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
    public void testCreateInternationalString() {
        LocalizedString localized = new LocalizedString();
        localized.setCharset("charset");
        localized.setLang("lang");
        localized.setValue("value");
        InternationalStringType international = Ebrs21.createInternationalString(localized);
        assertNotNull(international);
        
        List<LocalizedStringType> localizedStrings = international.getLocalizedString();
        assertEquals(1, localizedStrings.size());
        assertEquals("charset", localizedStrings.get(0).getCharset());
        assertEquals("lang", localizedStrings.get(0).getLang());
        assertEquals("value", localizedStrings.get(0).getValue());
    }
    
    @Test
    public void testGetLocalizedString() {
        LocalizedStringType localized1 = new LocalizedStringType();
        localized1.setCharset("charset1");
        localized1.setLang("lang1");
        localized1.setValue("value1");
        
        LocalizedStringType localized2 = new LocalizedStringType();
        localized2.setCharset("charset2");
        localized2.setLang("lang2");
        localized2.setValue("value2");

        InternationalStringType international = new InternationalStringType();
        international.getLocalizedString().add(localized1);
        international.getLocalizedString().add(localized2);
        
        LocalizedString result = Ebrs21.getLocalizedString(international, 1);
        assertEquals("charset2", result.getCharset());
        assertEquals("lang2", result.getLang());
        assertEquals("value2", result.getValue());
    }
}
