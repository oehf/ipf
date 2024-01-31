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
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.BalpJwtUtils;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.GenericFhirAuditMessageBuilder;

import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_OPERATION_HEADER;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_REQUEST_DETAILS;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_RESOURCE_TYPE_HEADER;

/**
 * Generic Audit Strategy for FHIR interfaces. The audit written is built alongside what is
 * defined for IHE FHIR transactions, with the resource type (e.g. Encounter) as event type code.
 *
 * @author Christian Ohr
 */
public class GenericFhirAuditStrategy extends FhirAuditStrategy<GenericFhirAuditDataset> {

    private final PatientIdExtractor patientIdExtractor;

    /**
     * @param serverSide         server side auditing
     * @param patientIdExtractor function that extracts a patient reference from a domain resource
     */
    public GenericFhirAuditStrategy(boolean serverSide, PatientIdExtractor patientIdExtractor) {
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
        var requestDetails = (ServletRequestDetails) parameters.get(FHIR_REQUEST_DETAILS);

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

        // Resource in the request? Extract Patient ID and Sensitivity at this point
        if (request instanceof IDomainResource) {
            addResourceData(auditDataset, (IDomainResource) request);
        } else if (request instanceof IBaseBinary) {
            addResourceData(auditDataset, (IBaseBinary) request);
        } else if (request instanceof IIdType) {
            auditDataset.setResourceId(((IIdType) request).toUnqualifiedVersionless());
        }

        // For instance-level operations e.g. PUT, DELETE, EXTENDED_OPERATION_INSTANCE: set resource ID and patient ID
        if (auditDataset.getResourceId() == null && requestDetails != null && requestDetails.getId() != null) {
            var id = requestDetails.getId();
            auditDataset.setResourceId(id.toUnqualifiedVersionless());
            if (id.hasResourceType()) {
                auditDataset.setAffectedResourceType(id.getResourceType());
            }
            if ("Patient".equals(id.getResourceType())) {
                auditDataset.getPatientIds().add(id.getIdPart());
            }
        }

        // Domain Resource in the request? Extract Patient ID and Sensitivity at this point
        if (request instanceof IDomainResource) {
            addResourceData(auditDataset, (IDomainResource) request);
        } else if (request instanceof IIdType) {
            auditDataset.setResourceId((IIdType) request);
        }

        if (requestDetails != null && requestDetails.getRestOperationType() != null) {
            switch (requestDetails.getRestOperationType()) {
                case EXTENDED_OPERATION_SERVER:
                case EXTENDED_OPERATION_TYPE:
                case EXTENDED_OPERATION_INSTANCE:
                    auditDataset.setOperationName(requestDetails.getOperation());
                    break;
                case SEARCH_TYPE: {
                    auditDataset.setQueryString(requestDetails.getServletRequest().getQueryString());
                    patientIdExtractor.patientReferenceFromSearchParameter(requestDetails).ifPresentOrElse(
                            id -> auditDataset.getPatientIds().add(id),
                            () -> patientIdExtractor.patientIdentifierFromSearchParameter(requestDetails).ifPresent(
                                    id -> auditDataset.getPatientIds().add(id.substring(id.indexOf("|") + 1))));
                    break;
                }
                case SEARCH_SYSTEM:
                    auditDataset.setQueryString(requestDetails.getServletRequest().getQueryString());
                    break;
            }
        }

        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(GenericFhirAuditDataset auditDataset, Object response, AuditContext auditContext) {
        // Domain Resource in the request? Extract Patient ID and Sensitivity at this point
        if (response instanceof IDomainResource) {
            addResourceData(auditDataset, (IDomainResource) response);
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
                addResourceData(auditDataset, methodOutcome.getResource());
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
        builder.addJwtParticipants(auditDataset);
        return builder.getMessages();
    }

    private void addResourceData(GenericFhirAuditDataset auditDataset, IBaseResource resource) {
        // Note that OperationOutcome is also a IDomainResource. Do not overwrite any resource
        // details has already been recorded.
        if (auditDataset.getResourceId() == null && resource.getIdElement().hasIdPart()) {
            auditDataset.setResourceId(resource.getIdElement().toUnqualifiedVersionless());
        }
        if (resource.getIdElement().hasResourceType()) {
            auditDataset.setAffectedResourceType(resource.getIdElement().getResourceType());
        }
        patientIdExtractor.patientReferenceFromResource(resource).ifPresent(ref ->
                auditDataset.getPatientIds().add(ref.getResource() != null ?
                        ref.getResource().getIdElement().getIdPart() :
                        ref.getReferenceElement().getIdPart()));

        var securityLabels = resource.getMeta().getSecurity();
        if (!securityLabels.isEmpty()) {
            auditDataset.setSecurityLabel(securityLabels.get(0).getCode());
        }
    }

}
