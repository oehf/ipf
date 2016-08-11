/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti83;

import org.hl7.fhir.instance.model.Identifier;
import org.hl7.fhir.instance.model.Parameters;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.Type;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditStrategy;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy for auditing ITI-83 transactions
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class Iti83AuditStrategy extends FhirQueryAuditStrategy<FhirQueryAuditDataset> {

    protected Iti83AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public FhirQueryAuditDataset createAuditDataset() {
        return new FhirQueryAuditDataset(isServerSide());
    }

    @Override
    public void doAudit(FhirQueryAuditDataset auditDataset) {
        AuditorManager.getFhirAuditor().auditIti83(
                isServerSide(),
                auditDataset.getEventOutcomeCode(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getClientIpAddress(),
                auditDataset.getQueryString(),
                auditDataset.getPatientIds());
    }

    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        FhirQueryAuditDataset dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);

        Parameters params = (Parameters) request;
        if (params != null) {
            Type sourceIdentifier = params.getParameter().stream()
                    .filter(ppc -> Constants.SOURCE_IDENTIFIER_NAME.equals(ppc.getName()))
                    .map(Parameters.ParametersParameterComponent::getValue)
                    .findFirst().orElseThrow(() -> new RuntimeException("No sourceIdentifier in PIX query"));

            if (sourceIdentifier instanceof Identifier) {
                Identifier identifier = (Identifier) sourceIdentifier;
                dataset.getPatientIds().add(String.format("%s|%s", identifier.getSystem(), identifier.getValue()));
            } else if (sourceIdentifier instanceof StringType) {
                StringType identifier = (StringType) sourceIdentifier;
                dataset.getPatientIds().add(identifier.getValue());
            } else {
                dataset.getPatientIds().add(sourceIdentifier.toString());
            }
        }
        return dataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(FhirQueryAuditDataset auditDataset, Object response) {
        boolean result = super.enrichAuditDatasetFromResponse(auditDataset, response);
        /* Pending https://github.com/oehf/ipf/issues/124
        if (result) {
            if (response instanceof Parameters) {
                Parameters parameters = (Parameters) response;
                auditDataset.getPatientIds().addAll(
                        parameters.getParameter().stream()
                                .map(Parameters.ParametersParameterComponent::getValue)
                                .filter(Identifier.class::isInstance)
                                .map(Identifier.class::cast)
                                .map(id -> String.format("%s|%s", id.getSystem(), id.getValue()))
                                .collect(Collectors.toList()));
            }
        }
        */
        return result;
    }
}
