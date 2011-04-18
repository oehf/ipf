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
package org.openehealth.ipf.commons.ihe.xds.core.validate.requests;

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.validate.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validation of an ebXML object container.
 * @author Jens Riemschneider
 */
public class ObjectContainerValidator implements Validator<EbXMLObjectContainer, ValidationProfile> {

    private final SlotLengthValidator slotLengthValidator = new SlotLengthValidator();
    private final OIDValidator oidValidator = new OIDValidator();
    private final TimeValidator timeValidator = new TimeValidator();
    private final XCNValidator xcnValidator = new XCNValidator();
    private final XONValidator xonValidator = new XONValidator();
    private final HashValidator hashValidator = new HashValidator();
    private final NopValidator nopValidator = new NopValidator();
    private final LanguageCodeValidator languageCodeValidator = new LanguageCodeValidator();
    private final PositiveNumberValidator positiveNumberValidator = new PositiveNumberValidator();
    private final PidValidator pidValidator = new PidValidator();
    private final UriValidator uriValidator = new UriValidator();
    private final RecipientListValidator recipientListValidator = new RecipientListValidator();
    private final CXValidator cxValidator = new CXValidator();
    
    private final SlotValueValidation[] authorValidations = new SlotValueValidation[] {
        new SlotValueValidation(SLOT_NAME_AUTHOR_PERSON, xcnValidator),
        new SlotValueValidation(SLOT_NAME_AUTHOR_INSTITUTION, xonValidator, 0, Integer.MAX_VALUE),
        new SlotValueValidation(SLOT_NAME_AUTHOR_ROLE, nopValidator, 0, Integer.MAX_VALUE),
        new SlotValueValidation(SLOT_NAME_AUTHOR_SPECIALTY, nopValidator, 0, Integer.MAX_VALUE)};
    
    private final SlotValueValidation[] codingSchemeValidations = new SlotValueValidation[] {
        new SlotValueValidation(SLOT_NAME_CODING_SCHEME, nopValidator)};
    
