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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp.toHL7;

import org.openehealth.ipf.commons.ihe.xds.core.transform.hl7.PatientInfoTransformer;

/**
 * Transforms between a {@link DocumentEntry} and its ebXML representation.
 * @author Jens Riemschneider
 */
public class DocumentEntryTransformer extends XDSMetaClassTransformer<EbXMLExtrinsicObject, DocumentEntry> {
    private final AuthorTransformer authorTransformer;
    private final CodeTransformer codeTransformer;

    private final PatientInfoTransformer patientInfoTransformer = new PatientInfoTransformer();

    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects.
     */
    public DocumentEntryTransformer(EbXMLFactory factory) {
        super(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID,
                DOC_ENTRY_LOCALIZED_STRING_PATIENT_ID,
                DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID,
                DOC_ENTRY_LOCALIZED_STRING_UNIQUE_ID,
                DOC_ENTRY_LIMITED_METADATA_CLASS_NODE,
                factory);

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
        docEntry.setUri(extrinsic.getSingleSlotValue(SLOT_NAME_URI));
        docEntry.setDocumentAvailability(DocumentAvailability.valueOfOpcode(
                extrinsic.getSingleSlotValue(SLOT_NAME_DOCUMENT_AVAILABILITY)));

        var size = extrinsic.getSingleSlotValue(SLOT_NAME_SIZE);
        docEntry.setSize(size != null ? Long.parseLong(size) : null);

        var hl7LegalAuthenticator = extrinsic.getSingleSlotValue(SLOT_NAME_LEGAL_AUTHENTICATOR);
        docEntry.setLegalAuthenticator(Hl7v2Based.parse(hl7LegalAuthenticator, Person.class));

        var sourcePatient = extrinsic.getSingleSlotValue(SLOT_NAME_SOURCE_PATIENT_ID);
        docEntry.setSourcePatientId(Hl7v2Based.parse(sourcePatient, Identifiable.class));

        var slotValues = extrinsic.getSlotValues(SLOT_NAME_SOURCE_PATIENT_INFO);
        docEntry.setSourcePatientInfo(patientInfoTransformer.fromHL7(slotValues));

        for (var referenceIdValue : extrinsic.getSlotValues(SLOT_NAME_REFERENCE_ID_LIST)) {
            docEntry.getReferenceIdList().add(Hl7v2Based.parse(referenceIdValue, ReferenceId.class));
        }
    }

    @Override
    protected void addSlots(DocumentEntry docEntry, EbXMLExtrinsicObject extrinsic, EbXMLObjectLibrary objectLibrary) {
        super.addSlots(docEntry, extrinsic, objectLibrary);

        extrinsic.addSlot(SLOT_NAME_CREATION_TIME, toHL7(docEntry.getCreationTime()));
        extrinsic.addSlot(SLOT_NAME_HASH, docEntry.getHash());
        extrinsic.addSlot(SLOT_NAME_LANGUAGE_CODE, docEntry.getLanguageCode());
        extrinsic.addSlot(SLOT_NAME_SERVICE_START_TIME, toHL7(docEntry.getServiceStartTime()));
        extrinsic.addSlot(SLOT_NAME_SERVICE_STOP_TIME, toHL7(docEntry.getServiceStopTime()));
        extrinsic.addSlot(SLOT_NAME_REPOSITORY_UNIQUE_ID, docEntry.getRepositoryUniqueId());
        extrinsic.addSlot(SLOT_NAME_URI, docEntry.getUri());
        extrinsic.addSlot(SLOT_NAME_DOCUMENT_AVAILABILITY,
                DocumentAvailability.toFullQualifiedOpcode(docEntry.getDocumentAvailability()));

        var size = docEntry.getSize();
        extrinsic.addSlot(SLOT_NAME_SIZE, size != null ? size.toString() : null);

        var hl7LegalAuthenticator = Hl7v2Based.render(docEntry.getLegalAuthenticator());
        extrinsic.addSlot(SLOT_NAME_LEGAL_AUTHENTICATOR, hl7LegalAuthenticator);

        var sourcePatient = Hl7v2Based.render(docEntry.getSourcePatientId());
        extrinsic.addSlot(SLOT_NAME_SOURCE_PATIENT_ID, sourcePatient);

        var slotValues = patientInfoTransformer.toHL7(docEntry.getSourcePatientInfo());
        extrinsic.addSlot(SLOT_NAME_SOURCE_PATIENT_INFO, slotValues.toArray(new String[0]));

        if (! docEntry.getReferenceIdList().isEmpty()) {
            var referenceIdValues = docEntry.getReferenceIdList().stream()
                    .map(Hl7v2Based::render)
                    .toArray(String[]::new);
            extrinsic.addSlot(SLOT_NAME_REFERENCE_ID_LIST, referenceIdValues);
        }
    }

