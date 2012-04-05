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
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.validate.*;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.*;

import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.*;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.*;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates an {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class AdhocQueryRequestValidator implements Validator<EbXMLAdhocQueryRequest, ValidationProfile> {
    private static final CXValidator cxValidator = new CXValidator();
    private static final TimeValidator timeValidator = new TimeValidator();
    private static final NopValidator nopValidator = new NopValidator();


    private static final Map<List<InteractionId>, List<QueryType>> ALLOWED_QUERY_TYPES;
    static {
        ALLOWED_QUERY_TYPES = new HashMap<List<InteractionId>, List<QueryType>>(3);
        ALLOWED_QUERY_TYPES.put(
                Collections.<InteractionId> singletonList(ITI_16),
                Collections.singletonList(SQL));
        ALLOWED_QUERY_TYPES.put(
                Arrays.<InteractionId> asList(ITI_18, ITI_38),
                Arrays.asList(
                        FIND_DOCUMENTS,
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
                Collections.<InteractionId> singletonList(ITI_63),
                Collections.singletonList(FETCH));
    }


    private QueryParameterValidation[] getValidators(QueryType queryType, ValidationProfile profile) {
        boolean requireHomeCommunityId =
                (profile.getProfile() == ValidationProfile.InteractionProfile.XCA) ||
                (profile.getProfile() == ValidationProfile.InteractionProfile.XCF);

        switch (queryType) {
            case FETCH:
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
                    new HomeCommunityIdValidation(true),
                };

            case FIND_DOCUMENTS:
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
                return new QueryParameterValidation[] {
                    new StringValidation(FOLDER_PATIENT_ID, cxValidator, false),
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
                    new ChoiceValidation(FOLDER_UUID, FOLDER_UNIQUE_ID),
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

        if (profile.getInteractionId() == IpfInteractionId.ITI_63) {
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
            for (QueryParameterValidation validation : getValidators(queryType, profile)) {
                validation.validate(request);
            }
        }
    }
}
