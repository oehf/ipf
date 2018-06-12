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
package org.openehealth.ipf.commons.ihe.xacml20.chppq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.audit.codes.PpqEventTypeCodes;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants.StatusCode;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.DeletePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EpdPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.UpdatePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;

import java.util.Map;

/**
 * @author Dmytro Rud
 *
 * @since 3.5.1
 * @deprecated split into PPQ-1 and PPQ-2 in the Swiss EPR specification from March 2018.
 */
@Deprecated
@Slf4j
public class ChPpqAuditStrategy extends AuditStrategySupport<ChPpqAuditDataset> {

    public ChPpqAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public ChPpqAuditDataset createAuditDataset() {
        return new ChPpqAuditDataset(isServerSide());
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, ChPpqAuditDataset auditDataset) {
        EventType eventType;
        switch (auditDataset.getAction()) {
            case Execute:
                eventType = PpqEventTypeCodes.PrivacyPolicyQueryPolicyQuery;
                break;
            case Create:
                eventType = PpqEventTypeCodes.PrivacyPolicyQueryAddPolicy;
                break;
            case Update:
                eventType = PpqEventTypeCodes.PrivacyPolicyQueryUpdatePolicy;
                break;
            case Delete:
                eventType = PpqEventTypeCodes.PrivacyPolicyQueryDeletePolicy;
                break;
            default:
                throw new RuntimeException("Cannot handle event action code " + auditDataset.getAction());
        }

        String queryMessage = StringUtils.join(auditDataset.getPolicyAndPolicySetIds(), ',');
        if (StringUtils.isEmpty(queryMessage)) {
            queryMessage = StringUtils.trimToEmpty(auditDataset.getPatientId());
        }

        String queryId = StringUtils.isEmpty(auditDataset.getQueryId()) ? "unknown" : auditDataset.getQueryId();

        return new QueryInformationBuilder(auditContext, auditDataset, eventType, auditDataset.getPurposesOfUse())
                .addPatients(auditDataset.getPatientId())
                .setQueryParameters(queryId, ParticipantObjectIdType.of(eventType), queryMessage)
                .getMessages();
    }

    @Override
    public ChPpqAuditDataset enrichAuditDatasetFromRequest(ChPpqAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        if (request instanceof XACMLPolicyQueryType) {
            auditDataset.setAction(EventActionCode.Execute);
            enrichAuditDatasetFromRequest(auditDataset, (XACMLPolicyQueryType) request);
        } else if (request instanceof AddPolicyRequest) {
            auditDataset.setAction(EventActionCode.Create);
            Xacml20Utils.toStream((AddPolicyRequest) request).forEach(policy -> auditDataset.getPolicyAndPolicySetIds().add(policy.getId().toString()));
        } else if (request instanceof UpdatePolicyRequest) {
            auditDataset.setAction(EventActionCode.Update);
            Xacml20Utils.toStream((UpdatePolicyRequest) request).forEach(policy -> auditDataset.getPolicyAndPolicySetIds().add(policy.getId().toString()));
        } else if (request instanceof DeletePolicyRequest) {
            auditDataset.setAction(EventActionCode.Delete);
            Xacml20Utils.toStream((DeletePolicyRequest) request).forEach(id -> auditDataset.getPolicyAndPolicySetIds().add(id.getValue()));
        }
        return auditDataset;
    }

    private static void enrichAuditDatasetFromRequest(ChPpqAuditDataset auditDataset, XACMLPolicyQueryType request) {
        auditDataset.setQueryId(request.getID());
        Xacml20Utils.extractPatientId(request).ifPresent(auditDataset::setPatientId);
        Xacml20Utils.extractPolicyId(request).ifPresent(id -> auditDataset.getPolicyAndPolicySetIds().add(id.getValue()));
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(ChPpqAuditDataset auditDataset, Object response, AuditContext auditContext) {
        auditDataset.setEventOutcomeIndicator(getEventOutcomeIndicator(response));
        return true;
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(Object response) {
        if (response instanceof ResponseType) {
            return getEventOutcomeIndicator((ResponseType) response);
        } else if (response instanceof EpdPolicyRepositoryResponse) {
            return getEventOutcomeIndicator((EpdPolicyRepositoryResponse) response);
        } else {
            log.error("Don't know how to handle {}", ClassUtils.getSimpleName(response, "<null>"));
            return EventOutcomeIndicator.MajorFailure;
        }
    }

    private static EventOutcomeIndicator getEventOutcomeIndicator(ResponseType response) {
        try {
            if (!Xacml20Utils.SAML20_STATUS_SUCCESS.equals(response.getStatus().getStatusCode().getValue())) {
                return EventOutcomeIndicator.SeriousFailure;
            }
            if (response.getAssertionOrEncryptedAssertion().isEmpty()) {
                return EventOutcomeIndicator.MinorFailure;
            }
            return EventOutcomeIndicator.Success;
        } catch (Exception e) {
            return EventOutcomeIndicator.MajorFailure;
        }
    }

    private static EventOutcomeIndicator getEventOutcomeIndicator(EpdPolicyRepositoryResponse response) {
        return StatusCode.SUCCESS.equals(response.getStatus()) ? EventOutcomeIndicator.Success : EventOutcomeIndicator.SeriousFailure;
    }

}
