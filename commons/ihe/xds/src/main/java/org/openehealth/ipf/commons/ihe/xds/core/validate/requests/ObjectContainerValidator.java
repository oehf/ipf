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

import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.*;
import org.openehealth.ipf.commons.ihe.xds.core.XdsRuntimeException;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.validate.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage.OPTIONAL;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage.REQUIRED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validation of an ebXML object container.
 * @author Jens Riemschneider
 */
public class ObjectContainerValidator implements Validator<EbXMLObjectContainer, ValidationProfile> {

    private final SlotLengthAndNameUniquenessValidator slotLengthAndNameUniquenessValidator =
            new SlotLengthAndNameUniquenessValidator();
    private final OIDValidator oidValidator = new OIDValidator();
    private final TimeValidator timeValidator = new TimeValidator();
    private final TimeValidator timeValidatorSec = new TimeValidator(14);
    private final XCNValidator xcnValidator = new XCNValidator();
    private final XONValidator xonValidator = new XONValidator();
    private final HashValidator hashValidator = new HashValidator();
    private final NopValidator nopValidator = new NopValidator();
    private final LanguageCodeValidator languageCodeValidator = new LanguageCodeValidator();
    private final PositiveNumberValidator positiveNumberValidator = new PositiveNumberValidator();
    private final PidValidator pidValidator = new PidValidator();
    private final UriValidator uriValidator = new UriValidator();
    private final RecipientListValidator recipientListValidator = new RecipientListValidator();
    private final CXValidator cxValidatorRequiredAA = new CXValidator(true);
    private final CXValidator cxValidatorOptionalAA = new CXValidator(false);
    private final XTNValidator xtnValidator = new XTNValidator();
    private final CXiValidator cxiValidator = new CXiValidator();
    private final UUIDValidator uuidValidator = new UUIDValidator();
    private final IdentifierValidator identifierValidator = new IdentifierValidator();
    private final MimeTypeValidator mimeTypeValidator = new MimeTypeValidator();

    private final SlotValueValidation[] authorValidations = new SlotValueValidation[] {
        new SlotValueValidation(SLOT_NAME_AUTHOR_PERSON, xcnValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_AUTHOR_INSTITUTION, xonValidator, 0, Integer.MAX_VALUE),
        new SlotValueValidation(SLOT_NAME_AUTHOR_ROLE, cxValidatorOptionalAA, 0, Integer.MAX_VALUE),
        new SlotValueValidation(SLOT_NAME_AUTHOR_SPECIALTY, cxValidatorOptionalAA, 0, Integer.MAX_VALUE),
        new SlotValueValidation(SLOT_NAME_AUTHOR_TELECOM, xtnValidator, 0, Integer.MAX_VALUE)};

    private final SlotValueValidation[] codingSchemeValidations = new SlotValueValidation[] {
        new SlotValueValidation(SLOT_NAME_CODING_SCHEME, nopValidator)};