    @Override
    protected void addClassificationsFromEbXML(DocumentEntry docEntry, EbXMLExtrinsicObject extrinsic) {
        super.addClassificationsFromEbXML(docEntry, extrinsic);

        extrinsic.getClassifications(DOC_ENTRY_AUTHOR_CLASS_SCHEME)
                .forEach(author -> docEntry.getAuthors().add(authorTransformer.fromEbXML(author)));

        var classCode = extrinsic.getSingleClassification(DOC_ENTRY_CLASS_CODE_CLASS_SCHEME);
        docEntry.setClassCode(codeTransformer.fromEbXML(classCode));

        var formatCode = extrinsic.getSingleClassification(DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME);
        docEntry.setFormatCode(codeTransformer.fromEbXML(formatCode));

        var healthcareFacility = extrinsic.getSingleClassification(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);
        docEntry.setHealthcareFacilityTypeCode(codeTransformer.fromEbXML(healthcareFacility));

        var practiceSetting = extrinsic.getSingleClassification(DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME);
        docEntry.setPracticeSettingCode(codeTransformer.fromEbXML(practiceSetting));

        var typeCode = extrinsic.getSingleClassification(DOC_ENTRY_TYPE_CODE_CLASS_SCHEME);
        docEntry.setTypeCode(codeTransformer.fromEbXML(typeCode));

        var confidentialityCodes = docEntry.getConfidentialityCodes();
        confidentialityCodes.addAll(extrinsic.getClassifications(DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME).stream()
                .map(codeTransformer::fromEbXML)
                .toList());

        var eventCodeList = docEntry.getEventCodeList();
        eventCodeList.addAll(extrinsic.getClassifications(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME).stream()
                .map(codeTransformer::fromEbXML)
                .toList());

    }

    @Override
    protected void addClassifications(DocumentEntry docEntry, EbXMLExtrinsicObject extrinsic, EbXMLObjectLibrary objectLibrary) {
        super.addClassifications(docEntry, extrinsic, objectLibrary);

        docEntry.getAuthors().stream()
                .map(author -> authorTransformer.toEbXML(author, objectLibrary))
                .forEach(authorClassification -> extrinsic.addClassification(authorClassification, DOC_ENTRY_AUTHOR_CLASS_SCHEME));

        var classCode = codeTransformer.toEbXML(docEntry.getClassCode(), objectLibrary);
        extrinsic.addClassification(classCode, DOC_ENTRY_CLASS_CODE_CLASS_SCHEME);

        var formatCode = codeTransformer.toEbXML(docEntry.getFormatCode(), objectLibrary);
        extrinsic.addClassification(formatCode, DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME);

        var healthcareFacility = codeTransformer.toEbXML(docEntry.getHealthcareFacilityTypeCode(), objectLibrary);
        extrinsic.addClassification(healthcareFacility, DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME);

        var practiceSetting = codeTransformer.toEbXML(docEntry.getPracticeSettingCode(), objectLibrary);
        extrinsic.addClassification(practiceSetting, DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME);

        var typeCode = codeTransformer.toEbXML(docEntry.getTypeCode(), objectLibrary);
        extrinsic.addClassification(typeCode, DOC_ENTRY_TYPE_CODE_CLASS_SCHEME);

        docEntry.getConfidentialityCodes().stream()
                .map(confCode -> codeTransformer.toEbXML(confCode, objectLibrary))
                .forEach(conf -> extrinsic.addClassification(conf, DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME));

        docEntry.getEventCodeList().stream()
                .map(eventCode -> codeTransformer.toEbXML(eventCode, objectLibrary))
                .forEach(event -> extrinsic.addClassification(event, DOC_ENTRY_EVENT_CODE_CLASS_SCHEME));
    }
}
