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

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssigningAuthority;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.LocalizedStringType;

/**
 * Tests for {@link IdentifiableTransformer}.
 * @author Jens Riemschneider
 */
public class IdentifiableTransformerTest {
    private IdentifiableTransformer transformer;
    private Identifiable identifiable;
    
    @Before
    public void setUp() {
        transformer = new IdentifiableTransformer();

        AssigningAuthority assigningAuthority = new AssigningAuthority();
        assigningAuthority.setNamespaceId("namespace");
        assigningAuthority.setUniversalId("uni");
        assigningAuthority.setUniversalIdType("uniType");
        
        identifiable = new Identifiable();
        identifiable.setId("21&3");
        identifiable.setAssigningAuthority(assigningAuthority);
    }
    
    @Test
    public void testToEbXML30Patient() {
        ExternalIdentifierType result = transformer.toEbXML30Patient(identifiable);
        assertNotNull(result);
        
        assertEquals("21\\T\\3^^^namespace&uni&uniType", result.getValue());
        List<LocalizedStringType> localizedStrings = result.getName().getLocalizedString();
        assertEquals(1, localizedStrings.size());
        assertEquals(Vocabulary.LOCALIZED_STRING_PATIENT_ID, localizedStrings.get(0).getValue());
        assertEquals(Vocabulary.DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, result.getIdentificationScheme());
    }
    
    @Test
    public void testToEbXML30PatientNull() {
        assertNull(transformer.toEbXML30Patient(null));
    }

    @Test
    public void testToEbXML30PatientEmpty() {
        ExternalIdentifierType result = transformer.toEbXML30Patient(new Identifiable());        
        assertNotNull(result);
        assertNull(result.getValue());
        List<LocalizedStringType> localizedStrings = result.getName().getLocalizedString();
        assertEquals(1, localizedStrings.size());
        assertEquals(Vocabulary.LOCALIZED_STRING_PATIENT_ID, localizedStrings.get(0).getValue());
        assertEquals(Vocabulary.DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, result.getIdentificationScheme());
    }

    
    
    @Test
    public void testFromEbXML30Patient() {
        ExternalIdentifierType ebXML = transformer.toEbXML30Patient(identifiable);
        Identifiable result = transformer.fromEbXML30PatientID(ebXML);       
        assertNotNull(result);
        
        assertEquals("21&3", result.getId());
        
        AssigningAuthority assigningAuthority = result.getAssigningAuthority();
        assertEquals("namespace", assigningAuthority.getNamespaceId());
        assertEquals("uni", assigningAuthority.getUniversalId());
        assertEquals("uniType", assigningAuthority.getUniversalIdType());
    }
    
    @Test
    public void testFromEbXML30PatientNull() {
        assertNull(transformer.fromEbXML30PatientID(null));
    }
    
    @Test
    public void testFromEbXML30PatientEmpty() {
        Identifiable result = transformer.fromEbXML30PatientID(new ExternalIdentifierType());
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getAssigningAuthority());
    }

    

    @Test
    public void testToEbXML30SourcePatient() {
        String result = transformer.toEbXML30SourcePatient(identifiable);
        assertNotNull(result);
        
        assertEquals("21\\T\\3^^^namespace&uni&uniType", result);
    }

    @Test
    public void testToEbXML30SourcePatientNull() {
        assertNull(transformer.toEbXML30SourcePatient(null));
    }

    @Test
    public void testToEbXML30SourcePatientEmpty() {
        String result = transformer.toEbXML30SourcePatient(new Identifiable());
        assertNotNull(result);
        assertEquals("", result);
    }



    @Test
    public void testFromEbXML30SourcePatient() {
        String ebXML = transformer.toEbXML30SourcePatient(identifiable);
        Identifiable result = transformer.fromEbXML30SourcePatientID(ebXML);       
        assertNotNull(result);
        
        assertEquals("21&3", result.getId());
        
        AssigningAuthority assigningAuthority = result.getAssigningAuthority();
        assertEquals("namespace", assigningAuthority.getNamespaceId());
        assertEquals("uni", assigningAuthority.getUniversalId());
        assertEquals("uniType", assigningAuthority.getUniversalIdType());
    }
    
    @Test
    public void testFromEbXML30SourcePatientNull() {
        assertNull(transformer.fromEbXML30SourcePatientID(null));
    }
    
    @Test
    public void testFromEbXML30SourcePatientEmpty() {
        Identifiable result = transformer.fromEbXML30SourcePatientID("");
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getAssigningAuthority());
    }
}
