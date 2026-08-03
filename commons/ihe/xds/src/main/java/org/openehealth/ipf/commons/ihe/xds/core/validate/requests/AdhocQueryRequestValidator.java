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

import lombok.Getter;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.xds.XdsIntegrationProfile.HomeCommunityIdOptionality;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.CMPD.Interactions.PHARM_1;
import static org.openehealth.ipf.commons.ihe.xds.XCA.Interactions.ITI_38;
import static org.openehealth.ipf.commons.ihe.xds.XCF.Interactions.ITI_63;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_18;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_51;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType.LEAF_CLASS;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType.LEAF_CLASS_WITH_REPOSITORY_ITEM;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryReturnType.OBJECT_REF;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType.*;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.requests.QueryRules.rules;

/**
 * Validates an {@link EbXMLAdhocQueryRequest}.
 * <p>
 * Everything that is specific to a single {@link QueryType} is declared in one place, namely in the
 * {@link #rulesFor} switch, expressed in the vocabulary of {@link QueryRules}. The switch is
 * deliberately exhaustive and has no {@code default} branch, so a newly added {@code QueryType}
 * fails to compile until its rules are declared.
 *
 * @author Jens Riemschneider
 */
public class AdhocQueryRequestValidator implements Validator<EbXMLAdhocQueryRequest<AdhocQueryRequest>, ValidationProfile> {

    @Getter
    private static final AdhocQueryRequestValidator instance = new AdhocQueryRequestValidator();


    // ------------------------------------------------------------------------
    // Which query types may be used in which transaction
    // ------------------------------------------------------------------------

    private static final Map<InteractionId, Set<QueryType>> ALLOWED_QUERY_TYPES;

    static {
        Set<QueryType> itiStoredQueryTypes = EnumSet.of(
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
            GET_RELATED_DOCUMENTS);

        Set<QueryType> pharmStoredQueryTypes = EnumSet.of(
            FIND_MEDICATION_TREATMENT_PLANS,
            FIND_PRESCRIPTIONS,
            FIND_DISPENSES,
            FIND_MEDICATION_ADMINISTRATIONS,
            FIND_PRESCRIPTIONS_FOR_VALIDATION,
            FIND_PRESCRIPTIONS_FOR_DISPENSE,
            FIND_MEDICATION_LIST);

        // The FindDocumentsExclude Option (CP-ITI-1323) is defined for XDS.b only, hence
        // FIND_DOCUMENTS_EXCLUDE is accepted for ITI-18 but not for ITI-38.
        Set<QueryType> iti18StoredQueryTypes = EnumSet.copyOf(itiStoredQueryTypes);
        iti18StoredQueryTypes.add(FIND_DOCUMENTS_EXCLUDE);

        ALLOWED_QUERY_TYPES = new HashMap<>(5);
        ALLOWED_QUERY_TYPES.put(ITI_18, iti18StoredQueryTypes);
        ALLOWED_QUERY_TYPES.put(ITI_38, itiStoredQueryTypes);
        ALLOWED_QUERY_TYPES.put(ITI_51, EnumSet.of(FIND_DOCUMENTS_MPQ, FIND_FOLDERS_MPQ, FIND_DOCUMENTS_BY_REFERENCE_ID_MPQ));
        ALLOWED_QUERY_TYPES.put(ITI_63, EnumSet.of(FETCH));
        ALLOWED_QUERY_TYPES.put(PHARM_1, pharmStoredQueryTypes);
    }

    /**
     * @return all query types that are allowed in at least one transaction.
     * Package-private, intended for consistency checks in tests.
     */
    static Set<QueryType> queryTypesAllowedInAnyTransaction() {
        return ALLOWED_QUERY_TYPES.values().stream()
            .flatMap(Set::stream)
            .collect(Collectors.toUnmodifiableSet());
    }


    // ------------------------------------------------------------------------
    // Rules per query type
    // ------------------------------------------------------------------------

    private final Map<String, QueryRules> rulesCache = new ConcurrentHashMap<>();

    private AdhocQueryRequestValidator() {
    }

