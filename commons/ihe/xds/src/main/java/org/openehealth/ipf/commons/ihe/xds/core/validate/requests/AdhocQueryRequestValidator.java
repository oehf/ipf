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
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.CXValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.NopValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.SlotLengthAndNameUniquenessValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.TimeValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.AssociationValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.ChoiceValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.CodeValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.DocumentEntryTypeValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.HomeCommunityIdValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.NumberValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.QueryListCodeValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.QueryParameterValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.StatusValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.StringListValidation;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.StringValidation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.XCA.Interactions.ITI_38;
import static org.openehealth.ipf.commons.ihe.xds.XCF.Interactions.ITI_63;
import static org.openehealth.ipf.commons.ihe.xds.XDS_A.Interactions.ITI_16;
import static org.openehealth.ipf.commons.ihe.xds.XDS_B.Interactions.ITI_18;
import static org.openehealth.ipf.commons.ihe.xds.XDS_B.Interactions.ITI_51;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FETCH;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_DOCUMENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_DOCUMENTS_BY_REFERENCE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_DOCUMENTS_MPQ;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_FOLDERS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_FOLDERS_MPQ;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.FIND_SUBMISSION_SETS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_ALL;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_ASSOCIATIONS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_DOCUMENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_DOCUMENTS_AND_ASSOCIATIONS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_FOLDERS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_FOLDERS_FOR_DOCUMENT;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_FOLDER_AND_CONTENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_RELATED_DOCUMENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_SUBMISSION_SETS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.GET_SUBMISSION_SET_AND_CONTENTS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.SQL;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.ASSOCIATION_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.ASSOCIATION_TYPE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_AUTHOR_PERSON;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_CLASS_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_CREATION_TIME_FROM;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_CREATION_TIME_TO;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_DOCUMENT_AVAILABILITY;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_EVENT_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_EVENT_CODE_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_FORMAT_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_FORMAT_CODE_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_LOGICAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_PATIENT_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_PRACTICE_SETTING_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_REFERENCE_IDS;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_SERVICE_START_TIME_FROM;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_SERVICE_START_TIME_TO;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_SERVICE_STOP_TIME_FROM;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_SERVICE_STOP_TIME_TO;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_TYPE_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_UNIQUE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_UUID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_CODES;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_CODES_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_LAST_UPDATE_TIME_FROM;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_LAST_UPDATE_TIME_TO;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_LOGICAL_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_PATIENT_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_UNIQUE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.FOLDER_UUID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.METADATA_LEVEL;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.PATIENT_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_AUTHOR_PERSON;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_CONTENT_TYPE_CODE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_PATIENT_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_SOURCE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_SUBMISSION_TIME_FROM;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_SUBMISSION_TIME_TO;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_UNIQUE_ID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.SUBMISSION_SET_UUID;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.UUID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_SQL_QUERY_TEXT;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UNKNOWN_QUERY_TYPE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UNKNOWN_RETURN_TYPE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UNSUPPORTED_QUERY_TYPE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates an {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class AdhocQueryRequestValidator implements Validator<EbXMLAdhocQueryRequest, ValidationProfile> {
    private static final CXValidator cxValidator = new CXValidator(true);
    private static final TimeValidator timeValidator = new TimeValidator();
    private static final NopValidator nopValidator = new NopValidator();


    private static void addAllowedMultipleSlots(QueryType queryType, QueryParameter... parameters) {
        Set<String> slotNames = new HashSet<>();
        for (QueryParameter parameter : parameters) {
            slotNames.add(parameter.getSlotName());
        }
        ALLOWED_MULTIPLE_SLOTS.put(queryType, slotNames);
    }


    private static final Map<QueryType, Set<String>> ALLOWED_MULTIPLE_SLOTS;
    static {
        ALLOWED_MULTIPLE_SLOTS = new HashMap<>();

        addAllowedMultipleSlots(FIND_DOCUMENTS,
                DOC_ENTRY_CLASS_CODE,
                DOC_ENTRY_TYPE_CODE,
                DOC_ENTRY_PRACTICE_SETTING_CODE,
                DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE,
                DOC_ENTRY_EVENT_CODE,
                DOC_ENTRY_CONFIDENTIALITY_CODE,
                DOC_ENTRY_AUTHOR_PERSON,
                DOC_ENTRY_FORMAT_CODE,
                DOC_ENTRY_STATUS,
                DOC_ENTRY_DOCUMENT_AVAILABILITY,
                METADATA_LEVEL);

        addAllowedMultipleSlots(FIND_DOCUMENTS_BY_REFERENCE_ID,
                DOC_ENTRY_CLASS_CODE,
                DOC_ENTRY_TYPE_CODE,
                DOC_ENTRY_PRACTICE_SETTING_CODE,
                DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE,
                DOC_ENTRY_EVENT_CODE,
                DOC_ENTRY_CONFIDENTIALITY_CODE,
                DOC_ENTRY_AUTHOR_PERSON,
                DOC_ENTRY_FORMAT_CODE,
                DOC_ENTRY_STATUS,
                DOC_ENTRY_REFERENCE_IDS);

        addAllowedMultipleSlots(FIND_DOCUMENTS_MPQ,
                DOC_ENTRY_PATIENT_ID,
                DOC_ENTRY_CLASS_CODE,
                DOC_ENTRY_TYPE_CODE,
                DOC_ENTRY_PRACTICE_SETTING_CODE,
                DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE,
                DOC_ENTRY_EVENT_CODE,
                DOC_ENTRY_CONFIDENTIALITY_CODE,
                DOC_ENTRY_AUTHOR_PERSON,
                DOC_ENTRY_FORMAT_CODE,
                DOC_ENTRY_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(FIND_SUBMISSION_SETS,
                SUBMISSION_SET_SOURCE_ID,
                SUBMISSION_SET_CONTENT_TYPE_CODE,
                SUBMISSION_SET_STATUS);

        addAllowedMultipleSlots(FIND_FOLDERS,
                FOLDER_CODES,
                FOLDER_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(FIND_FOLDERS_MPQ,
                FOLDER_PATIENT_ID,
                FOLDER_CODES,
                FOLDER_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_ALL,
                DOC_ENTRY_STATUS,
                SUBMISSION_SET_STATUS,
                FOLDER_STATUS,
                DOC_ENTRY_FORMAT_CODE,
                DOC_ENTRY_CONFIDENTIALITY_CODE,
                ASSOCIATION_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_DOCUMENTS,
                DOC_ENTRY_UUID,
                DOC_ENTRY_UNIQUE_ID,
                DOC_ENTRY_LOGICAL_ID,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_FOLDERS,
                FOLDER_UUID,
                FOLDER_UNIQUE_ID,
                FOLDER_LOGICAL_ID,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_ASSOCIATIONS,
                UUID,
                ASSOCIATION_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_DOCUMENTS_AND_ASSOCIATIONS,
                DOC_ENTRY_UUID,
                DOC_ENTRY_UNIQUE_ID,
                ASSOCIATION_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_SUBMISSION_SETS,
                UUID,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_SUBMISSION_SET_AND_CONTENTS,
                DOC_ENTRY_FORMAT_CODE,
                DOC_ENTRY_CONFIDENTIALITY_CODE,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_FOLDER_AND_CONTENTS,
                DOC_ENTRY_FORMAT_CODE,
                DOC_ENTRY_CONFIDENTIALITY_CODE,
                ASSOCIATION_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_FOLDERS_FOR_DOCUMENT,
                ASSOCIATION_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(GET_RELATED_DOCUMENTS,
                ASSOCIATION_TYPE,
                ASSOCIATION_STATUS,
                METADATA_LEVEL);

        addAllowedMultipleSlots(FETCH,
                DOC_ENTRY_CLASS_CODE,
                DOC_ENTRY_TYPE_CODE,
                DOC_ENTRY_PRACTICE_SETTING_CODE,
                DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE,
                DOC_ENTRY_EVENT_CODE,
                DOC_ENTRY_CONFIDENTIALITY_CODE,
                DOC_ENTRY_AUTHOR_PERSON,
                DOC_ENTRY_FORMAT_CODE);
    }


    private static final Map<List<InteractionId>, List<QueryType>> ALLOWED_QUERY_TYPES;
    static {
        ALLOWED_QUERY_TYPES = new HashMap<>(3);
        ALLOWED_QUERY_TYPES.put(
                Collections.singletonList(ITI_16),
                Collections.singletonList(SQL));
        ALLOWED_QUERY_TYPES.put(
                Arrays.asList(ITI_18, ITI_38),
                Arrays.asList(
                        FIND_DOCUMENTS,
                        FIND_DOCUMENTS_BY_REFERENCE_ID,
                        FIND_SUBMISSION_SETS,
                        FIND_FOLDERS,
                        GET_ALL,
                        GET_DOCUMENTS,
                        GET_FOLDERS,
                        GET_ASSOCIATIONS,
                        GET_DOCUMENTS_AND_ASSOCIATIONS,
                        GET_SUBMISSION_SETS,
                        GET_SUBMISSION_SET_AND_CONTENTS,
                        GET_FOLDER_AND_CONTENTS,
                        GET_FOLDERS_FOR_DOCUMENT,
                        GET_RELATED_DOCUMENTS
                ));
        ALLOWED_QUERY_TYPES.put(
                Collections.singletonList(ITI_51),
                Arrays.asList(
                        FIND_DOCUMENTS_MPQ,
                        FIND_FOLDERS_MPQ
                ));
        ALLOWED_QUERY_TYPES.put(
                Collections.singletonList(ITI_63),
                Collections.singletonList(FETCH));
    }


    private QueryParameterValidation[] getValidators(QueryType queryType, ValidationProfile profile) {
        boolean requireHomeCommunityId = profile.getInteractionProfile().requiresHomeCommunityId();

        switch (queryType) {
            case FETCH:
                return new QueryParameterValidation[] {
                    new StringValidation(DOC_ENTRY_PATIENT_ID, cxValidator, false),
                    new CodeValidation(DOC_ENTRY_CLASS_CODE, false),
                    new CodeValidation(DOC_ENTRY_TYPE_CODE),
                    new CodeValidation(DOC_ENTRY_PRACTICE_SETTING_CODE),
                    new CodeValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE),
                    new CodeValidation(DOC_ENTRY_FORMAT_CODE),
                    new NumberValidation(DOC_ENTRY_CREATION_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_CREATION_TIME_TO, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_START_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_START_TIME_TO, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_STOP_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_STOP_TIME_TO, timeValidator),
                    new QueryListCodeValidation(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME),
                    new QueryListCodeValidation(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME),
                    new StringListValidation(DOC_ENTRY_AUTHOR_PERSON, nopValidator),
                    new HomeCommunityIdValidation(true),
                };

            case FIND_DOCUMENTS:
            case FIND_DOCUMENTS_MPQ:
                return new QueryParameterValidation[] {
                    // PatientId MUST BE supplied in  single patient query.
                    // PatientId (list) MAY BE supplied in multi patient query.
                    // The validators for the two cases are otherwise identical.
                    queryType.equals(FIND_DOCUMENTS)
                            ? new StringValidation(DOC_ENTRY_PATIENT_ID, cxValidator, false)
                            : new StringListValidation(DOC_ENTRY_PATIENT_ID, cxValidator),
                    new CodeValidation(DOC_ENTRY_CLASS_CODE),
                    new CodeValidation(DOC_ENTRY_TYPE_CODE),
                    new CodeValidation(DOC_ENTRY_PRACTICE_SETTING_CODE),
                    new CodeValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE),
                    new CodeValidation(DOC_ENTRY_FORMAT_CODE),
                    new NumberValidation(DOC_ENTRY_CREATION_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_CREATION_TIME_TO, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_START_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_START_TIME_TO, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_STOP_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_STOP_TIME_TO, timeValidator),
                    new QueryListCodeValidation(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME),
                    new QueryListCodeValidation(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME),
                    new StringListValidation(DOC_ENTRY_AUTHOR_PERSON, nopValidator),
                    new StatusValidation(DOC_ENTRY_STATUS),
                    new DocumentEntryTypeValidation(),
                };

            case FIND_DOCUMENTS_BY_REFERENCE_ID:
                return new QueryParameterValidation[] {
                    new StringValidation(DOC_ENTRY_PATIENT_ID, cxValidator, false),
                    new CodeValidation(DOC_ENTRY_CLASS_CODE),
                    new CodeValidation(DOC_ENTRY_TYPE_CODE),
                    new CodeValidation(DOC_ENTRY_PRACTICE_SETTING_CODE),
                    new CodeValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE),
                    new CodeValidation(DOC_ENTRY_FORMAT_CODE),
                    new NumberValidation(DOC_ENTRY_CREATION_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_CREATION_TIME_TO, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_START_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_START_TIME_TO, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_STOP_TIME_FROM, timeValidator),
                    new NumberValidation(DOC_ENTRY_SERVICE_STOP_TIME_TO, timeValidator),
                    new QueryListCodeValidation(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME),
                    new QueryListCodeValidation(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME),
                    new StringListValidation(DOC_ENTRY_AUTHOR_PERSON, nopValidator),
                    new StatusValidation(DOC_ENTRY_STATUS),
                    new DocumentEntryTypeValidation(),
                    new StringListValidation(DOC_ENTRY_REFERENCE_IDS, nopValidator),
                };

            case FIND_SUBMISSION_SETS:
                return new QueryParameterValidation[] {
                    new StringValidation(SUBMISSION_SET_PATIENT_ID, cxValidator, false),
                    // Excluded to avoid validation errors for xdstest requests
                    // new StringListValidation(SUBMISSION_SET_SOURCE_ID, oidValidator),
                    new NumberValidation(SUBMISSION_SET_SUBMISSION_TIME_FROM, timeValidator),
                    new NumberValidation(SUBMISSION_SET_SUBMISSION_TIME_TO, timeValidator),
                    new StringValidation(SUBMISSION_SET_AUTHOR_PERSON, nopValidator, true),
                    new CodeValidation(SUBMISSION_SET_CONTENT_TYPE_CODE),
                    new StatusValidation(SUBMISSION_SET_STATUS),
                };

            case FIND_FOLDERS:
            case FIND_FOLDERS_MPQ:
                return new QueryParameterValidation[] {
                    // PatientId MUST BE supplied in  single patient query.
                    // PatientId (list) MAY BE supplied in multi patient query.
                    // The validators for the two cases are otherwise identical.
                    queryType.equals(FIND_FOLDERS) ? new StringValidation(FOLDER_PATIENT_ID, cxValidator, false) : new StringListValidation(FOLDER_PATIENT_ID, cxValidator),
                    new NumberValidation(FOLDER_LAST_UPDATE_TIME_FROM, timeValidator),
                    new NumberValidation(FOLDER_LAST_UPDATE_TIME_TO, timeValidator),
                    new QueryListCodeValidation(FOLDER_CODES, FOLDER_CODES_SCHEME),
                    new StatusValidation(FOLDER_STATUS),
                };

            case GET_ALL:
                return new QueryParameterValidation[] {
                    new StringValidation(PATIENT_ID, cxValidator, false),
                    new StatusValidation(DOC_ENTRY_STATUS),
                    new StatusValidation(SUBMISSION_SET_STATUS),
                    new StatusValidation(FOLDER_STATUS),
                    new QueryListCodeValidation(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_SCHEME),
                    new DocumentEntryTypeValidation(),
                };

            case GET_DOCUMENTS:
                return new QueryParameterValidation[] {
                    new HomeCommunityIdValidation(requireHomeCommunityId),
                    new ChoiceValidation(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID, DOC_ENTRY_LOGICAL_ID),
                    new StringListValidation(DOC_ENTRY_UUID, nopValidator),
                    new StringListValidation(DOC_ENTRY_UNIQUE_ID, nopValidator),
                };

            case GET_DOCUMENTS_AND_ASSOCIATIONS:
                return new QueryParameterValidation[] {
                    new HomeCommunityIdValidation(requireHomeCommunityId),
                    new ChoiceValidation(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID),
                    new StringListValidation(DOC_ENTRY_UUID, nopValidator),
                    new StringListValidation(DOC_ENTRY_UNIQUE_ID, nopValidator),
                };

            case GET_FOLDERS_FOR_DOCUMENT:
                return new QueryParameterValidation[] {
                    new HomeCommunityIdValidation(requireHomeCommunityId),
                    new ChoiceValidation(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID),
                    new StringValidation(DOC_ENTRY_UUID, nopValidator, true),
                    new StringValidation(DOC_ENTRY_UNIQUE_ID, nopValidator, true),
                };

            case GET_FOLDERS:
                return new QueryParameterValidation[] {
                    new HomeCommunityIdValidation(requireHomeCommunityId),
                    new ChoiceValidation(FOLDER_UUID, FOLDER_UNIQUE_ID, FOLDER_LOGICAL_ID),
                    new StringListValidation(FOLDER_UUID, nopValidator),
                    new StringListValidation(FOLDER_UNIQUE_ID, nopValidator),
                };

            case GET_ASSOCIATIONS:
            case GET_SUBMISSION_SETS:
                return new QueryParameterValidation[] {
                    new HomeCommunityIdValidation(requireHomeCommunityId),
                    new StringListValidation(UUID, nopValidator),
                };

            case GET_SUBMISSION_SET_AND_CONTENTS:
                return new QueryParameterValidation[] {
                    new HomeCommunityIdValidation(requireHomeCommunityId),
                    new ChoiceValidation(SUBMISSION_SET_UUID, SUBMISSION_SET_UNIQUE_ID),
                    new StringValidation(SUBMISSION_SET_UUID, nopValidator, true),
                    new StringValidation(SUBMISSION_SET_UNIQUE_ID, nopValidator, true),
                    new QueryListCodeValidation(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME),
                    new QueryListCodeValidation(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_SCHEME),
                    new DocumentEntryTypeValidation(),
                };

            case GET_FOLDER_AND_CONTENTS:
                return new QueryParameterValidation[] {
                    new HomeCommunityIdValidation(requireHomeCommunityId),
                    new ChoiceValidation(FOLDER_UUID, FOLDER_UNIQUE_ID),
                    new StringValidation(FOLDER_UUID, nopValidator, true),
                    new StringValidation(FOLDER_UNIQUE_ID, nopValidator, true),
                    new QueryListCodeValidation(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME),
                    new QueryListCodeValidation(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_SCHEME),
                    new DocumentEntryTypeValidation(),
                };

            case GET_RELATED_DOCUMENTS:
                return new QueryParameterValidation[] {
                    new HomeCommunityIdValidation(requireHomeCommunityId),
                    new ChoiceValidation(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID),
                    new StringValidation(DOC_ENTRY_UUID, nopValidator, true),
                    new StringValidation(DOC_ENTRY_UNIQUE_ID, nopValidator, true),
                    new AssociationValidation(ASSOCIATION_TYPE),
                    new DocumentEntryTypeValidation(),
                };
        }

        return null;    // should not occur
    }

    @Override
    public void validate(EbXMLAdhocQueryRequest request, ValidationProfile profile) {
        notNull(request, "request cannot be null");

        if (profile == ITI_63) {
            metaDataAssert(QueryReturnType.LEAF_CLASS_WITH_REPOSITORY_ITEM.getCode().equals(request.getReturnType()),
                    UNKNOWN_RETURN_TYPE, request.getReturnType());
        } else {
            metaDataAssert(QueryReturnType.LEAF_CLASS.getCode().equals(request.getReturnType())
                        || QueryReturnType.OBJECT_REF.getCode().equals(request.getReturnType()),
                UNKNOWN_RETURN_TYPE, request.getReturnType());
        }

        QueryType queryType = QueryType.valueOfId(request.getId());
        metaDataAssert(queryType != null, UNKNOWN_QUERY_TYPE, request.getId());

        boolean found = false;
        for(Map.Entry<List<InteractionId>, List<QueryType>> entry : ALLOWED_QUERY_TYPES.entrySet()) {
            if (entry.getKey().contains(profile.getInteractionId())) {
                metaDataAssert(entry.getValue().contains(queryType), UNSUPPORTED_QUERY_TYPE, queryType);
                found = true;
                break;
            }
        }
        metaDataAssert(found, UNKNOWN_QUERY_TYPE, queryType);

        if (queryType == QueryType.SQL) {
            metaDataAssert(request.getSql() != null, MISSING_SQL_QUERY_TEXT);
        } else {
            new SlotLengthAndNameUniquenessValidator().validateQuerySlots(
                    request.getSlots(),
                    ALLOWED_MULTIPLE_SLOTS.get(queryType));
            QueryParameterValidation[] validations = getValidators(queryType, profile);
            if (validations != null) {
                for (QueryParameterValidation validation : validations) {
                    validation.validate(request);
                }
            }

            if (queryType == FIND_DOCUMENTS_MPQ) {
                metaDataAssert(
                        (! request.getSlotValues(DOC_ENTRY_CLASS_CODE.getSlotName()).isEmpty()) ||
                        (! request.getSlotValues(DOC_ENTRY_EVENT_CODE.getSlotName()).isEmpty()) ||
                        (! request.getSlotValues(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE.getSlotName()).isEmpty()),
                        ValidationMessage.MISSING_REQUIRED_QUERY_PARAMETER,
                        "at least one of $XDSDocumentEntryClassCode, $XDSDocumentEntryEventCodeList, $XDSDocumentEntryHealthcareFacilityTypeCode");
            }
        }
    }
}
