/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.pharm5;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.*;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Resource Provider for CMPD PHARM-5.
 *
 * @author Quentin Ligier
 * @since 4.3
 **/
public class Pharm5ResourceProvider extends AbstractPlainProvider {
    public static final String SP_PATIENT_IDENTIFIER = DocumentReference.SP_PATIENT + "." + Patient.SP_IDENTIFIER;
    public static final String SP_AUTHOR_FAMILY = DocumentReference.SP_AUTHOR + "." + Practitioner.SP_FAMILY;
    public static final String SP_AUTHOR_GIVEN = DocumentReference.SP_AUTHOR + "." + Practitioner.SP_GIVEN;

    @Operation(name = "$find-medication-treatment-plans", type = DocumentReference.class, idempotent = true,
            bundleType = BundleTypeEnum.SEARCHSET)
    public IBundleProvider findMedicationTreatmentPlans(
            @OperationParam(name = SP_PATIENT_IDENTIFIER, min = 1, max = 1) TokenParam patient,
            @OperationParam(name = DocumentReference.SP_STATUS, min = 1, max = 2) TokenOrListParam status,
            @OperationParam(name = DocumentReference.SP_IDENTIFIER) TokenParam identifier,
            @OperationParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OperationParam(name = DocumentReference.SP_DATE) DateRangeParam date,
            @OperationParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OperationParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @OperationParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OperationParam(name = DocumentReference.SP_EVENT) TokenOrListParam event,
            @OperationParam(name = DocumentReference.SP_SECURITY_LABEL) TokenOrListParam securityLabel,
            @OperationParam(name = SP_AUTHOR_FAMILY) StringParam authorFamily,
            @OperationParam(name = SP_AUTHOR_GIVEN) StringParam authorGiven,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return this.stableSearch(patient, identifier, setting, date, period, format, facility, event, securityLabel,
                authorFamily, authorGiven, status, sortSpec, includeSpec, requestDetails, httpServletRequest,
                httpServletResponse, Pharm5Operations.FIND_MEDICATION_TREATMENT_PLANS);
    }

    @Operation(name = "$find-prescriptions", type = DocumentReference.class, idempotent = true,
            bundleType = BundleTypeEnum.SEARCHSET)
    public IBundleProvider findPrescriptions(
            @OperationParam(name = SP_PATIENT_IDENTIFIER, min = 1, max = 1) TokenParam patient,
            @OperationParam(name = DocumentReference.SP_STATUS, min = 1, max = 2) TokenOrListParam status,
            @OperationParam(name = DocumentReference.SP_IDENTIFIER) TokenParam identifier,
            @OperationParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OperationParam(name = DocumentReference.SP_DATE) DateRangeParam date,
            @OperationParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OperationParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @OperationParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OperationParam(name = DocumentReference.SP_EVENT) TokenOrListParam event,
            @OperationParam(name = DocumentReference.SP_SECURITY_LABEL) TokenOrListParam securityLabel,
            @OperationParam(name = SP_AUTHOR_FAMILY) StringParam authorFamily,
            @OperationParam(name = SP_AUTHOR_GIVEN) StringParam authorGiven,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return this.stableSearch(patient, identifier, setting, date, period, format, facility, event, securityLabel,
                authorFamily, authorGiven, status, sortSpec, includeSpec, requestDetails, httpServletRequest, httpServletResponse,
                Pharm5Operations.FIND_PRESCRIPTIONS);
    }

