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

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.hl7.HL7Delimiter;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ValueListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.AssigningAuthorityTransformer;

/**
 * Transforms between an {@link Identifiable} and its ebXML 2.1 representation.
 * @author Jens Riemschneider
 */
public class IdentifiableTransformer {
    private final AssigningAuthorityTransformer assigningAuthorityTransformer = new AssigningAuthorityTransformer();
    
    /**
     * Transforms an {@link Identifiable} into an {@link ExternalIdentifierType}
     * for usage with a Patient ID (not Source Patient ID).
     * @param identifiable
     *          the identifiable instance.
     * @return the ebXML 2.1 representation.
     */
    public ExternalIdentifierType toEbXML21Patient(Identifiable identifiable) {
        if (identifiable == null) {
            return null;
        }
        
        ExternalIdentifierType result = Ebrs21.createExternalIdentifiable();
        result.setIdentificationScheme(Vocabulary.DOC_ENTRY_PATIENT_ID_EXTERNAL_ID);
        result.setValue(toHL7CX(identifiable));
        
        LocalizedString localized = new LocalizedString();
        localized.setValue(Vocabulary.LOCALIZED_STRING_PATIENT_ID);
        result.setName(Ebrs21.createInternationalString(localized));
        
        return result;
    }
    
    /**
     * Transforms an {@link Identifiable} into a {@link SlotType1} for usage with
     * a Source Patient ID (not Patient ID).
     * @param identifiable
     *          the identifiable instance.
     * @return the ebXML 2.1 representation.
     */
    public SlotType1 toEbXML21SourcePatient(Identifiable identifiable) {
        if (identifiable == null) {
            return null;
        }
        
        return Ebrs21.createSlot(Vocabulary.SLOT_NAME_SOURCE_PATIENT_ID, toHL7CX(identifiable));
    }

    /**
     * Transforms an external identifier into an {@link Identifiable} for usage
     * with a Patient ID (not Source Patient ID).
     * @param externalIdentifier
     *          the ebXML 2.1 representation.
     * @return the identifiable instance.
     */
    public Identifiable fromEbXML21PatientID(ExternalIdentifierType externalIdentifier) {
        if (externalIdentifier == null) {
            return null;
        }
        
        
        return fromHl7CX(externalIdentifier.getValue());
    }

    /**
     * Transforms an external identifier into an {@link Identifiable} for usage
     * with a Source Patient ID (not Patient ID).
     * @param slot
     *          the ebXML 2.1 representation.
     * @return the identifiable instance.
     */
    public Identifiable fromEbXML21SourcePatientID(SlotType1 slot) {
        if (slot == null) {
            return null;
        }
        
        ValueListType valueList = slot.getValueList();
        String value = null;
        if (valueList != null && valueList.getValue().size() > 0) {
            value = valueList.getValue().get(0);
        }
        return fromHl7CX(value);
    }

    private Identifiable fromHl7CX(String hl7CX) {
        Identifiable identifiable = new Identifiable();
        List<String> cx = HL7.parse(HL7Delimiter.COMPONENT, hl7CX);
        identifiable.setId(HL7.get(cx, 1, true));       
        identifiable.setAssigningAuthority(assigningAuthorityTransformer.fromHL7(HL7.get(cx, 4, false)));
        return identifiable;
    }
    
    private String toHL7CX(Identifiable identifiable) {
        String hl7HD = assigningAuthorityTransformer.toHL7(identifiable.getAssigningAuthority());
        return HL7.render(HL7Delimiter.COMPONENT, 
                HL7.escape(identifiable.getId()),
                null,
                null,
                hl7HD);
    }    
}
