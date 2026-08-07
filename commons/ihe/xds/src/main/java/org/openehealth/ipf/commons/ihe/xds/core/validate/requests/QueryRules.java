/*
 * Copyright 2026 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.XdsIntegrationProfile.HomeCommunityIdOptionality;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.*;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_REQUIRED_QUERY_PARAMETER;

/**
 * The validation rules of a single {@link QueryType}, and the ability to apply them to a request:
 * which parameters are validated and how, which slots may occur more than once, and which groups of
 * parameters require at least one member to be present.
 * <p>
 * Instances are built up fluently, starting from {@link #rules()}. Every method returns a new
 * instance with one more rule, so the declaration of a query type reads as the sequence of its
 * rules:
 * <pre>
 * rules().patientId(DOC_ENTRY_PATIENT_ID)
 *        .documentMetadata(false)
 *        .stringList(DOC_ENTRY_REFERENCE_IDS)
 *        .allowingMultipleSlots(DOC_ENTRY_EVENT_CODE)
 * </pre>
 * Which {@link ValueValidator} a parameter is checked with is an implementation detail of the
 * methods below, hence the naming: {@link #stringList} and {@link #optionalString} do not validate
 * the value at all, whereas {@link #oidList} and {@link #patientId} do.
 *
 * @see AdhocQueryRequestValidator
 * @since 5.3
 */
