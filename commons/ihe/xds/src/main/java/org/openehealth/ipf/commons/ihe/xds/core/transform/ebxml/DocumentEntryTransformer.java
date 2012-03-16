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

import static org.apache.commons.lang3.Validate.notNull;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Author;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.PatientInfoTransformer;
import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.PersonTransformer;

import java.util.List;

/**
 * Transforms between a {@link DocumentEntry} and its ebXML representation.
 * @author Jens Riemschneider
 */
public class DocumentEntryTransformer extends XDSMetaClassTransformer<EbXMLExtrinsicObject, DocumentEntry> {
    private final EbXMLFactory factory;
    
    private final AuthorTransformer authorTransformer;
    private final CodeTransformer codeTransformer;
    
    private final PersonTransformer personTransformer = new PersonTransformer();
    private final IdentifiableTransformer identifiableTransformer = new IdentifiableTransformer();
    private final PatientInfoTransformer patientInfoTransformer = new PatientInfoTransformer();
    private final UriTransformer uriTransformer = new UriTransformer();   
    
    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects. 
     */
    public DocumentEntryTransformer(EbXMLFactory factory) {
        super(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, 
                DOC_ENTRY_LOCALIZED_STRING_PATIENT_ID, 
                DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID,
                DOC_ENTRY_LOCALIZED_STRING_UNIQUE_ID);
        
        notNull(factory, "factory cannot be null");

        this.factory = factory;
        authorTransformer = new AuthorTransformer(factory);
        codeTransformer = new CodeTransformer(factory);
    }
    
    @Override
    protected EbXMLExtrinsicObject createEbXMLInstance(String id, EbXMLObjectLibrary objectLibrary) {
        return factory.createExtrinsic(id, objectLibrary);
    }
    
    @Override
    protected DocumentEntry createMetaClassInstance() {
        return new DocumentEntry();
    }

    @Override
    protected void addAttributesFromEbXML(DocumentEntry docEntry, EbXMLExtrinsicObject extrinsic) {
        super.addAttributesFromEbXML(docEntry, extrinsic);
        docEntry.setAvailabilityStatus(extrinsic.getStatus());        
        docEntry.setMimeType(extrinsic.getMimeType());
        docEntry.setType(DocumentEntryType.valueOfUuid(extrinsic.getObjectType()));
        docEntry.setHomeCommunityId(extrinsic.getHome());
    }

    @Override
    protected void addAttributes(DocumentEntry metaData, EbXMLExtrinsicObject ebXML, EbXMLObjectLibrary objectLibrary) {
        super.addAttributes(metaData, ebXML, objectLibrary);
        ebXML.setStatus(metaData.getAvailabilityStatus());                
        ebXML.setMimeType(metaData.getMimeType());
        ebXML.setObjectType(DocumentEntryType.toUuid(metaData.getType()));
        ebXML.setHome(metaData.getHomeCommunityId());
    }

    @Override
    protected void addSlotsFromEbXML(DocumentEntry docEntry, EbXMLExtrinsicObject extrinsic) {
        super.addSlotsFromEbXML(docEntry, extrinsic);
        
        docEntry.setCreationTime(extrinsic.getSingleSlotValue(SLOT_NAME_CREATION_TIME));
        docEntry.setHash(extrinsic.getSingleSlotValue(SLOT_NAME_HASH));
        docEntry.setLanguageCode(extrinsic.getSingleSlotValue(SLOT_NAME_LANGUAGE_CODE));
        docEntry.setServiceStartTime(extrinsic.getSingleSlotValue(SLOT_NAME_SERVICE_START_TIME));
        docEntry.setServiceStopTime(extrinsic.getSingleSlotValue(SLOT_NAME_SERVICE_STOP_TIME));
        docEntry.setRepositoryUniqueId(extrinsic.getSingleSlotValue(SLOT_NAME_REPOSITORY_UNIQUE_ID));
        docEntry.setUri(uriTransformer.fromEbXML(extrinsic.getSlotValues(SLOT_NAME_URI)));
        
        String size = extrinsic.getSingleSlotValue(SLOT_NAME_SIZE);
        docEntry.setSize(size != null ? Long.parseLong(size) : null);
        
        String hl7LegalAuthenticator = extrinsic.getSingleSlotValue(SLOT_NAME_LEGAL_AUTHENTICATOR);
        docEntry.setLegalAuthenticator(personTransformer.fromHL7(hl7LegalAuthenticator));
        
        String sourcePatient = extrinsic.getSingleSlotValue(SLOT_NAME_SOURCE_PATIENT_ID);
        docEntry.setSourcePatientId(identifiableTransformer.fromEbXML(sourcePatient));
        
        List<String> slotValues = extrinsic.getSlotValues(SLOT_NAME_SOURCE_PATIENT_INFO);        
        docEntry.setSourcePatientInfo(patientInfoTransformer.fromHL7(slotValues));
    }
    
