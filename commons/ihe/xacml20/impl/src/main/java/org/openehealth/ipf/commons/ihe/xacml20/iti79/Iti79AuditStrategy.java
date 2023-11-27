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
package org.openehealth.ipf.commons.ihe.xacml20.iti79;

import lombok.extern.slf4j.Slf4j;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Status;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.audit.codes.Xacml20EventTypeCodes;
import org.openehealth.ipf.commons.ihe.xacml20.audit.codes.Xacml20ParticipantIdType;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;

import java.util.Collections;
import java.util.Map;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
@Slf4j
public class Iti79AuditStrategy extends AuditStrategySupport<Iti79AuditDataset> {

    public Iti79AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti79AuditDataset createAuditDataset() {
        return new Iti79AuditDataset(isServerSide());
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, Iti79AuditDataset auditDataset) {
        var builder = new QueryInformationBuilder<>(auditContext, auditDataset, Xacml20EventTypeCodes.AuthorizationDecisionsQueryIhe, auditDataset.getPurposesOfUse());
        return builder
            .setQueryParameters(
                auditDataset.getRequesterId(),
                Xacml20ParticipantIdType.AuthorizationDecisionsQueryIhe,
                null,
                ParticipantObjectTypeCode.Person,
                ParticipantObjectTypeCodeRole.SecurityUserEntity,
                Collections.emptyList())
            .setQueryParameters(
                auditDataset.getQueryId(),
                Xacml20ParticipantIdType.AuthorizationDecisionsQueryIhe,
                auditDataset.getRequestPayload(),
                Collections.emptyList())
            .addSecurityResourceParticipantObject(
                Xacml20ParticipantIdType.AuthorizationDecisionsQueryIhe,
                auditDataset.getStatusCode())
            .getMessages();
    }


    @Override
    public Iti79AuditDataset enrichAuditDatasetFromRequest(Iti79AuditDataset auditDataset, Object requestObject, Map<String, Object> parameters) {
        var query = (XACMLAuthzDecisionQueryType) requestObject;
        var request = Xacml20Utils.extractAuthzRequest(query);
        for (AttributeType attribute : request.getSubjects().get(0).getAttributes()) {
            if (PpqConstants.AttributeIds.XACML_1_0_SUBJECT_ID.equals(attribute.getAttributeId())) {
                auditDataset.setRequesterId(Xacml20Utils.extractStringAttributeValue(attribute));
            }
        }
        auditDataset.setQueryId(query.getID());
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(Iti79AuditDataset auditDataset, Object responseObject, AuditContext auditContext) {
        try {
            var response = (ResponseType) responseObject;
            var statusCode = response.getStatus().getStatusCode().getValue();
            auditDataset.setStatusCode(statusCode);
            if (Xacml20Status.SUCCESS.getCode().equals(statusCode)) {
                auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
            } else {
                auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.SeriousFailure);
            }
        } catch (Exception e) {
            auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.MajorFailure);
        }
        return super.enrichAuditDatasetFromResponse(auditDataset, responseObject, auditContext);
    }

}