record QueryRules(
    List<QueryParameterValidation> parameterValidations,
    Set<String> multipleSlotsAllowed,
    List<List<QueryParameter>> atLeastOneOf) {

    private static final CXValidator cxValidator = new CXValidator(true);
    private static final OIDValidator oidValidator = new OIDValidator();
    private static final NopValidator nopValidator = new NopValidator();


    /**
     * @return an empty set of rules, to be filled in fluently.
     */
    static QueryRules rules() {
        return new QueryRules(List.of(), Set.of(), List.of());
    }


    // ------------------------------------------------------------------------
    // Single parameter validations
    // ------------------------------------------------------------------------

    /**
     * The patient ID of a single patient query.
     */
    QueryRules patientId(QueryParameter param) {
        return with(new StringValidation(param, cxValidator, false));
    }

    /**
     * The patient ID, either as a single value (single patient query) or as a list
     * (multi patient query).
     */
    QueryRules patientId(QueryParameter param, boolean single) {
        return single ? patientId(param) : with(new StringListValidation(param, cxValidator));
    }

    /**
     * An optional single-valued string parameter whose value is not validated any further.
     */
    QueryRules optionalString(QueryParameter param) {
        return with(new StringValidation(param, nopValidator, true));
    }

    /**
     * String list parameters whose values are not validated any further.
     */
    QueryRules stringList(QueryParameter... params) {
        return with(Arrays.stream(params)
            .<QueryParameterValidation>map(param -> new StringListValidation(param, nopValidator))
            .toList());
    }

    /**
     * A string list parameter whose values must be OIDs.
     */
    QueryRules oidList(QueryParameter param) {
        return with(new StringListValidation(param, oidValidator));
    }

    QueryRules code(QueryParameter... params) {
        return with(Arrays.stream(params)
            .<QueryParameterValidation>map(CodeValidation::new)
            .toList());
    }

    QueryRules code(QueryParameter param, boolean optional) {
        return with(new CodeValidation(param, optional));
    }

    QueryRules codeQueryList(QueryParameter param, QueryParameter schemeParam) {
        return with(new QueryListCodeValidation(param, schemeParam));
    }

    QueryRules timestamps(QueryParameter... params) {
        return with(Arrays.stream(params)
            .<QueryParameterValidation>map(TimestampValidation::new)
            .toList());
    }

    QueryRules status(QueryParameter... params) {
        return with(Arrays.stream(params)
            .<QueryParameterValidation>map(StatusValidation::new)
            .toList());
    }

    QueryRules documentEntryType(QueryParameter param) {
        return with(new DocumentEntryTypeValidation(param));
    }

    QueryRules association(QueryParameter param) {
        return with(new AssociationValidation(param));
    }

    /**
     * At most one (if {@code optional}) or exactly one of the given parameters may be present.
     */
    QueryRules choice(boolean optional, QueryParameter... params) {
        return with(new ChoiceValidation(optional, params));
    }

    QueryRules homeCommunityId(HomeCommunityIdOptionality optionality) {
        return with(new HomeCommunityIdValidation(optionality));
    }


    // ------------------------------------------------------------------------
    // Groups of parameter validations
    // ------------------------------------------------------------------------

    /**
     * The document entry metadata parameters shared by all FindDocuments flavours and by Fetch.
     */
    QueryRules documentMetadata(boolean classCodeRequired) {
        return code(DOC_ENTRY_CLASS_CODE, !classCodeRequired)
            .code(DOC_ENTRY_TYPE_CODE,
                DOC_ENTRY_PRACTICE_SETTING_CODE,
                DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE,
                DOC_ENTRY_FORMAT_CODE)
            .documentTimeRanges()
            .codeQueryList(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME)
            .codeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME)
            .stringList(DOC_ENTRY_AUTHOR_PERSON);
    }

    QueryRules documentTimeRanges() {
        return timestamps(
            DOC_ENTRY_CREATION_TIME_FROM,
            DOC_ENTRY_CREATION_TIME_TO,
            DOC_ENTRY_SERVICE_START_TIME_FROM,
            DOC_ENTRY_SERVICE_START_TIME_TO,
            DOC_ENTRY_SERVICE_STOP_TIME_FROM,
            DOC_ENTRY_SERVICE_STOP_TIME_TO);
    }

    QueryRules documentStatusAndType() {
        return status(DOC_ENTRY_STATUS).documentEntryType(DOC_ENTRY_TYPE);
    }

    /**
     * Exactly one of the given identifier parameters must be present, and the entry UUID and unique
     * ID may carry more than one value.
     */
    QueryRules identifierList(QueryParameter uuid, QueryParameter uniqueId, QueryParameter... alsoEligible) {
        return choice(false, choiceOf(uuid, uniqueId, alsoEligible))
            .stringList(uuid, uniqueId);
    }

    /**
     * Exactly one of the given identifier parameters must be present, and it carries a single value.
     */
    QueryRules identifierValue(QueryParameter uuid, QueryParameter uniqueId) {
        return choice(false, uuid, uniqueId)
            .optionalString(uuid)
            .optionalString(uniqueId);
    }

    /**
     * Filters on the document entries contained in a submission set or folder.
     */
    QueryRules contentsFilters() {
        return codeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME)
            .codeQueryList(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_SCHEME)
            .documentEntryType(DOC_ENTRY_TYPE);
    }

    /**
     * An excluding parameter of FindDocumentsExclude cannot be combined with its non-excluding
     * counterpart (ITI TF-2: 3.18.4.1.2.3.7.15).
     */
    QueryRules mutuallyExclusiveExcludedParameters() {
        return with(EXCLUDED_PARAMETERS.stream()
            .<QueryParameterValidation>map(param -> new ChoiceValidation(true, param.base(), param.excluded()))
            .toList());
    }

    /**
     * The excluding parameters of FindDocumentsExclude are coded exactly like their counterparts
     * (ITI TF-2: 3.18.4.1.2.3.4) and are validated accordingly.
     */
    QueryRules excludedParameters() {
        return with(EXCLUDED_PARAMETERS.stream()
            .map(param -> param.validation().apply(param.excluded()))
            .toList());
    }


    // ------------------------------------------------------------------------
    // Rules that are not parameter validations
    // ------------------------------------------------------------------------

    QueryRules allowingMultipleSlots(QueryParameter... parameters) {
        var slotNames = new HashSet<>(multipleSlotsAllowed);
        Arrays.stream(parameters).map(QueryParameter::getSlotName).forEach(slotNames::add);
        return new QueryRules(parameterValidations, Set.copyOf(slotNames), atLeastOneOf);
    }

    QueryRules requiringAtLeastOneOf(QueryParameter... parameters) {
        var groups = new ArrayList<>(atLeastOneOf);
        groups.add(List.of(parameters));
        return new QueryRules(parameterValidations, multipleSlotsAllowed, List.copyOf(groups));
    }


    // ------------------------------------------------------------------------
    // Application
    // ------------------------------------------------------------------------

    /**
     * Applies all rules to the given request.
     *
     * @param request the query request.
     * @throws XDSMetaDataException if one of the rules is violated.
     */
    void validate(EbXMLAdhocQueryRequest<AdhocQueryRequest> request) throws XDSMetaDataException {
        SlotLengthAndNameUniquenessValidator.validateQuerySlots(request.getSlots(), multipleSlotsAllowed);
        parameterValidations.forEach(validation ->
            validation.validate(request));
        atLeastOneOf.forEach(parameters ->
            checkAtLeastOnePresent(request, parameters));
    }

    /**
     * Checks that at least one of the given query parameters is provided in the message.
     */
    private static void checkAtLeastOnePresent(
        EbXMLAdhocQueryRequest<AdhocQueryRequest> request, List<QueryParameter> parameters) {
        var slotNames = parameters.stream().map(QueryParameter::getSlotName).toList();
        slotNames.stream()
            .map(request::getSlotValues)
            .filter(slotList -> !slotList.isEmpty())
            .findAny()
            .orElseThrow(() -> new XDSMetaDataException(MISSING_REQUIRED_QUERY_PARAMETER, "one of " +
                StringUtils.join(slotNames, ", ")));
    }


    // ------------------------------------------------------------------------
    // Parameters of FindDocumentsExclude
    // ------------------------------------------------------------------------

    /**
     * An excluding query parameter of FindDocumentsExclude, its non-excluding counterpart, and the
     * kind of validation both of them require.
     */
    private record ExcludedParameter(
        QueryParameter base,
        QueryParameter excluded,
        Function<QueryParameter, QueryParameterValidation> validation) {
    }

    /**
     * The excluding parameters of FindDocumentsExclude (ITI TF-2: 3.18.4.1.2.3.7.15). Both the
     * mutual-exclusion checks and the validations of the excluding parameters themselves are
     * derived from this table.
     */
    private static final List<ExcludedParameter> EXCLUDED_PARAMETERS = List.of(
        new ExcludedParameter(DOC_ENTRY_CLASS_CODE, DOC_ENTRY_CLASS_CODE_EXCLUDE,
            CodeValidation::new),
        new ExcludedParameter(DOC_ENTRY_TYPE_CODE, DOC_ENTRY_TYPE_CODE_EXCLUDE,
            CodeValidation::new),
        new ExcludedParameter(DOC_ENTRY_PRACTICE_SETTING_CODE, DOC_ENTRY_PRACTICE_SETTING_CODE_EXCLUDE,
            CodeValidation::new),
        new ExcludedParameter(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE, DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_EXCLUDE,
            CodeValidation::new),
        new ExcludedParameter(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_EXCLUDE,
            CodeValidation::new),
        new ExcludedParameter(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_EXCLUDE,
            param -> new QueryListCodeValidation(param, DOC_ENTRY_EVENT_CODE_EXCLUDE_SCHEME)),
        new ExcludedParameter(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE,
            param -> new QueryListCodeValidation(param, DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE_SCHEME)),
        new ExcludedParameter(DOC_ENTRY_AUTHOR_PERSON, DOC_ENTRY_AUTHOR_PERSON_EXCLUDE,
            param -> new StringListValidation(param, nopValidator)),
        new ExcludedParameter(DOC_ENTRY_TYPE, DOC_ENTRY_TYPE_EXCLUDE,
            DocumentEntryTypeValidation::new),
        new ExcludedParameter(DOC_ENTRY_REFERENCE_IDS, DOC_ENTRY_REFERENCE_IDS_EXCLUDE,
            param -> new StringListValidation(param, nopValidator)));


    // ------------------------------------------------------------------------
    // Plumbing
    // ------------------------------------------------------------------------

    private QueryRules with(QueryParameterValidation validation) {
        return with(List.of(validation));
    }

    private QueryRules with(List<QueryParameterValidation> validations) {
        return new QueryRules(
            Stream.concat(parameterValidations.stream(), validations.stream()).toList(),
            multipleSlotsAllowed,
            atLeastOneOf);
    }

    private static QueryParameter[] choiceOf(QueryParameter first, QueryParameter second, QueryParameter... rest) {
        var all = new QueryParameter[rest.length + 2];
        all[0] = first;
        all[1] = second;
        System.arraycopy(rest, 0, all, 2, rest.length);
        return all;
    }
}