    private List<RegistryObjectValidator> documentEntrySlotValidators(ValidationProfile profile, boolean onDemandProvided, boolean limitedMetadata) {
        var isContinuaHRN = (profile == CONTINUA_HRN.Interactions.ITI_41);
        var requiredOnlyForContinuaHRN = isContinuaHRN ? REQUIRED : OPTIONAL;
        var isOnDemand = (profile == XDS.Interactions.ITI_61) || (profile.isQuery() && onDemandProvided);
        var needHashAndSize = (! isOnDemand) &&
                (isContinuaHRN || profile.isQuery() || (profile == XDS.Interactions.ITI_42) || (profile == XDM.Interactions.ITI_41));
        var needPatientId = (! limitedMetadata) && (profile != XCDR.Interactions.ITI_80);
        var needRepositoryUniqueId = (profile.getInteractionId() == XDS.Interactions.ITI_42) || isOnDemand || profile.isQuery();

        var validators = new ArrayList<RegistryObjectValidator>();
        Collections.addAll(validators,
            new SlotValueValidation(SLOT_NAME_CREATION_TIME, timeValidator,
                    (limitedMetadata || isOnDemand) ? 0 : 1,
                    isOnDemand ? 0 : 1),
            new SlotValueValidation(SLOT_NAME_SERVICE_START_TIME, timeValidator, 0, 1),
            new SlotValueValidation(SLOT_NAME_SERVICE_STOP_TIME, timeValidator, 0, 1),
            new ServiceTimeChronologyValidation(),
            new SlotValueValidation(SLOT_NAME_HASH, hashValidator,
                    needHashAndSize ? 1 : 0,
                    isOnDemand ? 0 : 1),
            new SlotValueValidation(SLOT_NAME_LANGUAGE_CODE, languageCodeValidator, limitedMetadata ? 0 : 1, 1),
            new SlotValueValidation(SLOT_NAME_LEGAL_AUTHENTICATOR, xcnValidator, 0,
                    (isOnDemand || isContinuaHRN) ? 0 : 1),
            new SlotValueValidation(SLOT_NAME_SIZE, positiveNumberValidator,
                    needHashAndSize ? 1 : 0,
                    isOnDemand ? 0 : 1),
            new SlotValueValidation(SLOT_NAME_SOURCE_PATIENT_ID, cxValidatorRequiredAA,
                    (profile.isEbXml30Based() && (! limitedMetadata)) ? 1 : 0, 1),
            new SlotValueValidation(SLOT_NAME_SOURCE_PATIENT_INFO, pidValidator,
                    isContinuaHRN ? 1 : 0, Integer.MAX_VALUE),
            new SlotValueValidation(SLOT_NAME_REFERENCE_ID_LIST, cxiValidator, 0, Integer.MAX_VALUE),
            new SlotValueValidation(SLOT_NAME_URI, uriValidator, 0, 1),
            new AuthorClassificationValidation(DOC_ENTRY_AUTHOR_CLASS_SCHEME, authorValidations),
            new ClassificationValidation(DOC_ENTRY_CLASS_CODE_CLASS_SCHEME,
                    limitedMetadata ? 0 : 1, 1, requiredOnlyForContinuaHRN, codingSchemeValidations),
            new ClassificationValidation(DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME,
                    limitedMetadata ? 0 : 1, Integer.MAX_VALUE,
                    requiredOnlyForContinuaHRN, codingSchemeValidations),
            new ClassificationValidation(DOC_ENTRY_EVENT_CODE_CLASS_SCHEME, 0, Integer.MAX_VALUE,
                    requiredOnlyForContinuaHRN, codingSchemeValidations),
            new ClassificationValidation(DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME,
                    limitedMetadata ? 0 : 1, 1, OPTIONAL, codingSchemeValidations),
            new ClassificationValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME,
                    limitedMetadata ? 0 : 1, 1, requiredOnlyForContinuaHRN, codingSchemeValidations),
            new ClassificationValidation(DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME,
                    limitedMetadata ? 0 : 1, 1, requiredOnlyForContinuaHRN, codingSchemeValidations),
            new ClassificationValidation(DOC_ENTRY_TYPE_CODE_CLASS_SCHEME,
                    limitedMetadata ? 0 : 1, 1, requiredOnlyForContinuaHRN, codingSchemeValidations),
            new ExternalIdentifierValidation(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, cxValidatorRequiredAA, needPatientId),
            new SlotValueValidation(SLOT_NAME_REPOSITORY_UNIQUE_ID, oidValidator, needRepositoryUniqueId ? 1 : 0, 1));