    private QueryRules getRules(QueryType queryType, ValidationProfile profile) {
        var optionality = profile.getInteractionProfile().getHomeCommunityIdOptionality();
        return rulesCache.computeIfAbsent(queryType.name() + optionality.name(),
            key -> rulesFor(queryType, optionality));
    }

    private static QueryRules rulesFor(QueryType queryType, HomeCommunityIdOptionality optionality) {
        return switch (queryType) {

            case FETCH -> rules()
                .patientId(DOC_ENTRY_PATIENT_ID)
                .documentMetadata(true)
                .homeCommunityId(optionality)
                .allowingMultipleSlots(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE);

            case FIND_DOCUMENTS -> findDocumentsRules(true, optionality);

            // PatientId MUST BE supplied in the single patient query and MAY BE supplied in the
            // multi patient query, which in turn demands at least one of several parameters.
            case FIND_DOCUMENTS_MPQ -> findDocumentsRules(false, optionality)
                .requiringAtLeastOneOf(
                    DOC_ENTRY_PATIENT_ID,
                    DOC_ENTRY_CLASS_CODE,
                    DOC_ENTRY_EVENT_CODE,
                    DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE);

            case FIND_DOCUMENTS_BY_REFERENCE_ID -> findDocumentsByReferenceIdRules(true, optionality);
            case FIND_DOCUMENTS_BY_REFERENCE_ID_MPQ -> findDocumentsByReferenceIdRules(false, optionality);

            case FIND_DOCUMENTS_BY_TITLE -> rules()
                .patientId(DOC_ENTRY_PATIENT_ID)
                .documentMetadata(false)
                .stringList(DOC_ENTRY_TITLE)
                .documentStatusAndType()
                .homeCommunityId(optionality);

            case FIND_DOCUMENTS_EXCLUDE -> rules()
                .mutuallyExclusiveExcludedParameters()
                .patientId(DOC_ENTRY_PATIENT_ID)
                .documentMetadata(false)
                .documentStatusAndType()
                .stringList(DOC_ENTRY_REFERENCE_IDS)
                .excludedParameters()
                .homeCommunityId(optionality)
                .allowingMultipleSlots(
                    DOC_ENTRY_REFERENCE_IDS, DOC_ENTRY_REFERENCE_IDS_EXCLUDE,
                    DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_EXCLUDE,
                    DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE);

            case FIND_SUBMISSION_SETS -> rules()
                .patientId(SUBMISSION_SET_PATIENT_ID)
                // Excluded to avoid validation errors for xdstest requests
                // .oidList(SUBMISSION_SET_SOURCE_ID)
                .timestamps(SUBMISSION_SET_SUBMISSION_TIME_FROM, SUBMISSION_SET_SUBMISSION_TIME_TO)
                .optionalString(SUBMISSION_SET_AUTHOR_PERSON)
                .code(SUBMISSION_SET_CONTENT_TYPE_CODE)
                .status(SUBMISSION_SET_STATUS)
                .homeCommunityId(optionality);

            case FIND_FOLDERS -> findFoldersRules(true, optionality);
            case FIND_FOLDERS_MPQ -> findFoldersRules(false, optionality)
                .requiringAtLeastOneOf(FOLDER_PATIENT_ID, FOLDER_CODES);

            case GET_ALL -> rules()
                .patientId(PATIENT_ID)
                .status(DOC_ENTRY_STATUS, SUBMISSION_SET_STATUS, FOLDER_STATUS)
                .codeQueryList(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_SCHEME)
                .documentEntryType(DOC_ENTRY_TYPE)
                .homeCommunityId(optionality)
                .allowingMultipleSlots(DOC_ENTRY_CONFIDENTIALITY_CODE);

            case GET_DOCUMENTS -> rules()
                .homeCommunityId(optionality)
                .identifierList(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID, DOC_ENTRY_LOGICAL_ID);

            case GET_DOCUMENTS_AND_ASSOCIATIONS -> rules()
                .homeCommunityId(optionality)
                .identifierList(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID);

            case GET_FOLDERS -> rules()
                .homeCommunityId(optionality)
                .identifierList(FOLDER_UUID, FOLDER_UNIQUE_ID, FOLDER_LOGICAL_ID);

            case GET_FOLDERS_FOR_DOCUMENT -> rules()
                .homeCommunityId(optionality)
                .identifierValue(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID);

            case GET_ASSOCIATIONS, GET_SUBMISSION_SETS -> rules()
                .homeCommunityId(optionality)
                .stringList(UUID);

            case GET_SUBMISSION_SET_AND_CONTENTS -> rules()
                .homeCommunityId(optionality)
                .identifierValue(SUBMISSION_SET_UUID, SUBMISSION_SET_UNIQUE_ID)
                .contentsFilters()
                .allowingMultipleSlots(DOC_ENTRY_CONFIDENTIALITY_CODE);

            case GET_FOLDER_AND_CONTENTS -> rules()
                .homeCommunityId(optionality)
                .identifierValue(FOLDER_UUID, FOLDER_UNIQUE_ID)
                .contentsFilters()
                .allowingMultipleSlots(DOC_ENTRY_CONFIDENTIALITY_CODE);

            case GET_RELATED_DOCUMENTS -> rules()
                .homeCommunityId(optionality)
                .identifierValue(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID)
                .association(ASSOCIATION_TYPE)
                .documentEntryType(DOC_ENTRY_TYPE);

            case FIND_MEDICATION_TREATMENT_PLANS, FIND_PRESCRIPTIONS, FIND_DISPENSES,
                 FIND_MEDICATION_ADMINISTRATIONS, FIND_PRESCRIPTIONS_FOR_VALIDATION,
                 FIND_PRESCRIPTIONS_FOR_DISPENSE -> rules()
                .patientId(DOC_ENTRY_PATIENT_ID)
                .choice(true, DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID)
                .stringList(FOLDER_UUID, FOLDER_UNIQUE_ID)
                .code(DOC_ENTRY_PRACTICE_SETTING_CODE)
                .documentTimeRanges()
                .code(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE,
                    DOC_ENTRY_EVENT_CODE,
                    DOC_ENTRY_CONFIDENTIALITY_CODE)
                .stringList(DOC_ENTRY_AUTHOR_PERSON)
                .status(DOC_ENTRY_STATUS)
                .allowingMultipleSlots(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE);

            case FIND_MEDICATION_LIST -> rules()
                .patientId(DOC_ENTRY_PATIENT_ID)
                .timestamps(
                    DOC_ENTRY_SERVICE_START_FROM,
                    DOC_ENTRY_SERVICE_START_TO,
                    DOC_ENTRY_SERVICE_END_FROM,
                    DOC_ENTRY_SERVICE_END_TO)
                .code(DOC_ENTRY_FORMAT_CODE)
                .status(DOC_ENTRY_STATUS)
                .documentEntryType(DOC_ENTRY_TYPE);

            case SUBSCRIPTION_FOR_DOCUMENT_ENTRY -> rules()
                .patientId(DOC_ENTRY_PATIENT_ID)
                .code(DOC_ENTRY_CLASS_CODE, DOC_ENTRY_TYPE_CODE)
                .stringList(DOC_ENTRY_REFERENCE_IDS)
                .code(DOC_ENTRY_PRACTICE_SETTING_CODE,
                    DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE,
                    DOC_ENTRY_FORMAT_CODE)
                .codeQueryList(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME)
                .codeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME)
                .stringList(DOC_ENTRY_AUTHOR_PERSON);

            case SUBSCRIPTION_FOR_PATIENT_INDEPENDENT_DOCUMENT_ENTRY -> rules()
                .code(DOC_ENTRY_CLASS_CODE,
                    DOC_ENTRY_TYPE_CODE,
                    DOC_ENTRY_PRACTICE_SETTING_CODE,
                    DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE)
                .codeQueryList(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME)
                .codeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME)
                .code(DOC_ENTRY_FORMAT_CODE)
                .stringList(DOC_ENTRY_AUTHOR_PERSON)
                .requiringAtLeastOneOf(
                    DOC_ENTRY_CLASS_CODE,
                    DOC_ENTRY_TYPE_CODE,
                    DOC_ENTRY_PRACTICE_SETTING_CODE,
                    DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE);

