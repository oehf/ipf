/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.chadr;

import lombok.extern.slf4j.Slf4j;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20AuditUtils;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Status;
import org.openehealth.ipf.commons.ihe.xacml20.audit.codes.Xacml20EventTypeCodes;
import org.openehealth.ipf.commons.ihe.xacml20.audit.codes.Xacml20ParticipantIdType;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.XACMLAuthzDecisionStatementType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
@Slf4j
public class ChAdrAuditStrategy extends AuditStrategySupport<ChAdrAuditDataset> {

    public ChAdrAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public ChAdrAuditDataset createAuditDataset() {
        return new ChAdrAuditDataset(isServerSide());
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, ChAdrAuditDataset auditDataset) {
        var builder = new QueryInformationBuilder<>(auditContext, auditDataset, Xacml20EventTypeCodes.AuthorizationDecisionsQueryAdr, auditDataset.getPurposesOfUse());
        return builder
            .setQueryParameters(
                auditDataset.getSubjectId(),
                auditDataset.getSubjectRole(),
                null,
                ParticipantObjectTypeCode.Person,
                ParticipantObjectTypeCodeRole.SecurityUserEntity,
                Collections.emptyList())
            .setQueryParameters(
                auditDataset.getResourceId(),
                Xacml20ParticipantIdType.AuthorizationDecisionsQueryAdr,
                null,
                ParticipantObjectTypeCode.System,
                auditDataset.getObjectRole(),
                (auditDataset.getDecision() != null)
                    ? Collections.singletonList(new TypeValuePairType("decision", auditDataset.getDecision()))
                    : Collections.emptyList())
            .getMessages();
    }

    @Override
    public ChAdrAuditDataset enrichAuditDatasetFromRequest(ChAdrAuditDataset auditDataset, Object requestObject, Map<String, Object> parameters) {
        var query = (XACMLAuthzDecisionQueryType) requestObject;
        for (AttributeType attribute : Xacml20AuditUtils.extractSubjectAttributes(query)) {
            switch (attribute.getAttributeId()) {
                case PpqConstants.AttributeIds.XACML_1_0_SUBJECT_ID:
                    auditDataset.setSubjectId(Xacml20AuditUtils.extractStringAttributeValue(attribute));
                    break;
                case PpqConstants.AttributeIds.XACML_2_0_SUBJECT_ROLE:
                    auditDataset.setSubjectRole(Xacml20AuditUtils.extractCodeAttributeValue(attribute));
                    break;
            }
        }
        for (AttributeType attribute : Xacml20AuditUtils.extractResourceAttributes(query)) {
            if (PpqConstants.AttributeIds.XACML_1_0_RESOURCE_ID.equals(attribute.getAttributeId())) {
                auditDataset.setResourceId(Xacml20AuditUtils.extractStringAttributeValue(attribute));
                break;
            }
        }
        for (AttributeType attribute : Xacml20AuditUtils.extractActionAttributes(query)) {
            if (PpqConstants.AttributeIds.XACML_1_0_ACTION_ID.equals(attribute.getAttributeId())) {
                String action = Xacml20AuditUtils.extractStringAttributeValue(attribute);
                if (action != null) {
                    switch (action) {
                        case PpqConstants.ActionIds.ITI_18:
                        case PpqConstants.ActionIds.ITI_42:
                        case PpqConstants.ActionIds.ITI_57:
                        case PpqConstants.ActionIds.ITI_92:
                            auditDataset.setObjectRole(ParticipantObjectTypeCodeRole.Report);
                            break;
                        case PpqConstants.ActionIds.PPQ_1_ADD:
                        case PpqConstants.ActionIds.PPQ_1_UPDATE:
                        case PpqConstants.ActionIds.PPQ_1_DELETE:
                        case PpqConstants.ActionIds.PPQ_2:
                            auditDataset.setObjectRole(ParticipantObjectTypeCodeRole.SecurityResource);
                            break;
                        case PpqConstants.ActionIds.ITI_81:
                            auditDataset.setObjectRole(ParticipantObjectTypeCodeRole.DataRepository);
                            break;
                    }
                    break;
                }
            }
        }
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(ChAdrAuditDataset auditDataset, Object responseObject, AuditContext auditContext) {
        try {
            var response = (ResponseType) responseObject;
            var statusCode = response.getStatus().getStatusCode().getValue();
            if (Xacml20Status.SUCCESS.getCode().equals(statusCode)) {
                auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
                var assertion = (AssertionType) response.getAssertionOrEncryptedAssertion().get(0);
                var statement = (XACMLAuthzDecisionStatementType) assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement().get(0);
                var decisions = statement.getResponse().getResults().stream()
                    .map(result -> result.getDecision().value())
                    .collect(Collectors.toSet());
                // the order matters, the first one who is present wins
                for (String decision : List.of("Deny", "Permit", "NotApplicable", "Indeterminate")) {
                    if (decisions.contains(decision)) {
                        auditDataset.setDecision(decision);
                        break;
                    }
                }
            } else {
                auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.SeriousFailure);
            }
        } catch (Exception e) {
            auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.MajorFailure);
        }
        return super.enrichAuditDatasetFromResponse(auditDataset, responseObject, auditContext);
    }

}