    @Override
    protected void addSlots(DocumentEntry docEntry, EbXMLExtrinsicObject extrinsic, EbXMLObjectLibrary objectLibrary) {
        super.addSlots(docEntry, extrinsic, objectLibrary);
        
        extrinsic.addSlot(SLOT_NAME_CREATION_TIME, docEntry.getCreationTime());        
        extrinsic.addSlot(SLOT_NAME_HASH, docEntry.getHash());
        extrinsic.addSlot(SLOT_NAME_LANGUAGE_CODE, docEntry.getLanguageCode());
        extrinsic.addSlot(SLOT_NAME_SERVICE_START_TIME, docEntry.getServiceStartTime());
        extrinsic.addSlot(SLOT_NAME_SERVICE_STOP_TIME, docEntry.getServiceStopTime());
        extrinsic.addSlot(SLOT_NAME_REPOSITORY_UNIQUE_ID, docEntry.getRepositoryUniqueId());
        extrinsic.addSlot(SLOT_NAME_URI, uriTransformer.toEbXML(docEntry.getUri()));
        
        Long size = docEntry.getSize();
        extrinsic.addSlot(SLOT_NAME_SIZE, size != null ? size.toString() : null);
        
        String hl7LegalAuthenticator = personTransformer.toHL7(docEntry.getLegalAuthenticator());
        extrinsic.addSlot(SLOT_NAME_LEGAL_AUTHENTICATOR, hl7LegalAuthenticator);
        
        String sourcePatient = identifiableTransformer.toEbXML(docEntry.getSourcePatientId());
        extrinsic.addSlot(SLOT_NAME_SOURCE_PATIENT_ID, sourcePatient);
        
        List<String> slotValues = patientInfoTransformer.toHL7(docEntry.getSourcePatientInfo());
        extrinsic.addSlot(SLOT_NAME_SOURCE_PATIENT_INFO, slotValues.toArray(new String[slotValues.size()]));
    }

    @Override
    protected void addClassificationsFromEbXML(DocumentEntry docEntry, EbXMLExtrinsicObject extrinsic) {
        super.addClassificationsFromEbXML(docEntry, extrinsic);

        for (EbXMLClassification author : extrinsic.getClassifications(DOC_ENTRY_AUTHOR_CLASS_SCHEME)) {
            docEntry.getAuthors().add(authorTransformer.fromEbXML(author));
        }
        
        EbXMLClassification classCode = extrinsic.getSingleClassification(DOC_ENTRY_CLASS_CODE_CLASS_SCHEME);
        docEntry.setClassCode(codeTransformer.fromEbXML(classCode));

        EbXMLClassification formatCode = extrinsic.getSingleClassification(DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME);
        docEntry.setFormatCode(codeTransformer.fromEbXML(formatCode));

        EbXMLClassification healthcareFacility = extrinsic.getSingleClassification(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        docEntry.setHealthcareFacilityTypeCode(codeTransformer.fromEbXML(healthcareFacility));
        
        EbXMLClassification practiceSetting = extrinsic.getSingleClassification(DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME);
        docEntry.setPracticeSettingCode(codeTransformer.fromEbXML(practiceSetting));
        
        EbXMLClassification typeCode = extrinsic.getSingleClassification(DOC_ENTRY_TYPE_CODE_CLASS_SCHEME);
        docEntry.setTypeCode(codeTransformer.fromEbXML(typeCode));
        
        List<Code> confidentialityCodes = docEntry.getConfidentialityCodes();
        for (EbXMLClassification code : extrinsic.getClassifications(DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME)) {
            confidentialityCodes.add(codeTransformer.fromEbXML(code));
        }

        List<Code> eventCodeList = docEntry.getEventCodeList();
        for (EbXMLClassification code : extrinsic.getClassifications(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME)) {
            eventCodeList.add(codeTransformer.fromEbXML(code));
        }
    }

    @Override
    protected void addClassifications(DocumentEntry docEntry, EbXMLExtrinsicObject extrinsic, EbXMLObjectLibrary objectLibrary) {
        super.addClassifications(docEntry, extrinsic, objectLibrary);
        
        for (Author author : docEntry.getAuthors()) {
            EbXMLClassification authorClasification = authorTransformer.toEbXML(author, objectLibrary);
            extrinsic.addClassification(authorClasification, DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        }
        
        EbXMLClassification classCode = codeTransformer.toEbXML(docEntry.getClassCode(), objectLibrary);
        extrinsic.addClassification(classCode, DOC_ENTRY_CLASS_CODE_CLASS_SCHEME);
        
        EbXMLClassification formatCode = codeTransformer.toEbXML(docEntry.getFormatCode(), objectLibrary);
        extrinsic.addClassification(formatCode, DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME);

        EbXMLClassification healthcareFacility = codeTransformer.toEbXML(docEntry.getHealthcareFacilityTypeCode(), objectLibrary);
        extrinsic.addClassification(healthcareFacility, DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        
        EbXMLClassification practiceSetting = codeTransformer.toEbXML(docEntry.getPracticeSettingCode(), objectLibrary);
        extrinsic.addClassification(practiceSetting, DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME);
        
        EbXMLClassification typeCode = codeTransformer.toEbXML(docEntry.getTypeCode(), objectLibrary);
        extrinsic.addClassification(typeCode, DOC_ENTRY_TYPE_CODE_CLASS_SCHEME);
        
        for (Code confCode : docEntry.getConfidentialityCodes()) {
            EbXMLClassification conf = codeTransformer.toEbXML(confCode, objectLibrary);
            extrinsic.addClassification(conf, DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME);
        }
        
        for (Code eventCode : docEntry.getEventCodeList()) {
            EbXMLClassification event = codeTransformer.toEbXML(eventCode, objectLibrary);
            extrinsic.addClassification(event, DOC_ENTRY_EVENT_CODE_CLASS_SCHEME);
        }
    }
}
