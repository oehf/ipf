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
import org.openehealth.ipf.commons.ihe.xds.CONTINUA_HRN;
import org.openehealth.ipf.commons.ihe.xds.XCF;
import org.openehealth.ipf.commons.ihe.xds.XDM;
import org.openehealth.ipf.commons.ihe.xds.XDR;
import org.openehealth.ipf.commons.ihe.xds.XDS_B;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExtrinsicObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectContainer;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationLabel;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.validate.AuthorClassificationValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.CXValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.CXiValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ClassificationValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ExternalIdentifierValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HashValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.LanguageCodeValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.NopValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.OIDValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.PidValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.PositiveNumberValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.RecipientListValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.RegistryObjectValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.SlotLengthAndNameUniquenessValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.SlotValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.SlotValueValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.TimeValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.UUIDValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.UriValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XCNValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XONValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XTNValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_CLASS_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_CONFIDENTIALITY_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_EVENT_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_FORMAT_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_LIMITED_METADATA_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_PATIENT_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_PRACTICE_SETTING_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_TYPE_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage.OPTIONAL;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.DisplayNameUsage.REQUIRED;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.FOLDER_CLASS_NODE;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.FOLDER_CODE_LIST_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.FOLDER_PATIENT_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.FOLDER_UNIQUE_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_AUTHOR_INSTITUTION;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_AUTHOR_PERSON;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_AUTHOR_ROLE;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_AUTHOR_SPECIALTY;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_AUTHOR_TELECOM;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_CODING_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_CREATION_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_HASH;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_INTENDED_RECIPIENT;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_LANGUAGE_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_LAST_UPDATE_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_LEGAL_AUTHENTICATOR;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_REFERENCE_ID_LIST;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_REPOSITORY_UNIQUE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SERVICE_START_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SERVICE_STOP_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SIZE;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SOURCE_PATIENT_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SOURCE_PATIENT_INFO;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SUBMISSION_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_URI;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_AUTHOR_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_CLASS_NODE;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DOCUMENT_NOT_ALLOWED_IN_DOC_ENTRY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DOC_CODE_NOT_ALLOWED_ON_HAS_MEMBER;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DOC_ENTRY_INVALID_AVAILABILITY_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DOC_ENTRY_PATIENT_ID_WRONG;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.EXACTLY_ONE_SUBMISSION_SET_MUST_EXIST;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.FOLDER_INVALID_AVAILABILITY_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.FOLDER_PATIENT_ID_WRONG;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_ASSOCIATION_TYPE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_SUBMISSION_SET_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_TITLE_ENCODING;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.LOGICAL_ID_EQUALS_ENTRY_UUID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.LOGICAL_ID_MISSING;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.LOGICAL_ID_SAME;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_ASSOCIATION;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_DOCUMENT_FOR_DOC_ENTRY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_FOLDER_NAME;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_HAS_MEMBER_ASSOCIATION;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_NEW_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_ORIGINAL;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_ORIGINAL_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_PREVIOUS_VERSION;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_SNAPSHOT_ASSOCIATION;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_SUBMISSION_SET;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.SOURCE_UUID_NOT_FOUND;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.SUBMISSION_SET_INVALID_AVAILABILITY_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.SUBMISSION_SET_STATUS_MANDATORY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.TITLE_TOO_LONG;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.TOO_MANY_SUBMISSION_SET_STATES;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UNIQUE_ID_MISSING;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UNIQUE_ID_TOO_LONG;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UUID_NOT_UNIQUE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.WRONG_DOCUMENT_ENTRY_TYPE;
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

    private final SlotValueValidation[] authorValidations = new SlotValueValidation[] {
        new SlotValueValidation(SLOT_NAME_AUTHOR_PERSON, xcnValidator, 0, 1),
        new SlotValueValidation(SLOT_NAME_AUTHOR_INSTITUTION, xonValidator, 0, Integer.MAX_VALUE),
        new SlotValueValidation(SLOT_NAME_AUTHOR_ROLE, cxValidatorOptionalAA, 0, Integer.MAX_VALUE),
        new SlotValueValidation(SLOT_NAME_AUTHOR_SPECIALTY, cxValidatorOptionalAA, 0, Integer.MAX_VALUE),
        new SlotValueValidation(SLOT_NAME_AUTHOR_TELECOM, xtnValidator, 0, Integer.MAX_VALUE)};
    
    private final SlotValueValidation[] codingSchemeValidations = new SlotValueValidation[] {
        new SlotValueValidation(SLOT_NAME_CODING_SCHEME, nopValidator)};


    private List<RegistryObjectValidator> documentEntrySlotValidators(ValidationProfile profile, boolean onDemandProvided, boolean limitedMetadata) {
        List<RegistryObjectValidator> validators = new ArrayList<>();
        boolean isContinuaHRN = (profile == CONTINUA_HRN.Interactions.ITI_41);
        DisplayNameUsage requiredOnlyForContinuaHRN = isContinuaHRN ? REQUIRED : OPTIONAL;

        boolean isOnDemand    = (profile == XDS_B.Interactions.ITI_61) ||
                                (profile.isQuery() && onDemandProvided);

        boolean needHashAndSize = (! isOnDemand) &&
                (isContinuaHRN || profile.isQuery()
                        || (profile == XDS_B.Interactions.ITI_42)
                        || (profile == XDM.Interactions.ITI_41)
                );

        Collections.addAll(validators,
            new SlotValueValidation(SLOT_NAME_CREATION_TIME, timeValidator,
                    (limitedMetadata || isOnDemand) ? 0 : 1,
                    isOnDemand ? 0 : 1),
            new SlotValueValidation(SLOT_NAME_SERVICE_START_TIME, timeValidator, 0, 1),
            new SlotValueValidation(SLOT_NAME_SERVICE_STOP_TIME, timeValidator, 0, 1),
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
            new SlotValidation(SLOT_NAME_URI, uriValidator),
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
                    limitedMetadata ? 0 : 1, 1, requiredOnlyForContinuaHRN, codingSchemeValidations));

        if (! limitedMetadata) {
            validators.add(new ExternalIdentifierValidation(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID, cxValidatorRequiredAA));
        }

        if ((profile.getInteractionId() == XDS_B.Interactions.ITI_42) || isOnDemand || profile.isQuery()) {
            validators.add(new SlotValueValidation(SLOT_NAME_REPOSITORY_UNIQUE_ID, oidValidator));
        }
        return validators;
    }

    private List<RegistryObjectValidator> getSubmissionSetSlotValidations(ValidationProfile profile, boolean limitedMetadata) {
        boolean isContinuaHRN = (profile == CONTINUA_HRN.Interactions.ITI_41);
        DisplayNameUsage requiredOnlyForContinuaHRN = isContinuaHRN ? REQUIRED : OPTIONAL;

        List<RegistryObjectValidator> validators = new ArrayList<>();
        Collections.addAll(validators,
                new SlotValidation(SLOT_NAME_INTENDED_RECIPIENT, recipientListValidator),
                new SlotValueValidation(SLOT_NAME_SUBMISSION_TIME, timeValidator),
                new AuthorClassificationValidation(SUBMISSION_SET_AUTHOR_CLASS_SCHEME, authorValidations),
                new ClassificationValidation(SUBMISSION_SET_CONTENT_TYPE_CODE_CLASS_SCHEME,
                        limitedMetadata ? 0 : 1, 1, requiredOnlyForContinuaHRN, codingSchemeValidations),
                new ExternalIdentifierValidation(SUBMISSION_SET_SOURCE_ID_EXTERNAL_ID, oidValidator));
        if (! limitedMetadata) {
            validators.add(new ExternalIdentifierValidation(SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID, cxValidatorRequiredAA));
        }
        return validators;
    }


    private List<RegistryObjectValidator> getFolderSlotValidations(boolean limitedMetadata) {
        List<RegistryObjectValidator> validators = new ArrayList<>();
        Collections.addAll(validators,
                new SlotValueValidation(SLOT_NAME_LAST_UPDATE_TIME, timeValidator, 0, 1),
                new ClassificationValidation(FOLDER_CODE_LIST_CLASS_SCHEME,
                        limitedMetadata ? 0 : 1, Integer.MAX_VALUE, OPTIONAL, codingSchemeValidations));
        if (! limitedMetadata) {
            validators.add(new ExternalIdentifierValidation(FOLDER_PATIENT_ID_EXTERNAL_ID, cxValidatorRequiredAA));
        }
        return validators;
    }

    private boolean checkLimitedMetadata(EbXMLRegistryObject object, ValidationProfile profile) {
        boolean limitedMetadata = ! object.getClassifications(DOC_ENTRY_LIMITED_METADATA_CLASS_SCHEME).isEmpty();
        if (limitedMetadata) {
            metaDataAssert((profile == XDM.Interactions.ITI_41) || (profile == XDR.Interactions.ITI_41),
                    ValidationMessage.LIMITED_METADATA_PROHIBITED, object.getId());
        } else {
            metaDataAssert(profile != XDM.Interactions.ITI_41,
                    ValidationMessage.LIMITED_METADATA_REQUIRED, object.getId());
        }
        return limitedMetadata;
    }


    @Override
    public void validate(EbXMLObjectContainer container, ValidationProfile profile) {
        notNull(container, "container cannot be null");
        notNull(profile, "profile must be set");

        slotLengthAndNameUniquenessValidator.validateContainer(container);
    
        // Note: The order of these checks is important!        
        validateSubmissionSet(container, profile);
        if (!profile.isQuery()) {
            validateUniquenessOfUUIDs(container);
            validateUniqueIds(container);
        }
        validateAssociations(container, profile);
        validateDocumentEntries(container, profile);
        validateFolders(container, profile);
        if (!profile.isQuery()) {
            validatePatientIdsAreIdentical(container);
        }
    }

    private void validateFolders(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        Set<String> logicalIds = new HashSet<>();
        for (EbXMLRegistryPackage folder : container.getRegistryPackages(FOLDER_CLASS_NODE)) {
            boolean limitedMetadata = checkLimitedMetadata(folder, profile);
            runValidations(folder, getFolderSlotValidations(limitedMetadata));

            AvailabilityStatus status = folder.getStatus();
            if (profile.isQuery() || status != null) {
                metaDataAssert(status == AvailabilityStatus.APPROVED,
                        FOLDER_INVALID_AVAILABILITY_STATUS, status);
            }

            metaDataAssert(StringUtils.isBlank(folder.getLid()) || logicalIds.add(folder.getLid()),
                    LOGICAL_ID_SAME, folder.getLid());

            LocalizedString name = folder.getName();
            metaDataAssert(limitedMetadata || ((name != null) && (name.getValue() != null)),
                    MISSING_FOLDER_NAME, folder.getId());

            if (profile == XDS_B.Interactions.ITI_57){
                validateUpdateObject(folder, container);
            }
        }
    }

    private void validateSubmissionSet(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        List<EbXMLRegistryPackage> submissionSets = container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE);
        if (!profile.isQuery()) {
            metaDataAssert(submissionSets.size() == 1, EXACTLY_ONE_SUBMISSION_SET_MUST_EXIST);
        }
    
        for (EbXMLRegistryPackage submissionSet : submissionSets) {
            boolean limitedMetadata = checkLimitedMetadata(submissionSet, profile);
            runValidations(submissionSet, getSubmissionSetSlotValidations(profile, limitedMetadata));

            AvailabilityStatus status = submissionSet.getStatus();
            if (profile.isQuery() || (status != null)) {
                metaDataAssert(status == AvailabilityStatus.APPROVED,
                        SUBMISSION_SET_INVALID_AVAILABILITY_STATUS, status);
            }
        }
    }

    private void validateDocumentEntries(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        Set<String> logicalIds = new HashSet<>();
        for (EbXMLExtrinsicObject docEntry : container.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND)) {
            boolean limitedMetadata = checkLimitedMetadata(docEntry, profile);

            boolean onDemandExpected = (profile == XDS_B.Interactions.ITI_61);
            boolean onDemandProvided = DocumentEntryType.ON_DEMAND.getUuid().equals(docEntry.getObjectType());
            metaDataAssert(profile.isQuery() || (onDemandExpected == onDemandProvided),
                    WRONG_DOCUMENT_ENTRY_TYPE, docEntry.getObjectType());

            runValidations(docEntry, documentEntrySlotValidators(profile, onDemandProvided, limitedMetadata));

            if (profile.isQuery()) {
                AvailabilityStatus status = docEntry.getStatus();
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

            boolean attachmentExpected = (profile.getInteractionId() == XCF.Interactions.ITI_63);
            boolean attachmentProvided = (docEntry.getDataHandler() != null);
            metaDataAssert(attachmentProvided == attachmentExpected,
                    attachmentExpected ? MISSING_DOCUMENT_FOR_DOC_ENTRY : DOCUMENT_NOT_ALLOWED_IN_DOC_ENTRY,
                    docEntry.getId());

            metaDataAssert(profile.isQuery() || StringUtils.isBlank(docEntry.getLid()) || logicalIds.add(docEntry.getLid()),
                    LOGICAL_ID_SAME, docEntry.getLid());

            if (profile.getInteractionId() == XDS_B.Interactions.ITI_57){
                validateUpdateObject(docEntry, container);
            }
        }
    }

    private void runValidations(EbXMLRegistryObject obj, List<RegistryObjectValidator> validations) throws XDSMetaDataException {
        for (RegistryObjectValidator validation : validations) {
            validation.validate(obj);
        }
    }

    private void validateUniqueIds(EbXMLObjectContainer container) throws XDSMetaDataException {
        validateUniqueIds(container.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND), DOC_ENTRY_UNIQUE_ID_EXTERNAL_ID);
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
        Set<String> uuids = new HashSet<>();
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
    
        for (EbXMLExtrinsicObject docEntry : container.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND)) {
            String patientIdDocEntry = docEntry.getExternalIdentifierValue(DOC_ENTRY_PATIENT_ID_EXTERNAL_ID);
            metaDataAssert(patientId.equals(patientIdDocEntry), DOC_ENTRY_PATIENT_ID_WRONG);
        }
        
        for (EbXMLRegistryPackage folder : container.getRegistryPackages(FOLDER_CLASS_NODE)) {
            String patientIdFolder = folder.getExternalIdentifierValue(FOLDER_PATIENT_ID_EXTERNAL_ID);
            metaDataAssert(patientId.equals(patientIdFolder), FOLDER_PATIENT_ID_WRONG);
        }
    }

    private void validateAssociations(EbXMLObjectContainer container, ValidationProfile profile) throws XDSMetaDataException {
        Set<String> logicalIds = new HashSet<>();
        Set<String> docEntryIds = container.getExtrinsicObjects(DocumentEntryType.STABLE_OR_ON_DEMAND).stream()
                .filter(docEntry -> docEntry.getId() != null)
                .map(EbXMLRegistryObject::getId)
                .collect(Collectors.toSet());
        Set<String> submissionSetIds = container.getRegistryPackages(SUBMISSION_SET_CLASS_NODE).stream()
                .map(EbXMLRegistryObject::getId)
                .collect(Collectors.toSet());
        Set<String> associationIds = new HashSet<>();
        boolean hasSubmitAssociationType = false;
        for (EbXMLAssociation association : container.getAssociations()) {
            associationIds.add(association.getId());

            AssociationType type = association.getAssociationType();
            metaDataAssert(type != null, INVALID_ASSOCIATION_TYPE);
            hasSubmitAssociationType = hasSubmitAssociationType
                    || (type == AssociationType.SUBMIT_ASSOCIATION)
                    || (type == AssociationType.UPDATE_AVAILABILITY_STATUS);

            metaDataAssert(StringUtils.isBlank(association.getLid()) || logicalIds.add(association.getLid()),
                    LOGICAL_ID_SAME, association.getLid());
        }

        for (EbXMLAssociation association : container.getAssociations()) {
            switch (association.getAssociationType()) {
                case HAS_MEMBER:
                    boolean isSubmissionSetToDocEntry =
                            submissionSetIds.contains(association.getSource()) && docEntryIds.contains(association.getTarget());
                    validateAssociation(association, docEntryIds, profile, isSubmissionSetToDocEntry);
                    break;

                case IS_SNAPSHOT_OF:
                    if (!profile.isQuery()) {
                        EbXMLExtrinsicObject sourceDocEntry = getExtrinsicObject(
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

        List<String> slotValues = association.getSlotValues(SLOT_NAME_SUBMISSION_SET_STATUS);
        if (isSubmissionSetToDocEntry){
            metaDataAssert(!slotValues.isEmpty(), SUBMISSION_SET_STATUS_MANDATORY);
        }
        if (!slotValues.isEmpty()) {
            metaDataAssert(slotValues.size() == 1, TOO_MANY_SUBMISSION_SET_STATES);

            AssociationLabel status = AssociationLabel.fromOpcode(slotValues.get(0));
            metaDataAssert(status != null, INVALID_SUBMISSION_SET_STATUS);

            if (status == AssociationLabel.ORIGINAL && !profile.isQuery()) {
                metaDataAssert(docEntryIds.contains(association.getTarget()), MISSING_ORIGINAL);
            }
        }
    }

    private EbXMLExtrinsicObject getExtrinsicObject(EbXMLObjectContainer container, String docEntryId, String... objectTypes) {
        for (EbXMLExtrinsicObject docEntry : container.getExtrinsicObjects(objectTypes)) {
            if (docEntry.getId() != null && docEntry.getId().equals(docEntryId)) {
                return docEntry;
            }
        }
        return null;
    }

    private EbXMLRegistryPackage getRegistryPackage(EbXMLObjectContainer container, String submissionSetId, String classificationNode) {
        for (EbXMLRegistryPackage registryPackage : container.getRegistryPackages(classificationNode)) {
            if (registryPackage.getId() != null && registryPackage.getId().equals(submissionSetId)) {
                return registryPackage;
            }
        }
        return null;
    }

    private void validateUpdateObject(EbXMLRegistryObject registryObject, EbXMLObjectContainer container) {
        metaDataAssert(registryObject.getLid() != null, LOGICAL_ID_MISSING);
        uuidValidator.validate(registryObject.getLid());
        metaDataAssert(!registryObject.getLid().equals(registryObject.getId()), LOGICAL_ID_EQUALS_ENTRY_UUID,
                registryObject.getLid(), registryObject.getId());

        boolean foundHasMemberAssociation = false;
        for (EbXMLAssociation association : container.getAssociations()) {
            if (association.getAssociationType() == AssociationType.HAS_MEMBER
                && association.getTarget().equals(registryObject.getId())
                && (getRegistryPackage(container, association.getSource(), SUBMISSION_SET_CLASS_NODE) != null))
            {
                metaDataAssert(association.getPreviousVersion() != null, MISSING_PREVIOUS_VERSION);
                foundHasMemberAssociation = true;
            }
        }
        metaDataAssert(foundHasMemberAssociation, MISSING_HAS_MEMBER_ASSOCIATION, registryObject.getId());
    }
}