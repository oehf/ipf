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

import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants.StatusCode;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.DeletePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.UpdatePolicyRequest;

import java.util.Map;

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
        if (request instanceof AddPolicyRequest) {
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

    @Override
    public boolean enrichAuditDatasetFromResponse(ChPpqAuditDataset auditDataset, Object response, AuditContext auditContext) {
        auditDataset.setEventOutcomeIndicator(getEventOutcomeIndicator(response));
        return true;
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(Object responseObject) {
        EprPolicyRepositoryResponse response = (EprPolicyRepositoryResponse) responseObject;
        return StatusCode.SUCCESS.equals(response.getStatus()) ? EventOutcomeIndicator.Success : EventOutcomeIndicator.SeriousFailure;
    }

}
