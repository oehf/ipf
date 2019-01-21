/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.support;


import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;
import org.openehealth.ipf.commons.ihe.fhir.RequestDetailProvider;
import org.openehealth.ipf.commons.ihe.fhir.audit.GenericFhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.GenericFhirAuditMessageBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.*;

/**
 * Generic Audit Strategy for FHIR interfaces. The audit written is built alongside what is
 * defined for IHE FHIR transactions, with the resource type (e.g. Encounter) as event type code.
 *
 * @author Christian Ohr
 */
public class GenericFhirAuditStrategy<T extends IDomainResource> extends FhirAuditStrategy<GenericFhirAuditDataset> {

    private Function<T, Optional<Reference>> patientIdExtractor;

    /**
     * @param serverSide         server side auditing
     * @param patientIdExtractor function that extracts a patient reference from a domain resource
     */
    public GenericFhirAuditStrategy(boolean serverSide, Function<T, Optional<Reference>> patientIdExtractor) {
        super(serverSide);
        this.patientIdExtractor = requireNonNull(patientIdExtractor);
    }

    @Override
    public GenericFhirAuditDataset createAuditDataset() {
        return new GenericFhirAuditDataset(isServerSide());
    }

    @Override
    public GenericFhirAuditDataset enrichAuditDatasetFromRequest(GenericFhirAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);

        String resourceType = (String) parameters.get(FHIR_RESOURCE_TYPE_HEADER);
        if (resourceType == null && RequestDetailProvider.getRequestDetails() != null) {
            resourceType = RequestDetailProvider.getRequestDetails().getResourceName();
        }
        auditDataset.setAffectedResourceType(resourceType);

        RestOperationTypeEnum operation = (RestOperationTypeEnum) parameters.get(FHIR_OPERATION_HEADER);
        if (operation == null && RequestDetailProvider.getRequestDetails() != null) {
            operation = RequestDetailProvider.getRequestDetails().getRestOperationType();
        }
        auditDataset.setOperation(operation);

        // Domain Resource in the request? Extract Patient ID and Sensitivity at this point
        if (request instanceof IDomainResource) {
            addResourceData(auditDataset, (T) request);
        } else if (request instanceof IIdType) {
            auditDataset.setResourceId((IIdType) request);
        }

        if (parameters.containsKey(Constants.FHIR_REQUEST_PARAMETERS)) {
            String query = (String) parameters.get(HTTP_QUERY);
            auditDataset.setQueryString(query);

            FhirSearchParameters searchParameter = (FhirSearchParameters) parameters.get(Constants.FHIR_REQUEST_PARAMETERS);
            if (searchParameter != null) {
                List<TokenParam> tokenParams = searchParameter.getPatientIdParam();
                if (tokenParams != null) {
                    auditDataset.getPatientIds().addAll(
                            tokenParams.stream()
                                    .map(t -> t.getValueAsQueryToken(searchParameter.getFhirContext()))
                                    .collect(Collectors.toList()));
                }
            }
        }
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(GenericFhirAuditDataset auditDataset, Object response, AuditContext auditContext) {
        // Domain Resource in the request? Extract Patient ID and Sensitivity at this point
        if (response instanceof IDomainResource) {
            addResourceData(auditDataset, (T) response);
        }
        if (response instanceof MethodOutcome) {
            MethodOutcome methodOutcome = (MethodOutcome) response;
            if (methodOutcome.getCreated() != null && methodOutcome.getCreated()) {
                auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
            }
            if (methodOutcome.getOperationOutcome() != null) {
                super.enrichAuditDatasetFromResponse(auditDataset, methodOutcome.getOperationOutcome(), auditContext);
            } else {
                auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
            }
            if (methodOutcome.getResource() != null && methodOutcome.getResource() instanceof IDomainResource) {
                addResourceData(auditDataset, (T) methodOutcome.getResource());
            } else if (methodOutcome.getId() != null) {
                auditDataset.setResourceId(methodOutcome.getId());
                if (methodOutcome.getId().hasResourceType()) {
                    auditDataset.setAffectedResourceType(methodOutcome.getId().getResourceType());
                }
            }
        }
        return super.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, GenericFhirAuditDataset auditDataset) {
        GenericFhirAuditMessageBuilder builder = new GenericFhirAuditMessageBuilder(auditContext, auditDataset)
                .addPatients(auditDataset);
        if (auditDataset.getAffectedResourceType() != null && auditDataset.getQueryString() != null) {
            builder.addQueryParticipantObject(auditDataset);
        } else if (auditDataset.getResourceId() != null &&
                auditDataset.getResourceId().hasResourceType() &&
                auditDataset.getResourceId().hasIdPart()) {
            builder.addResourceParticipantObject(auditDataset);
        }
        return builder.getMessages();
    }

    private void addResourceData(GenericFhirAuditDataset auditDataset, T resource) {
        auditDataset.setResourceId(resource.getIdElement());
        if (resource.getIdElement().hasResourceType()) {
            auditDataset.setAffectedResourceType(resource.getIdElement().getResourceType());
        }
        patientIdExtractor.apply(resource).ifPresent(patient ->
                auditDataset.getPatientIds().add(patient.getResource() != null ?
                        patient.getResource().getIdElement().toUnqualifiedVersionless().getValue() :
                        patient.getReference()));

        List<? extends IBaseCoding> securityLabels = resource.getMeta().getSecurity();
        if (!securityLabels.isEmpty()) {
            auditDataset.setSecurityLabel(securityLabels.get(0).getCode());
        }
    }
}