    @Operation(name = "$find-dispenses", type = DocumentReference.class, idempotent = true,
            bundleType = BundleTypeEnum.SEARCHSET)
    public IBundleProvider findDispenses(
            @OperationParam(name = SP_PATIENT_IDENTIFIER, min = 1, max = 1) TokenParam patient,
            @OperationParam(name = DocumentReference.SP_STATUS, min = 1, max = 2) TokenOrListParam status,
            @OperationParam(name = DocumentReference.SP_IDENTIFIER) TokenParam identifier,
            @OperationParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OperationParam(name = DocumentReference.SP_DATE) DateRangeParam date,
            @OperationParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OperationParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @OperationParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OperationParam(name = DocumentReference.SP_EVENT) TokenOrListParam event,
            @OperationParam(name = DocumentReference.SP_SECURITY_LABEL) TokenOrListParam securityLabel,
            @OperationParam(name = SP_AUTHOR_FAMILY) StringParam authorFamily,
            @OperationParam(name = SP_AUTHOR_GIVEN) StringParam authorGiven,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return this.stableSearch(patient, identifier, setting, date, period, format, facility, event, securityLabel,
                authorFamily, authorGiven, status, sortSpec, includeSpec, requestDetails, httpServletRequest, httpServletResponse,
                Pharm5Operations.FIND_DISPENSES);
    }

    @Operation(name = "$find-medication-administrations", type = DocumentReference.class, idempotent = true,
            bundleType = BundleTypeEnum.SEARCHSET)
    public IBundleProvider findMedicationAdministrations(
            @OperationParam(name = SP_PATIENT_IDENTIFIER, min = 1, max = 1) TokenParam patient,
            @OperationParam(name = DocumentReference.SP_STATUS, min = 1, max = 2) TokenOrListParam status,
            @OperationParam(name = DocumentReference.SP_IDENTIFIER) TokenParam identifier,
            @OperationParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OperationParam(name = DocumentReference.SP_DATE) DateRangeParam date,
            @OperationParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OperationParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @OperationParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OperationParam(name = DocumentReference.SP_EVENT) TokenOrListParam event,
            @OperationParam(name = DocumentReference.SP_SECURITY_LABEL) TokenOrListParam securityLabel,
            @OperationParam(name = SP_AUTHOR_FAMILY) StringParam authorFamily,
            @OperationParam(name = SP_AUTHOR_GIVEN) StringParam authorGiven,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return this.stableSearch(patient, identifier, setting, date, period, format, facility, event, securityLabel,
                authorFamily, authorGiven, status, sortSpec, includeSpec, requestDetails, httpServletRequest, httpServletResponse,
                Pharm5Operations.FIND_MEDICATION_ADMINISTRATIONS);
    }

    @Operation(name = "$find-prescriptions-for-validation", type = DocumentReference.class, idempotent = true,
            bundleType = BundleTypeEnum.SEARCHSET)
    public IBundleProvider findPrescriptionsForValidation(
            @OperationParam(name = SP_PATIENT_IDENTIFIER, min = 1, max = 1) TokenParam patient,
            @OperationParam(name = DocumentReference.SP_STATUS, min = 1, max = 2) TokenOrListParam status,
            @OperationParam(name = DocumentReference.SP_IDENTIFIER) TokenParam identifier,
            @OperationParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OperationParam(name = DocumentReference.SP_DATE) DateRangeParam date,
            @OperationParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OperationParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @OperationParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OperationParam(name = DocumentReference.SP_EVENT) TokenOrListParam event,
            @OperationParam(name = DocumentReference.SP_SECURITY_LABEL) TokenOrListParam securityLabel,
            @OperationParam(name = SP_AUTHOR_FAMILY) StringParam authorFamily,
            @OperationParam(name = SP_AUTHOR_GIVEN) StringParam authorGiven,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return this.stableSearch(patient, identifier, setting, date, period, format, facility, event, securityLabel,
                authorFamily, authorGiven, status, sortSpec, includeSpec, requestDetails, httpServletRequest, httpServletResponse,
                Pharm5Operations.FIND_PRESCRIPTIONS_FOR_VALIDATION);
    }

