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

import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;

import java.util.ArrayList;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.EntryUUID;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ClassificationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.SlotType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.PatientInfoTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.PersonTransformer;

/**
 * Transforms between a {@link DocumentEntry} and its ebXML 2.1 representation.
 * @author Jens Riemschneider
 */
public class DocumentEntryTransformer {
    private final AuthorTransformer authorTransformer = new AuthorTransformer();
    private final CodeTransformer codeTransformer = new CodeTransformer();
    private final PersonTransformer personTransformer = new PersonTransformer();
    private final IdentifiableTransformer identifiableTransformer = new IdentifiableTransformer();
    private final PatientInfoTransformer patientInfoTransformer = new PatientInfoTransformer();
    
    /**
     * Transforms the given document entry into its ebXML 2.1 representation.
     * @param documentEntry
     *          the document entry to transform.
     * @return the ebXML 2.1 representation.
     */
    public ExtrinsicObjectType toEbXML21(DocumentEntry documentEntry) {
        if (documentEntry == null) {
            return null;
        }
        
        ExtrinsicObjectType extrinsic = Ebrs21.createExtrinsicObjectType();
        
        addAttributes(documentEntry, extrinsic);        
        addClassifications(documentEntry, extrinsic);
        addSlots(documentEntry, extrinsic);
        addExternalIdentifiers(documentEntry, extrinsic);
        
        return extrinsic;
    }
    
    /**
     * Transforms the given extrinsic object into a {@link DocumentEntry}.
     * @param extrinsic
     *          the ebXML 2.1 representation of a document entry.
     * @return the document entry instance.
     */
    public DocumentEntry fromEbXML21(ExtrinsicObjectType extrinsic) {
        if (extrinsic == null) {
            return null;
        }
        
        DocumentEntry docEntry = new DocumentEntry();
        
        return docEntry;
    }

    private void addExternalIdentifiers(DocumentEntry documentEntry, ExtrinsicObjectType extrinsic) {
        String patientID = identifiableTransformer.toEbXML21(documentEntry.getPatientID());
        addExternalIdentifier(extrinsic, 
                Ebrs21.createExternalIdentifiable(patientID),
                DOC_ENTRY_PATIENT_ID_EXTERNAL_ID,
                LOCALIZED_STRING_PATIENT_ID);
        
        String uniqueID = documentEntry.getUniqueID() != null ? documentEntry.getUniqueID().getValue() : null;
        addExternalIdentifier(extrinsic, 
                Ebrs21.createExternalIdentifiable(uniqueID),
                DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID,
                LOCALIZED_STRING_UNIQUE_ID);
    }

    private void addExternalIdentifier(ExtrinsicObjectType extrinsic, ExternalIdentifierType identifier, String scheme, String name) {
        if (identifier != null) {
            extrinsic.getExternalIdentifier().add(identifier);
            
            identifier.setIdentificationScheme(scheme);
            LocalizedString localized = new LocalizedString();
            localized.setValue(name);
            identifier.setName(Ebrs21.createInternationalString(localized));
        }
    }

    private void addAttributes(DocumentEntry documentEntry, ExtrinsicObjectType extrinsic) {
        AvailabilityStatus status = documentEntry.getAvailabilityStatus();
        extrinsic.setStatus(status != null ? status.getRepresentation() : null);        
        
        extrinsic.setDescription(
                Ebrs21.createInternationalString(documentEntry.getComments()));

        extrinsic.setName(
                Ebrs21.createInternationalString(documentEntry.getTitle()));
        
        EntryUUID entryUUID = documentEntry.getEntryUUID();
        extrinsic.setId(entryUUID != null ? entryUUID.getValue() : null);
        
        extrinsic.setMimeType(documentEntry.getMimeType());
    }

