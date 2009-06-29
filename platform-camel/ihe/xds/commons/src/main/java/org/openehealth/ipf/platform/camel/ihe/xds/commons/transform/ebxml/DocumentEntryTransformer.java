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

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary.*;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Author;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.IdentifiableTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.UriTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.PatientInfoTransformer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.hl7.PersonTransformer;

/**
 * Transforms between a {@link DocumentEntry} and its ebXML representation.
 * @author Jens Riemschneider
 */
public class DocumentEntryTransformer extends XDSMetaClassTransformer<ExtrinsicObject, DocumentEntry> {
    private final EbXMLFactory factory;
    
    private final AuthorTransformer authorTransformer;
    private final CodeTransformer codeTransformer;
    
    private final PersonTransformer personTransformer = new PersonTransformer();
    private final IdentifiableTransformer identifiableTransformer = new IdentifiableTransformer();
    private final PatientInfoTransformer patientInfoTransformer = new PatientInfoTransformer();
    private final UriTransformer uriTransformer = new UriTransformer();   
    
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
    protected ExtrinsicObject createEbXMLInstance(String id, ObjectLibrary objectLibrary) {
        return factory.createExtrinsic(id, objectLibrary);
    }
    
    @Override
    protected DocumentEntry createMetaClassInstance() {
        return new DocumentEntry();
    }

    @Override
    protected void addAttributesFromEbXML(DocumentEntry docEntry, ExtrinsicObject extrinsic) {
        super.addAttributesFromEbXML(docEntry, extrinsic);
        docEntry.setMimeType(extrinsic.getMimeType());
    }

    @Override
    protected void addAttributes(DocumentEntry docEntry, ExtrinsicObject extrinsic, ObjectLibrary objectLibrary) {
        super.addAttributes(docEntry, extrinsic, objectLibrary);
        extrinsic.setMimeType(docEntry.getMimeType());
        extrinsic.setObjectType(DOC_ENTRY_CLASS_NODE);
    }

    @Override
    protected void addSlotsFromEbXML(DocumentEntry docEntry, ExtrinsicObject extrinsic) {
        super.addSlotsFromEbXML(docEntry, extrinsic);
        
        docEntry.setCreationTime(extrinsic.getSingleSlotValue(SLOT_NAME_CREATION_TIME));
        docEntry.setHash(extrinsic.getSingleSlotValue(SLOT_NAME_HASH));
        docEntry.setLanguageCode(extrinsic.getSingleSlotValue(SLOT_NAME_LANGUAGE_CODE));
        docEntry.setServiceStartTime(extrinsic.getSingleSlotValue(SLOT_NAME_SERVICE_START_TIME));
        docEntry.setServiceStopTime(extrinsic.getSingleSlotValue(SLOT_NAME_SERVICE_STOP_TIME));
        docEntry.setUri(uriTransformer.fromEbXML(extrinsic.getSlotValues(SLOT_NAME_URI)));
        
        String size = extrinsic.getSingleSlotValue(SLOT_NAME_SIZE);
        docEntry.setSize(size != null ? Long.parseLong(size) : null);
        
        String hl7LegalAuthenticator = extrinsic.getSingleSlotValue(SLOT_NAME_LEGAL_AUTHENTICATOR);
        docEntry.setLegalAuthenticator(personTransformer.fromHL7(hl7LegalAuthenticator));
        
        String sourcePatient = extrinsic.getSingleSlotValue(SLOT_NAME_SOURCE_PATIENT_ID);
        docEntry.setSourcePatientID(identifiableTransformer.fromEbXML(sourcePatient));
        
        List<String> slotValues = extrinsic.getSlotValues(SLOT_NAME_SOURCE_PATIENT_INFO);        
        docEntry.setSourcePatientInfo(patientInfoTransformer.fromHL7(slotValues));
    }
    