            case SUBSCRIPTION_FOR_FOLDER -> rules()
                .patientId(FOLDER_PATIENT_ID)
                .stringList(FOLDER_UNIQUE_ID)
                .codeQueryList(FOLDER_CODES, FOLDER_CODES_SCHEME);

            case SUBSCRIPTION_FOR_SUBMISSION_SET -> rules()
                .patientId(SUBMISSION_SET_PATIENT_ID)
                .oidList(SUBMISSION_SET_SOURCE_ID)
                .optionalString(SUBMISSION_SET_AUTHOR_PERSON)
                .stringList(SUBMISSION_SET_INTENDED_RECIPIENT);

            case SUBSCRIPTION_FOR_PATIENT_INDEPENDENT_SUBMISSION_SET -> rules()
                .oidList(SUBMISSION_SET_SOURCE_ID)
                .optionalString(SUBMISSION_SET_AUTHOR_PERSON)
                .stringList(SUBMISSION_SET_INTENDED_RECIPIENT)
                .requiringAtLeastOneOf(
                    SUBMISSION_SET_SOURCE_ID,
                    SUBMISSION_SET_AUTHOR_PERSON,
                    SUBMISSION_SET_INTENDED_RECIPIENT);
        };
    }


    // ------------------------------------------------------------------------
    // Rules of query type families
    // ------------------------------------------------------------------------

    private static QueryRules findDocumentsRules(boolean singlePatient, HomeCommunityIdOptionality optionality) {
        return rules()
            .patientId(DOC_ENTRY_PATIENT_ID, singlePatient)
            .documentMetadata(false)
            .documentStatusAndType()
            .homeCommunityId(optionality)
            .allowingMultipleSlots(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE);
    }

    private static QueryRules findDocumentsByReferenceIdRules(boolean singlePatient, HomeCommunityIdOptionality optionality) {
        return rules()
            .patientId(DOC_ENTRY_PATIENT_ID, singlePatient)
            .documentMetadata(false)
            .documentStatusAndType()
            .stringList(DOC_ENTRY_REFERENCE_IDS)
            .homeCommunityId(optionality)
            .allowingMultipleSlots(DOC_ENTRY_REFERENCE_IDS, DOC_ENTRY_EVENT_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE);
    }

    private static QueryRules findFoldersRules(boolean singlePatient, HomeCommunityIdOptionality optionality) {
        return rules()
            .patientId(FOLDER_PATIENT_ID, singlePatient)
            .timestamps(FOLDER_LAST_UPDATE_TIME_FROM, FOLDER_LAST_UPDATE_TIME_TO)
            .codeQueryList(FOLDER_CODES, FOLDER_CODES_SCHEME)
            .status(FOLDER_STATUS)
            .homeCommunityId(optionality)
            .allowingMultipleSlots(FOLDER_CODES);
    }

    // ------------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------------

    @Override
    public void validate(EbXMLAdhocQueryRequest<AdhocQueryRequest> request, ValidationProfile profile) {
        requireNonNull(request, "request cannot be null");

        if (profile == ITI_63) {
            metaDataAssert(LEAF_CLASS_WITH_REPOSITORY_ITEM.getCode().equals(request.getReturnType()),
                UNKNOWN_RETURN_TYPE, request.getReturnType());
        } else {
            metaDataAssert(LEAF_CLASS.getCode().equals(request.getReturnType())
                    || OBJECT_REF.getCode().equals(request.getReturnType()),
                UNKNOWN_RETURN_TYPE, request.getReturnType());
        }

        var queryType = QueryType.valueOfId(request.getId());
        metaDataAssert(queryType != null, UNKNOWN_QUERY_TYPE, request.getId());

        var allowedQueryTypes = ALLOWED_QUERY_TYPES.getOrDefault(profile.getInteractionId(), Collections.emptySet());
        metaDataAssert(allowedQueryTypes.contains(queryType), UNSUPPORTED_QUERY_TYPE, queryType);

        getRules(queryType, profile).validate(request);
    }
}