        return validators;
    }

    private List<RegistryObjectValidator> getSubmissionSetSlotValidations(ValidationProfile profile, boolean limitedMetadata) {
        var isContinuaHRN = (profile == CONTINUA_HRN.Interactions.ITI_41);
        var requiredOnlyForContinuaHRN = isContinuaHRN ? REQUIRED : OPTIONAL;
        var needPatientId = (! limitedMetadata) && (profile != XCDR.Interactions.ITI_80);

        var validators = new ArrayList<RegistryObjectValidator>();
        Collections.addAll(validators,
                new SlotValidation(SLOT_NAME_INTENDED_RECIPIENT, recipientListValidator),
                new SlotValueValidation(SLOT_NAME_SUBMISSION_TIME, timeValidator),
                new AuthorClassificationValidation(SUBMISSION_SET_AUTHOR_CLASS_SCHEME, authorValidations),
                new ClassificationValidation(SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME,
                        limitedMetadata ? 0 : 1, 1, requiredOnlyForContinuaHRN, codingSchemeValidations),
                new ExternalIdentifierValidation(SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID, oidValidator, true),
                new ExternalIdentifierValidation(SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID, cxValidatorRequiredAA, needPatientId));

        return validators;
    }

    private List<RegistryObjectValidator> getFolderSlotValidations(ValidationProfile profile, boolean limitedMetadata) {
        var needPatientId = (! limitedMetadata) && (profile != XCDR.Interactions.ITI_80);

        var validators = new ArrayList<RegistryObjectValidator>();
        Collections.addAll(validators,
                new SlotValueValidation(SLOT_NAME_LAST_UPDATE_TIME, timeValidatorSec, 0, 1),
                new ClassificationValidation(FOLDER_CODE_LIST_CLASS_SCHEME,
                        limitedMetadata ? 0 : 1, Integer.MAX_VALUE, OPTIONAL, codingSchemeValidations),
                new ExternalIdentifierValidation(FOLDER_PATIENT_ID_EXTERNAL_ID, cxValidatorRequiredAA, needPatientId));

        return validators;
    }

    private boolean checkLimitedMetadata(EbXMLRegistryObject object, String limitedMetadataClassNode, ValidationProfile profile) {
        var limitedMetadata = object.getClassifications().stream().anyMatch(cl -> limitedMetadataClassNode.equals(cl.getClassificationNode()));
        if (limitedMetadata) {
            metaDataAssert((profile == XDM.Interactions.ITI_41) || (profile == XDR.Interactions.ITI_41),
                    ValidationMessage.LIMITED_METADATA_PROHIBITED, object.getId());
        } else {
            metaDataAssert(profile != XDR.Interactions.ITI_41,
                    ValidationMessage.LIMITED_METADATA_REQUIRED, object.getId());
        }
        return limitedMetadata;
    }


    @Override
    public void validate(EbXMLObjectContainer container, ValidationProfile profile) {
        requireNonNull(container, "container cannot be null");
        requireNonNull(profile, "profile must be set");

        slotLengthAndNameUniquenessValidator.validateContainer(container);

        // Note: The order of these checks is important!
        validateSubmissionSet(container, profile);
        if (!profile.isQuery()) {
            validateUniquenessOfUUIDs(container);
            validateUniqueIds(container, profile);
        }
        validateAssociations(container, profile);
        validateDocumentEntries(container, profile);
        validateFolders(container, profile);
        if (!profile.isQuery()) {
            validatePatientIdsAreIdentical(container);
        }
    }

    private void validateFolders(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        var logicalIds = new HashSet<String>();
        for (var folder : container.getRegistryPackages(FOLDER_CLASS_NODE)) {
             if (profile == RMU.Interactions.ITI_92) {
                 throw new XdsRuntimeException(ErrorCode.OBJECT_TYPE_ERROR, "Folders cannot be updated", Severity.ERROR, folder.getId());
             }

            var limitedMetadata = checkLimitedMetadata(folder, FOLDER_LIMITED_METADATA_CLASS_NODE, profile);
            runValidations(folder, getFolderSlotValidations(profile, limitedMetadata));

            var status = folder.getStatus();
            if (profile.isQuery() || status != null) {
                metaDataAssert(status == AvailabilityStatus.APPROVED,
                        FOLDER_INVALID_AVAILABILITY_STATUS, status);
            }

            metaDataAssert(StringUtils.isBlank(folder.getLid()) || logicalIds.add(folder.getLid()),
                    LOGICAL_ID_SAME, folder.getLid());

            var name = folder.getName();
            metaDataAssert(limitedMetadata || ((name != null) && (name.getValue() != null)),
                    MISSING_FOLDER_NAME, folder.getId());

            if (profile == XDS.Interactions.ITI_57) {
                validateUpdateObject(folder, container, profile);
            }
        }
    }

    private void validateSubmissionSet(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        var submissionSets = container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE);
        if (!profile.isQuery()) {
            metaDataAssert(submissionSets.size() == 1, EXACTLY_ONE_SUBMISSION_SET_MUST_EXIST);
        }

        for (var submissionSet : submissionSets) {
            var limitedMetadata = checkLimitedMetadata(submissionSet, SUBMISSION_SET_LIMITED_METADATA_CLASS_NODE, profile);
            runValidations(submissionSet, getSubmissionSetSlotValidations(profile, limitedMetadata));

            var status = submissionSet.getStatus();
            if (profile.isQuery() || (status != null)) {
                metaDataAssert(status == AvailabilityStatus.APPROVED,
                        SUBMISSION_SET_INVALID_AVAILABILITY_STATUS, status);
            }
        }
    }

    private void validateDocumentEntries(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        var logicalIds = new HashSet<String>();
        for (var docEntry : container.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND)) {
            var limitedMetadata = checkLimitedMetadata(docEntry, DOC_ENTRY_LIMITED_METADATA_CLASS_NODE, profile);

            // on-demand is required for ITI-61 and allowed for ITI-92
            var onDemandProvided = DocumentEntryType.ON_DEMAND.getUuid().equals(docEntry.getObjectType());
            if (onDemandProvided) {
                metaDataAssert((profile == XDS.Interactions.ITI_61) || (profile == RMU.Interactions.ITI_92) || profile.isQuery(),
                        WRONG_DOCUMENT_ENTRY_TYPE, docEntry.getObjectType());
            } else {
                metaDataAssert(profile != XDS.Interactions.ITI_61, WRONG_DOCUMENT_ENTRY_TYPE, docEntry.getObjectType());
            }

            runValidations(docEntry, documentEntrySlotValidators(profile, onDemandProvided, limitedMetadata));

            if (profile.isQuery()) {
                var status = docEntry.getStatus();
                metaDataAssert(status == AvailabilityStatus.APPROVED || status == AvailabilityStatus.DEPRECATED,
                        DOC_ENTRY_INVALID_AVAILABILITY_STATUS, status);
            }

            var name = docEntry.getName();
            if (name != null && name.getValue() != null) {
                metaDataAssert("UTF8".equals(name.getCharset()) || "UTF-8".equals(name.getCharset()),
                        INVALID_TITLE_ENCODING, name.getCharset());

                metaDataAssert(name.getValue().length() <= 128,
                        TITLE_TOO_LONG, name.getValue());
            }

            var attachmentExpected = (profile.getInteractionId() == XCF.Interactions.ITI_63);
            var attachmentProvided = (docEntry.getDataHandler() != null);
            metaDataAssert(attachmentProvided == attachmentExpected,
                    attachmentExpected ? MISSING_DOCUMENT_FOR_DOC_ENTRY : DOCUMENT_NOT_ALLOWED_IN_DOC_ENTRY,
                    docEntry.getId());

            metaDataAssert(profile.isQuery() || StringUtils.isBlank(docEntry.getLid()) || logicalIds.add(docEntry.getLid()),
                    LOGICAL_ID_SAME, docEntry.getLid());

            var mimeType = docEntry.getMimeType();
            metaDataAssert(StringUtils.isNotEmpty(mimeType), MIME_TYPE_MUST_BE_SPECIFIED);
            mimeTypeValidator.validate(mimeType);

            if ((profile == XDS.Interactions.ITI_57) || (profile == RMU.Interactions.ITI_92)) {
                validateUpdateObject(docEntry, container, profile);
            }
        }
    }

    private void runValidations(EbXMLRegistryObject obj, List<RegistryObjectValidator> validations) throws XDSMetaDataException {
        for (var validation : validations) {
            validation.validate(obj);
        }
    }

    private void validateUniqueIds(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        var idsInRequest = new HashSet<String>();
        var validationMsg = profile == XDS.Interactions.ITI_41 ?  UNIQUE_ID_NOT_UNIQUE_REPO : UNIQUE_ID_NOT_UNIQUE;
        var uniquenessValidator = (ValueValidator) (uniqueId) -> metaDataAssert(idsInRequest.add(uniqueId), validationMsg, uniqueId);
        validateUniqueIds(container.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND), DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID, uniquenessValidator, identifierValidator);
        validateUniqueIds(container.getRegistryPackages(FOLDER_CLASS_NODE), FOLDER_UNIQUE_ID_EXTERNAL_ID, uniquenessValidator, oidValidator);
        validateUniqueIds(container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE), SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID, uniquenessValidator, oidValidator);
    }

    private void validateUniqueIds(
            List<? extends EbXMLRegistryObject> objects,
            String scheme,
            ValueValidator uniquenessValidator,
            ValueValidator formatValidator) throws XDSMetaDataException
    {
        for (EbXMLRegistryObject obj : objects) {
            var uniqueId = obj.getExternalIdentifierValue(scheme);
            metaDataAssert(uniqueId != null, UNIQUE_ID_MISSING);
            metaDataAssert(uniqueId.length() <= 128, UNIQUE_ID_TOO_LONG);
            uniquenessValidator.validate(uniqueId);
            formatValidator.validate(uniqueId);
        }
    }

    private void validateUniquenessOfUUIDs(EbXMLObjectContainer container) throws XDSMetaDataException {
        var uuids = new HashSet<String>();
        addUUIDs(container.getAssociations(), uuids);
        addUUIDs(container.getExtrinsicObjects(), uuids);
        addUUIDs(container.getRegistryPackages(), uuids);
    }

    private void addUUIDs(List<? extends EbXMLRegistryObject> objects, Set<String> uuids) throws XDSMetaDataException {
        for (EbXMLRegistryObject obj : objects) {
            var uuid = obj.getId();
            if (uuid != null) {
                metaDataAssert(uuids.add(uuid), UUID_NOT_UNIQUE);
            }
        }
    }

    private void validatePatientIdsAreIdentical(EbXMLObjectContainer container) throws XDSMetaDataException {
        var submissionSets = container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE);
        var submissionSet = submissionSets.get(0);

        var patientId = submissionSet.getExternalIdentifierValue(SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID);

        for (var docEntry : container.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND)) {
            var patientIdDocEntry = docEntry.getExternalIdentifierValue(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID);
            metaDataAssert(StringUtils.equals(patientId, patientIdDocEntry), DOC_ENTRY_PATIENT_ID_WRONG);
        }

        for (var folder : container.getRegistryPackages(FOLDER_CLASS_NODE)) {
            var patientIdFolder = folder.getExternalIdentifierValue(FOLDER_PATIENT_ID_EXTERNAL_ID);
            metaDataAssert(StringUtils.equals(patientId, patientIdFolder), FOLDER_PATIENT_ID_WRONG);
        }
    }

    private void validateAssociations(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        var logicalIds = new HashSet<String>();
        var docEntryIds = container.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND).stream()
                .filter(docEntry -> docEntry.getId() != null)
                .map(EbXMLRegistryObject::getId)
                .collect(Collectors.toSet());
        var submissionSetIds = container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE).stream()
                .map(EbXMLRegistryObject::getId)
                .collect(Collectors.toSet());
        var associationIds = new HashSet<String>();
        var hasSubmitAssociationType = false;
        for (var association : container.getAssociations()) {
            //metaDataAssert(StringUtils.isNotEmpty(association.getId()), ASSOCIATION_ID_MISSING);
            associationIds.add(association.getId());

            var type = association.getAssociationType();
            metaDataAssert(type != null, INVALID_ASSOCIATION_TYPE);
            hasSubmitAssociationType = hasSubmitAssociationType
                    || (type == AssociationType.SUBMIT_ASSOCIATION)
                    || (type == AssociationType.UPDATE_AVAILABILITY_STATUS);

            metaDataAssert(StringUtils.isBlank(association.getLid()) || logicalIds.add(association.getLid()),
                    LOGICAL_ID_SAME, association.getLid());
        }

        for (var association : container.getAssociations()) {
            switch (association.getAssociationType()) {
                case HAS_MEMBER:
                    var isSubmissionSetToDocEntry =
                            submissionSetIds.contains(association.getSource()) && docEntryIds.contains(association.getTarget());
                    validateAssociation(association, docEntryIds, profile, isSubmissionSetToDocEntry);
                    break;

                case IS_SNAPSHOT_OF:
                    if (!profile.isQuery()) {
                        var sourceDocEntry = getExtrinsicObject(
                                container, association.getSource(), DocumentEntryType.STABLE.getUuid());
                        metaDataAssert(sourceDocEntry != null, MISSING_SNAPSHOT_ASSOCIATION, "sourceObject", association.getSource());
                        metaDataAssert(hasSubmitAssociationType || docEntryIds.contains(association.getSource()), SOURCE_UUID_NOT_FOUND);
                    }
                    break;

                case UPDATE_AVAILABILITY_STATUS:
                    if (!profile.isQuery()) {
                        metaDataAssert(submissionSetIds.contains(association.getSource()), MISSING_SUBMISSION_SET, association.getSource());
                        metaDataAssert(association.getOriginalStatus() != null, MISSING_ORIGINAL_STATUS);
                        metaDataAssert(association.getNewStatus() != null, MISSING_NEW_STATUS);
                    }
                    break;

                case SUBMIT_ASSOCIATION:
                    if (!profile.isQuery()) {
                        metaDataAssert(submissionSetIds.contains(association.getSource()), MISSING_SUBMISSION_SET, association.getSource());
                        metaDataAssert(associationIds.contains(association.getTarget()), MISSING_ASSOCIATION, association.getTarget());
                    }
                    break;

                default:
                    metaDataAssert(profile.isQuery() || hasSubmitAssociationType || docEntryIds.contains(association.getSource()), SOURCE_UUID_NOT_FOUND);
            }
        }
    }

    private void validateAssociation(EbXMLAssociation association, Set<String> docEntryIds,
                                     ValidationProfile profile, boolean isSubmissionSetToDocEntry) throws XDSMetaDataException {
        metaDataAssert(association.getSingleClassification(Vocabulary.ASSOCIATION_DOC_CODE_CLASS_SCHEME) == null,
                DOC_CODE_NOT_ALLOWED_ON_HAS_MEMBER);

        var slotValues = association.getSlotValues(SLOT_NAME_SUBMISSION_SET_STATUS);
        if (isSubmissionSetToDocEntry){
            metaDataAssert(!slotValues.isEmpty(), SUBMISSION_SET_STATUS_MANDATORY);
        }
        if (!slotValues.isEmpty()) {
            metaDataAssert(slotValues.size() == 1, TOO_MANY_SUBMISSION_SET_STATES);

            var status = AssociationLabel.fromOpcode(slotValues.get(0));

            if (status == AssociationLabel.ORIGINAL && !profile.isQuery()) {
                metaDataAssert(docEntryIds.contains(association.getTarget()), MISSING_ORIGINAL);
            }
        }
    }

    private EbXMLExtrinsicObject getExtrinsicObject(EbXMLObjectContainer container, String docEntryId, String... objectTypes) {
        for (var docEntry : container.getExtrinsicObjects(objectTypes)) {
            if (docEntry.getId() != null && docEntry.getId().equals(docEntryId)) {
                return docEntry;
            }
        }
        return null;
    }

    private EbXMLRegistryPackage getRegistryPackage(EbXMLObjectContainer container, String submissionSetId, String classificationNode) {
        for (var registryPackage : container.getRegistryPackages(classificationNode)) {
            if (registryPackage.getId() != null && registryPackage.getId().equals(submissionSetId)) {
                return registryPackage;
            }
        }
        return null;
    }

    private void validateUpdateObject(EbXMLRegistryObject registryObject, EbXMLObjectContainer container, ValidationProfile profile) {

        // logicalId is required for ITI-57 and optional for ITI-92
        var logicalId = registryObject.getLid();
        if (logicalId != null) {
            uuidValidator.validate(logicalId);
            if (logicalId.equals(registryObject.getId())) {
                throw new XdsRuntimeException(ErrorCode.INVALID_REQUEST_EXCEPTION,
                        "Initial version of the object was received in an update transaction",
                        Severity.ERROR, registryObject.getId());
            }
        } else if (profile == XDS.Interactions.ITI_57) {
            throw new XdsRuntimeException(
                    ErrorCode.METADATA_UPDATE_ERROR,
                    "logical ID is missing in the XDS Metadata Update request",
                    Severity.ERROR,
                    registryObject.getId());
        }

        var foundHasMemberAssociation = false;
        for (var association : container.getAssociations()) {
            if (association.getAssociationType() == AssociationType.HAS_MEMBER
                && association.getTarget().equals(registryObject.getId())
                && (getRegistryPackage(container, association.getSource(), SUBMISSION_SET_CLASS_NODE) != null))
            {
                if (association.getPreviousVersion() == null) {
                    throw new XdsRuntimeException(ErrorCode.METADATA_VERSION_ERROR,
                            "previous version is missing in the SS-DE HasMember association",
                            Severity.ERROR, association.getId());
                }

                if ((profile == RMU.Interactions.ITI_92) && Boolean.FALSE.equals(association.getAssociationPropagation())) {
                    throw new XdsRuntimeException(ErrorCode.METADATA_ANNOTATION_ERROR,
                            "association propagation shall be not disabled in the SS-DE HasMember association",
                            Severity.ERROR, association.getId());
                }

                foundHasMemberAssociation = true;
            }
        }
        if (!foundHasMemberAssociation) {
            throw new XdsRuntimeException(ErrorCode.METADATA_ANNOTATION_ERROR,
                    "SS-DE HasMember association is missing", Severity.ERROR, registryObject.getId());
        }
    }
}