    private void addSlots(DocumentEntry documentEntry, ExtrinsicObjectType extrinsic) {
        List<SlotType1> slots = extrinsic.getSlot();
        
        Ebrs21.addSlot(slots, SLOT_NAME_CREATION_TIME, documentEntry.getCreationTime());        
        Ebrs21.addSlot(slots, SLOT_NAME_HASH, documentEntry.getHash());
        Ebrs21.addSlot(slots, SLOT_NAME_LANGUAGE_CODE, documentEntry.getLanguageCode());
        Ebrs21.addSlot(slots, SLOT_NAME_SERVICE_START_TIME, documentEntry.getServiceStartTime());
        Ebrs21.addSlot(slots, SLOT_NAME_SERVICE_STOP_TIME, documentEntry.getServiceStopTime());        
        Ebrs21.addSlot(slots, SLOT_NAME_URI, convertUri(documentEntry.getUri()));
        
        Long size = documentEntry.getSize();
        Ebrs21.addSlot(slots, SLOT_NAME_SIZE, size != null ? size.toString() : null);
        
        String hl7LegalAuthenticator = personTransformer.toHL7(documentEntry.getLegalAuthenticator());
        Ebrs21.addSlot(slots, SLOT_NAME_LEGAL_AUTHENTICATOR, hl7LegalAuthenticator);
        
        String sourcePatient = identifiableTransformer.toEbXML21(documentEntry.getSourcePatientID());
        Ebrs21.addSlot(slots, SLOT_NAME_SOURCE_PATIENT_ID, sourcePatient);
        
        List<String> slotValues = patientInfoTransformer.toHL7(documentEntry.getSourcePatientInfo());
        Ebrs21.addSlot(slots, SLOT_NAME_SOURCE_PATIENT_INFO, slotValues.toArray(new String[0]));
    }

    private String[] convertUri(String uri) {
        if (uri == null) {
            return new String[0];
        }
        
        List<String> uriParts = new ArrayList<String>();
        
        int slotIdx = 1;
        int start = 0;
        while (start < uri.length()) {
            String prefix = slotIdx + "|";
            int validLength = 128 - prefix.length();
            if (uri.length() < start + validLength) {
                validLength = uri.length() - start;
            }
            
            String uriPart = uri.substring(start, start + validLength);
            String slotValue = prefix + uriPart;
            uriParts.add(slotValue);
            
            start += validLength;
            ++slotIdx;
        }
        
        return uriParts.toArray(new String[0]);
    }

    private void addClassifications(DocumentEntry documentEntry, ExtrinsicObjectType extrinsic) {
        ClassificationType author = authorTransformer.toEbXML21(documentEntry.getAuthor());
        addClassification(extrinsic, author, DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        
        ClassificationType classCode = codeTransformer.toEbXML21(documentEntry.getClassCode());
        addClassification(extrinsic, classCode, DOC_ENTRY_CLASS_CODE_CLASS_SCHEME);
        
        ClassificationType formatCode = codeTransformer.toEbXML21(documentEntry.getFormatCode());
        addClassification(extrinsic, formatCode, DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME);

        ClassificationType healthcareFacility = codeTransformer.toEbXML21(documentEntry.getHealthcareFacilityTypeCode());
        addClassification(extrinsic, healthcareFacility, DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        
        ClassificationType practiceSetting = codeTransformer.toEbXML21(documentEntry.getPracticeSettingCode());
        addClassification(extrinsic, practiceSetting, DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME);
        
        ClassificationType typeCode = codeTransformer.toEbXML21(documentEntry.getTypeCode());
        addClassification(extrinsic, typeCode, DOC_ENTRY_TYPE_CODE_CLASS_SCHEME);
        
        for (Code confCode : documentEntry.getConfidentialityCodes()) {
            ClassificationType conf = codeTransformer.toEbXML21(confCode);
            addClassification(extrinsic, conf, DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME);
        }
        
        for (Code eventCode : documentEntry.getEventCodeList()) {
            ClassificationType event = codeTransformer.toEbXML21(eventCode);
            addClassification(extrinsic, event, DOC_ENTRY_EVENT_CODE_CLASS_SCHEME);
        }
    }

    private void addClassification(ExtrinsicObjectType extrinsic, ClassificationType classification, String classScheme) {
        if (classification != null) {
            extrinsic.getClassification().add(classification);

            classification.setClassifiedObject(extrinsic);
            classification.setClassificationScheme(classScheme);
        }
    }
}
