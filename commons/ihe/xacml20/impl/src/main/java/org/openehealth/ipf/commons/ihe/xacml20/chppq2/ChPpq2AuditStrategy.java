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
package org.openehealth.ipf.commons.ihe.xacml20.chppq2;

import static org.openehealth.ipf.commons.ihe.core.atna.event.IHEAuditMessageBuilder.QUERY_ENCODING;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.audit.codes.PpqEventTypeCodes;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;

/**
 * @author Dmytro Rud
 * @since 3.5.1
 */
@Slf4j
public class ChPpq2AuditStrategy extends AuditStrategySupport<ChPpqAuditDataset> {

    public ChPpq2AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public ChPpqAuditDataset createAuditDataset() {
        return new ChPpqAuditDataset(isServerSide());
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, ChPpqAuditDataset auditDataset) {
        var builder = new QueryInformationBuilder(auditContext, auditDataset, PpqEventTypeCodes.PrivacyPolicyRetrieve, auditDataset.getPurposesOfUse());
        return builder
                .addPatients(auditDataset.getPatientId())
                .setQueryParameters(
                        auditDataset.getQueryId(),
                        ParticipantObjectIdType.of(PpqEventTypeCodes.PrivacyPolicyRetrieve),
                        auditDataset.getRequestPayload(),
                        StringUtils.isNotEmpty(auditDataset.getRequestPayload()) ?
                                Collections.singletonList(builder.getTypeValuePair(QUERY_ENCODING, Charset.defaultCharset().toString())) :
                                Collections.emptyList())
                .getMessages();
    }

    @Override
    public ChPpqAuditDataset enrichAuditDatasetFromRequest(ChPpqAuditDataset auditDataset, Object requestObject, Map<String, Object> parameters) {
        var request = (XACMLPolicyQueryType) requestObject;
        auditDataset.setAction(EventActionCode.Execute);
        auditDataset.setQueryId(request.getID());
        Xacml20Utils.extractPatientId(request).ifPresent(auditDataset::setPatientId);
        Xacml20Utils.extractPolicyId(request).ifPresent(id -> auditDataset.getPolicyAndPolicySetIds().add(id.getValue()));
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(ChPpqAuditDataset auditDataset, Object response, AuditContext auditContext) {
        auditDataset.setEventOutcomeIndicator(getEventOutcomeIndicator(response));
        return true;
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(Object responseObject) {
        var response = (ResponseType) responseObject;
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

}