    private final RegistryObjectValidator[] docEntrySlotValidations = new RegistryObjectValidator[] {
        new SlotValueValidation(SLOT_NAME_CREATION_TIME, timeValidator),
        new SlotValueValidation(SLOT_NAME_SERVICE_START_TIME, timeValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_SERVICE_STOP_TIME, timeValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_HASH, hashValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_LANGUAGE_CODE, languageCodeValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_LEGAL_AUTHENTICATOR, xcnValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_SIZE, positiveNumberValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_SOURCE_PATIENT_ID, nopValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_SOURCE_PATIENT_INFO, pidValidator, 0, Integer.MAX_VALUE),
        new SlotValidation(SLOT_NAME_URI, uriValidator),
        new ClassificationValidation(DOC_ENTRY_AUTHOR_CLASS_SCHEME, 0, Integer.MAX_VALUE, authorValidations),
        new ClassificationValidation(DOC_ENTRY_CLASS_CODE_CLASS_SCHEME, codingSchemeValidations),
        new ClassificationValidation(DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME, 0, Integer.MAX_VALUE, codingSchemeValidations),
        new ClassificationValidation(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME, 0, Integer.MAX_VALUE, codingSchemeValidations),
        new ClassificationValidation(DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME, codingSchemeValidations),
        new ClassificationValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME, codingSchemeValidations),
        new ClassificationValidation(DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME, codingSchemeValidations),
        new ClassificationValidation(DOC_ENTRY_TYPE_CODE_CLASS_SCHEME, codingSchemeValidations),
        new ExternalIdentifierValidation(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, cxValidator)};

    private final RegistryObjectValidator[] docEntrySlotValidations30 = new RegistryObjectValidator[] {
        new SlotValueValidation(SLOT_NAME_REPOSITORY_UNIQUE_ID, oidValidator)};
    
    private final RegistryObjectValidator[] submissionSetSlotValidations = new RegistryObjectValidator[] {
        new SlotValidation(SLOT_NAME_INTENDED_RECIPIENT, recipientListValidator),
        new SlotValueValidation(SLOT_NAME_SUBMISSION_TIME, timeValidator),
        new ClassificationValidation(SUBMISSION_SET_AUTHOR_CLASS_SCHEME, 0, Integer.MAX_VALUE, authorValidations),
        new ClassificationValidation(SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME, codingSchemeValidations),
        new ExternalIdentifierValidation(SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID, cxValidator),
        new ExternalIdentifierValidation(SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID, oidValidator)};
    
    private final RegistryObjectValidator[] folderSlotValidations = new RegistryObjectValidator[] {
        new SlotValueValidation(SLOT_NAME_LAST_UPDATE_TIME, timeValidator, 0, 1),
        // The spec says that the code list is required to have at least 1 code. However, 
        // the XDStoolkit tests do currently not always provide a code. Therefore, we 
        // accept 0 codes as well.
        new ClassificationValidation(FOLDER_CODE_LIST_CLASS_SCHEME, 0, Integer.MAX_VALUE, codingSchemeValidations),
        new ExternalIdentifierValidation(FOLDER_PATIENT_ID_EXTERNAL_ID, cxValidator)};

    @Override
    public void validate(EbXMLObjectContainer container, ValidationProfile profile) {
        notNull(container, "container cannot be null");
        
        if (profile == null) {
            profile = new ValidationProfile();
        }
        
        slotLengthValidator.validate(container);
    
        // Note: The order of these checks is important!        
        validateSubmissionSet(container, profile);
        if (!profile.isQuery()) {
            validateUniquenessOfUUIDs(container);
            validateUniqueIds(container);
        }
        validateAssociations(container, profile);
        validateDocumentEntries(container, profile);
        validateFolders(container);
        if (!profile.isQuery()) {
            validatePatientIdsAreIdentical(container);
        } 
    }

    private void validateFolders(EbXMLObjectContainer container) throws XDSMetaDataException {
        for (EbXMLRegistryPackage folder : container.getRegistryPackages(FOLDER_CLASS_NODE)) {
            runValidations(folder, folderSlotValidations);
    
            AvailabilityStatus status = folder.getStatus();
            if (status != null) {
                metaDataAssert(status == AvailabilityStatus.APPROVED || status == AvailabilityStatus.SUBMITTED,
                        FOLDER_INVALID_AVAILABILITY_STATUS, status);
            }
        }
    }

    private void validateSubmissionSet(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        List<EbXMLRegistryPackage> submissionSets = container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE);
        if (!profile.isQuery()) {
            metaDataAssert(submissionSets.size() == 1, EXACTLY_ONE_SUBMISSION_SET_MUST_EXIST);
        }
    
        for (EbXMLRegistryPackage submissionSet : submissionSets) {
            runValidations(submissionSet, submissionSetSlotValidations);
        
            AvailabilityStatus status = submissionSet.getStatus();
            if (status != null) {
                metaDataAssert(status == AvailabilityStatus.APPROVED || status == AvailabilityStatus.SUBMITTED,
                        SUBMISSION_SET_INVALID_AVAILABILITY_STATUS, status);
            }
        }
    }

    private void validateDocumentEntries(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        for (EbXMLExtrinsicObject docEntry : container.getExtrinsicObjects(DOC_ENTRY_CLASS_NODE)) {
            runValidations(docEntry, docEntrySlotValidations);
            if (((profile.getIheProfile() == IheProfile.XDS_B) ||
                 (profile.getIheProfile() == IheProfile.XCA)) &&
                (profile.getActor() == Actor.REGISTRY))
            {
                runValidations(docEntry, docEntrySlotValidations30);
            }
            
            AvailabilityStatus status = docEntry.getStatus();
            if (status != null) {
                metaDataAssert(status == AvailabilityStatus.APPROVED || status == AvailabilityStatus.DEPRECATED,
                        DOC_ENTRY_INVALID_AVAILABILITY_STATUS, status);
            }
            
            LocalizedString name = docEntry.getName();
            if (name != null && name.getValue() != null) {
                metaDataAssert("UTF8".equals(name.getCharset()) || "UTF-8".equals(name.getCharset()),
                        INVALID_TITLE_ENCODING, name.getCharset());
                
                metaDataAssert(name.getValue().length() <= 128,
                        TITLE_TOO_LONG, name.getValue());
            }
        }
    }

    private void runValidations(EbXMLRegistryObject obj, RegistryObjectValidator[] validations) throws XDSMetaDataException {
        for (RegistryObjectValidator validation : validations) {
            validation.validate(obj);
        }
    }

    private void validateUniqueIds(EbXMLObjectContainer container) throws XDSMetaDataException {
        validateUniqueIds(container.getExtrinsicObjects(DOC_ENTRY_CLASS_NODE), DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID);
        validateUniqueIds(container.getRegistryPackages(FOLDER_CLASS_NODE), FOLDER_UNIQUE_ID_EXTERNAL_ID);
        validateUniqueIds(container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE), SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID);
    }

    private void validateUniqueIds(List<? extends EbXMLRegistryObject> objects, String scheme) throws XDSMetaDataException {
        for (EbXMLRegistryObject obj : objects) {
            String uniqueId = obj.getExternalIdentifierValue(scheme);
            metaDataAssert(uniqueId != null, UNIQUE_ID_MISSING);
            metaDataAssert(uniqueId.length() <= 128, UNIQUE_ID_TOO_LONG);
        }
    }

    private void validateUniquenessOfUUIDs(EbXMLObjectContainer container) throws XDSMetaDataException {
        Set<String> uuids = new HashSet<String>();
        addUUIDs(container.getAssociations(), uuids);
        addUUIDs(container.getExtrinsicObjects(), uuids);
        addUUIDs(container.getRegistryPackages(), uuids);
    }

    private void addUUIDs(List<? extends EbXMLRegistryObject> objects, Set<String> uuids) throws XDSMetaDataException {
        for (EbXMLRegistryObject obj : objects) {
            String uuid = obj.getId();
            if (uuid != null) {
                metaDataAssert(!uuids.contains(uuid), UUID_NOT_UNIQUE);
                uuids.add(uuid);
            }
        }
    }

    private void validatePatientIdsAreIdentical(EbXMLObjectContainer container) throws XDSMetaDataException {
        List<EbXMLRegistryPackage> submissionSets = container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE);
        EbXMLRegistryPackage submissionSet = submissionSets.get(0);
    
        String patientId = submissionSet.getExternalIdentifierValue(SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID);
    
        for (EbXMLExtrinsicObject docEntry : container.getExtrinsicObjects(DOC_ENTRY_CLASS_NODE)) {
            String patientIdDocEntry = docEntry.getExternalIdentifierValue(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID);
            metaDataAssert(patientId.equals(patientIdDocEntry), DOC_ENTRY_PATIENT_ID_WRONG);
        }
        
        for (EbXMLRegistryPackage folder : container.getRegistryPackages(FOLDER_CLASS_NODE)) {
            String patientIdFolder = folder.getExternalIdentifierValue(FOLDER_PATIENT_ID_EXTERNAL_ID);
            metaDataAssert(patientId.equals(patientIdFolder), FOLDER_PATIENT_ID_WRONG);
        }
    }

    private void validateAssociations(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        Set<String> docEntryIds = new HashSet<String>();
        for (EbXMLExtrinsicObject docEntry : container.getExtrinsicObjects(DOC_ENTRY_CLASS_NODE)) {
            if (docEntry.getId() != null) {
                docEntryIds.add(docEntry.getId());
            }
        }
        
        for (EbXMLAssociation association : container.getAssociations()) {
            AssociationType type = association.getAssociationType();
            metaDataAssert(type != null, INVALID_ASSOCIATION_TYPE);
    
            if (type != AssociationType.HAS_MEMBER) {
                validateDocumentRelationship(association, docEntryIds, profile);
            }
            else {
                validateAssociation(association, docEntryIds, profile);
            }
        }
    }

    private void validateAssociation(EbXMLAssociation association, Set<String> docEntryIds, ValidationProfile profile) throws XDSMetaDataException {
        metaDataAssert(association.getSingleClassification(Vocabulary.ASSOCIATION_DOC_CODE_CLASS_SCHEME) == null,
                DOC_CODE_NOT_ALLOWED_ON_HAS_MEMBER);

        List<String> slotValues = association.getSlotValues(SLOT_NAME_SUBMISSION_SET_STATUS);
        if (!slotValues.isEmpty()) {
            metaDataAssert(slotValues.size() == 1, TOO_MANY_SUBMISSION_SET_STATES);
            
            AssociationLabel status = AssociationLabel.fromOpcode(slotValues.get(0));
            metaDataAssert(status != null, INVALID_SUBMISSION_SET_STATUS);
    
            if (status == AssociationLabel.ORIGINAL && !profile.isQuery()) {
                metaDataAssert(docEntryIds.contains(association.getTarget()), MISSING_ORIGINAL);
            }
        }
    }

    private void validateDocumentRelationship(EbXMLAssociation association, Set<String> docEntryIds, ValidationProfile profile)throws XDSMetaDataException {
        if (!profile.isQuery()) {
            metaDataAssert(docEntryIds.contains(association.getSource()), SOURCE_UUID_NOT_FOUND);
        }
    }
}