/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.chppq1;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.xml.bind.JAXBElement;
import lombok.extern.slf4j.Slf4j;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.functions.IiEqualFunction;
import org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants.StatusCode;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.DeletePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.UpdatePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.II;

/**
 * @author Dmytro Rud
 */
@Slf4j
abstract public class ChPpq1AuditStrategy extends AuditStrategySupport<ChPpqAuditDataset> {

    public ChPpq1AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public ChPpqAuditDataset createAuditDataset() {
        return new ChPpqAuditDataset(isServerSide());
    }

    @Override
    public ChPpqAuditDataset enrichAuditDatasetFromRequest(ChPpqAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        if (request instanceof AddPolicyRequest addPolicyRequest) {
            auditDataset.setAction(EventActionCode.Create);
            Xacml20Utils.toStream(addPolicyRequest).forEach(policy -> auditDataset.getPolicyAndPolicySetIds().add(policy.getId().toString()));
            auditDataset.setPatientId(extractPatientIdFromPolicy(Xacml20Utils.toStream(addPolicyRequest)));
        } else if (request instanceof UpdatePolicyRequest updatePolicyRequest) {
            auditDataset.setAction(EventActionCode.Update);
            Xacml20Utils.toStream(updatePolicyRequest).forEach(policy -> auditDataset.getPolicyAndPolicySetIds().add(policy.getId().toString()));
            auditDataset.setPatientId(extractPatientIdFromPolicy(Xacml20Utils.toStream(updatePolicyRequest)));
        } else if (request instanceof DeletePolicyRequest deletePolicyRequest) {
            auditDataset.setAction(EventActionCode.Delete);
            Xacml20Utils.toStream(deletePolicyRequest).forEach(id -> auditDataset.getPolicyAndPolicySetIds().add(id.getValue()));
            // The deletion request payload only contains a policy ID. So we have to rely on the XUA token; if no XUA token is present
            // the patient participant won't be present in the ATNA record either.
            auditDataset.setPatientId(auditDataset.getXuaPatientId());
        } else {
            log.warn("Enrichment if audit data set failed due to unknown input type: '{}'", request.getClass().getName());
        }

        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(ChPpqAuditDataset auditDataset, Object response, AuditContext auditContext) {
        auditDataset.setEventOutcomeIndicator(getEventOutcomeIndicator(auditDataset, response));
        return true;
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(ChPpqAuditDataset auditDataset, Object responseObject) {
        var response = (EprPolicyRepositoryResponse) responseObject;
        return StatusCode.SUCCESS.equals(response.getStatus()) ? EventOutcomeIndicator.Success : EventOutcomeIndicator.SeriousFailure;
    }

    /**
     * Extracts the EPR-SPID of the patient from the message payload. The EPR-SPID in the XUA token must, by definition, match the EPR-SPID of the policy(Set)
     * payload. Thus, if a XUA token is present and {@link ChPpqAuditDataset#getXuaPatientId()} returns a non-empty value, then use that instead of incurring
     * the cost of getting it from the payload with this method.
     *
     * @return The patient's EPR-SPID as stated in the payload (policy set)
     */
    private String extractPatientIdFromPolicy(Stream<Evaluatable> policyOrPolicySets) {
        final var eprSpids = policyOrPolicySets
            .flatMap(evaluatable -> evaluatable.getTarget().getResources().getResources().stream())
            .flatMap(resourceType -> resourceType.getResourceMatches().stream())
            .filter(
                resourceMatchType -> resourceMatchType.getMatchFunction().getFunctionId().equals(IiEqualFunction.ID) &&
                    resourceMatchType.getResourceAttributeDesignator().getAttributeId().equals(Xacml20Utils.ATTRIBUTE_TYPE_PATIENT_ID))
            .map(resourceMatchType -> resourceMatchType.getAttributeValue().getContent()
                .stream()
                .filter(JAXBElement.class::isInstance) // Needed to ignore whitespace content
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("AttributeValue is empty.")))
            .map(o -> ((JAXBElement<II>) o).getValue())
            .map(ii -> String.format("%s^^^&%s&ISO", ii.getExtension(), ii.getRoot()))
            .collect(Collectors.toSet());

        if (eprSpids.size() > 1) {
            throw new IllegalArgumentException(
                String.format("Policies for more than one patient are being fed in a single submission. This is not legal. EPR-SPIDs: %s", eprSpids));
        }

        return (eprSpids.stream().findFirst().orElse(null));
    }

}