    @Operation(name = "$find-prescriptions-for-dispense", type = DocumentReference.class, idempotent = true,
            bundleType = BundleTypeEnum.SEARCHSET)
    public IBundleProvider findPrescriptionsForDispense(
            @OperationParam(name = SP_PATIENT_IDENTIFIER, min = 1, max = 1) TokenParam patient,
            @OperationParam(name = DocumentReference.SP_STATUS, min = 1, max = 2) TokenOrListParam status,
            @OperationParam(name = DocumentReference.SP_IDENTIFIER) TokenParam identifier,
            @OperationParam(name = DocumentReference.SP_SETTING) TokenOrListParam setting,
            @OperationParam(name = DocumentReference.SP_DATE) DateRangeParam date,
            @OperationParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OperationParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @OperationParam(name = DocumentReference.SP_FACILITY) TokenOrListParam facility,
            @OperationParam(name = DocumentReference.SP_EVENT) TokenOrListParam event,
            @OperationParam(name = DocumentReference.SP_SECURITY_LABEL) TokenOrListParam securityLabel,
            @OperationParam(name = SP_AUTHOR_FAMILY) StringParam authorFamily,
            @OperationParam(name = SP_AUTHOR_GIVEN) StringParam authorGiven,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return this.stableSearch(patient, identifier, setting, date, period, format, facility, event, securityLabel,
                authorFamily, authorGiven, status, sortSpec, includeSpec, requestDetails, httpServletRequest, httpServletResponse,
                Pharm5Operations.FIND_PRESCRIPTIONS_FOR_DISPENSE);
    }

    // The method is not idempotent, but we need it to support GET requests
    @Operation(name = "$find-medication-list", type = DocumentReference.class, idempotent = true,
            bundleType = BundleTypeEnum.SEARCHSET)
    public IBundleProvider findMedicationList(
            @OperationParam(name = SP_PATIENT_IDENTIFIER, min = 1, max = 1) TokenParam patient,
            @OperationParam(name = DocumentReference.SP_STATUS, min = 1, max = 2) TokenOrListParam status,
            @OperationParam(name = DocumentReference.SP_PERIOD) DateRangeParam period,
            @OperationParam(name = DocumentReference.SP_FORMAT) TokenOrListParam format,
            @Sort SortSpec sortSpec,
            @IncludeParam Set<Include> includeSpec,
            RequestDetails requestDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        final var searchParameters = Pharm5SearchParameters.builder()
                .status(status)
                .period(period)
                .format(format)
                .patientIdentifier(patient)
                .sortSpec(sortSpec)
                .includeSpec(includeSpec)
                .fhirContext(getFhirContext())
                .operation(Pharm5Operations.FIND_MEDICATION_LIST)
                .build();

        // Run down the route
        return requestBundleProvider(null, searchParameters, ResourceType.DocumentReference.name(),
                httpServletRequest, httpServletResponse, requestDetails);
    }

    IBundleProvider stableSearch(final TokenParam patient,
                                 final TokenParam identifier,
                                 final TokenOrListParam setting,
                                 final DateRangeParam date,
                                 final DateRangeParam period,
                                 final TokenOrListParam format,
                                 final TokenOrListParam facility,
                                 final TokenOrListParam event,
                                 final TokenOrListParam securityLabel,
                                 final StringParam authorFamily,
                                 final StringParam authorGiven,
                                 final TokenOrListParam status,
                                 final SortSpec sortSpec,
                                 final Set<Include> includeSpec,
                                 final RequestDetails requestDetails,
                                 final HttpServletRequest httpServletRequest,
                                 final HttpServletResponse httpServletResponse,
                                 final Pharm5Operations operation) {

        final var searchParameters = Pharm5SearchParameters.builder()
                .patientIdentifier(patient)
                .status(status)
                .identifier(identifier)
                .setting(setting)
                .date(date)
                .period(period)
                .facility(facility)
                .event(event)
                .securityLabel(securityLabel)
                .format(format)
                .authorFamilyName(authorFamily)
                .authorGivenName(authorGiven)
                .sortSpec(sortSpec)
                .includeSpec(includeSpec)
                .fhirContext(getFhirContext())
                .operation(operation)
                .build();

        // Run down the route
        return requestBundleProvider(null, searchParameters, ResourceType.DocumentReference.name(),
                httpServletRequest, httpServletResponse, requestDetails);
    }
}