    @Override
    protected void addSlots(DocumentEntry docEntry, ExtrinsicObject extrinsic, ObjectLibrary objectLibrary) {
        super.addSlots(docEntry, extrinsic, objectLibrary);
        
        extrinsic.addSlot(SLOT_NAME_CREATION_TIME, docEntry.getCreationTime());        
        extrinsic.addSlot(SLOT_NAME_HASH, docEntry.getHash());
        extrinsic.addSlot(SLOT_NAME_LANGUAGE_CODE, docEntry.getLanguageCode());
        extrinsic.addSlot(SLOT_NAME_SERVICE_START_TIME, docEntry.getServiceStartTime());
        extrinsic.addSlot(SLOT_NAME_SERVICE_STOP_TIME, docEntry.getServiceStopTime());        
        extrinsic.addSlot(SLOT_NAME_URI, uriTransformer.toEbXML(docEntry.getUri()));
        
        Long size = docEntry.getSize();
        extrinsic.addSlot(SLOT_NAME_SIZE, size != null ? size.toString() : null);
        
        String hl7LegalAuthenticator = personTransformer.toHL7(docEntry.getLegalAuthenticator());
        extrinsic.addSlot(SLOT_NAME_LEGAL_AUTHENTICATOR, hl7LegalAuthenticator);
        
        String sourcePatient = identifiableTransformer.toEbXML(docEntry.getSourcePatientID());
        extrinsic.addSlot(SLOT_NAME_SOURCE_PATIENT_ID, sourcePatient);
        
        List<String> slotValues = patientInfoTransformer.toHL7(docEntry.getSourcePatientInfo());
        extrinsic.addSlot(SLOT_NAME_SOURCE_PATIENT_INFO, slotValues.toArray(new String[0]));
    }

    @Override
    protected void addClassificationsFromEbXML(DocumentEntry docEntry, ExtrinsicObject extrinsic) {
        super.addClassificationsFromEbXML(docEntry, extrinsic);

        for (Classification author : extrinsic.getClassifications(DOC_ENTRY_AUTHOR_CLASS_SCHEME)) {
            docEntry.getAuthors().add(authorTransformer.fromEbXML(author));
        }
        
        Classification classCode = extrinsic.getSingleClassification(DOC_ENTRY_CLASS_CODE_CLASS_SCHEME);
        docEntry.setClassCode(codeTransformer.fromEbXML(classCode));

        Classification formatCode = extrinsic.getSingleClassification(DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME);
        docEntry.setFormatCode(codeTransformer.fromEbXML(formatCode));

        Classification healthcareFacility = extrinsic.getSingleClassification(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        docEntry.setHealthcareFacilityTypeCode(codeTransformer.fromEbXML(healthcareFacility));
        
        Classification practiceSetting = extrinsic.getSingleClassification(DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME);
        docEntry.setPracticeSettingCode(codeTransformer.fromEbXML(practiceSetting));
        
        Classification typeCode = extrinsic.getSingleClassification(DOC_ENTRY_TYPE_CODE_CLASS_SCHEME);
        docEntry.setTypeCode(codeTransformer.fromEbXML(typeCode));
        
        List<Code> confidentialityCodes = docEntry.getConfidentialityCodes();
        for (Classification code : extrinsic.getClassifications(DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME)) {
            confidentialityCodes.add(codeTransformer.fromEbXML(code));
        }

        List<Code> eventCodeList = docEntry.getEventCodeList();
        for (Classification code : extrinsic.getClassifications(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME)) {
            eventCodeList.add(codeTransformer.fromEbXML(code));
        }
    }

    @Override
    protected void addClassifications(DocumentEntry docEntry, ExtrinsicObject extrinsic, ObjectLibrary objectLibrary) {
        super.addClassifications(docEntry, extrinsic, objectLibrary);
        
        for (Author author : docEntry.getAuthors()) {
            Classification authorClasification = authorTransformer.toEbXML(author, objectLibrary);
            extrinsic.addClassification(authorClasification, DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        }
        
        Classification classCode = codeTransformer.toEbXML(docEntry.getClassCode(), objectLibrary);
        extrinsic.addClassification(classCode, DOC_ENTRY_CLASS_CODE_CLASS_SCHEME);
        
        Classification formatCode = codeTransformer.toEbXML(docEntry.getFormatCode(), objectLibrary);
        extrinsic.addClassification(formatCode, DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME);

        Classification healthcareFacility = codeTransformer.toEbXML(docEntry.getHealthcareFacilityTypeCode(), objectLibrary);
        extrinsic.addClassification(healthcareFacility, DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        
        Classification practiceSetting = codeTransformer.toEbXML(docEntry.getPracticeSettingCode(), objectLibrary);
        extrinsic.addClassification(practiceSetting, DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME);
        
        Classification typeCode = codeTransformer.toEbXML(docEntry.getTypeCode(), objectLibrary);
        extrinsic.addClassification(typeCode, DOC_ENTRY_TYPE_CODE_CLASS_SCHEME);
        
        for (Code confCode : docEntry.getConfidentialityCodes()) {
            Classification conf = codeTransformer.toEbXML(confCode, objectLibrary);
            extrinsic.addClassification(conf, DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME);
        }
        
        for (Code eventCode : docEntry.getEventCodeList()) {
            Classification event = codeTransformer.toEbXML(eventCode, objectLibrary);
            extrinsic.addClassification(event, DOC_ENTRY_EVENT_CODE_CLASS_SCHEME);
        }
    }
}
