/*
 * Copyright 2019 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.audit;


import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.GenericFhirAuditMessageBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_OPERATION_HEADER;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_REQUEST_DETAILS;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_RESOURCE_TYPE_HEADER;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_QUERY;

/**
 * Generic Audit Strategy for FHIR interfaces. The audit written is built alongside what is
 * defined for IHE FHIR transactions, with the resource type (e.g. Encounter) as event type code.
 *
 * @author Christian Ohr
 */
public class GenericFhirAuditStrategy<T extends IDomainResource> extends FhirAuditStrategy<GenericFhirAuditDataset> {

    private final Function<T, Optional<? extends IBaseReference>> patientIdExtractor;

    /**
     * @param serverSide         server side auditing
     * @param operations         operations for accessthe FHIR version-specific OperationOutcome
     * @param patientIdExtractor function that extracts a patient reference from a domain resource
     */
    public GenericFhirAuditStrategy(boolean serverSide, IBaseOperationOutcomeOperations operations,
                                    Function<T, Optional<? extends IBaseReference>> patientIdExtractor) {
        super(serverSide, operations);
        this.patientIdExtractor = requireNonNull(patientIdExtractor);
    }

    @Override
    public GenericFhirAuditDataset createAuditDataset() {
        return new GenericFhirAuditDataset(isServerSide());
    }

    @Override
    public GenericFhirAuditDataset enrichAuditDatasetFromRequest(GenericFhirAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        var requestDetails = (RequestDetails) parameters.get(FHIR_REQUEST_DETAILS);

        var resourceType = (String) parameters.get(FHIR_RESOURCE_TYPE_HEADER);
        if (resourceType == null && requestDetails != null) {
            resourceType = requestDetails.getResourceName();
        }
        auditDataset.setAffectedResourceType(resourceType);

        var operation = (RestOperationTypeEnum) parameters.get(FHIR_OPERATION_HEADER);
        if (operation == null && requestDetails != null) {
            operation = requestDetails.getRestOperationType();
        }
        auditDataset.setOperation(operation);

        // Extract operation name
        if (requestDetails != null) {
            if (requestDetails.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE ||
                    requestDetails.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE ||
                    requestDetails.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_SERVER) {
                auditDataset.setOperationName(requestDetails.getOperation());
            }

            // set resource ID with extended-instance-operations
            if (requestDetails.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE) {
                auditDataset.setResourceId(requestDetails.getId());
            }
        }


        // Domain Resource in the request? Extract Patient ID and Sensitivity at this point
        if (request instanceof IDomainResource) {
            addResourceData(auditDataset, (T) request);
        } else if (request instanceof IIdType) {
            auditDataset.setResourceId((IIdType) request);
        }

        if (parameters.containsKey(Constants.FHIR_REQUEST_PARAMETERS)) {
            var query = (String) parameters.get(HTTP_QUERY);
            auditDataset.setQueryString(query);

            var searchParameter = (FhirSearchParameters) parameters.get(Constants.FHIR_REQUEST_PARAMETERS);
            if (searchParameter != null) {
                var tokenParams = searchParameter.getPatientIdParam();
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
            var methodOutcome = (MethodOutcome) response;
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
        var builder = new GenericFhirAuditMessageBuilder(auditContext, auditDataset)
                .addPatients(auditDataset);
        if (auditDataset.getQueryString() != null) {
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
                        patient.getReferenceElement().getValue()));

        var securityLabels = resource.getMeta().getSecurity();
        if (!securityLabels.isEmpty()) {
            auditDataset.setSecurityLabel(securityLabels.get(0).getCode());
        }
    }
}
