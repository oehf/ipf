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

package org.openehealth.ipf.commons.ihe.core.atna;

import lombok.AccessLevel;
import lombok.Getter;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;

import java.util.Map;

/**
 * @since 3.1
 */
public abstract class AuditStrategySupport<AuditDatasetType extends AuditDataset> implements AuditStrategy<AuditDatasetType> {

    @Getter(AccessLevel.PROTECTED)
    private final boolean serverSide;


    /**
     * @param serverSide <code>true</code> when this strategy is a server-side one;
     *                   <code>false</code> otherwise.
     */
    protected AuditStrategySupport(boolean serverSide) {
        this.serverSide = serverSide;
    }


    /**
     * Builds the audit message and hands it to the audit context.
     * <p>
     * Nothing that goes wrong on the way out of here reaches the caller. Auditing runs in the
     * {@code finally} of the interceptors, where a thrown exception would replace the outcome of the
     * transaction being audited -- so a malformed access token, a participant the builder cannot make
     * sense of, or any other defect in assembling the record would turn a served request into a server
     * error. Failures go to the configured {@link org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler}
     * instead, the same place a failed serialization or delivery ends up; deployments that want
     * auditing to be fatal can plug a rethrowing handler.
     *
     * @param auditContext audit context
     * @param auditDataset audit dataset
     */
    @Override
    public void doAudit(AuditContext auditContext, AuditDatasetType auditDataset) {
        try {
            auditContext.audit(makeAuditMessage(auditContext, auditDataset));
        } catch (Exception e) {
            auditContext.getAuditExceptionHandler().handleException(auditContext, e, null);
        }
    }

    /**
     * Constructs an {@link AuditMessage} from a provided {@link AuditDataset}
     *
     * @param auditContext audit context
     * @param auditDataset audit dataset
     * @return audit message
     */
    public abstract AuditMessage[] makeAuditMessage(AuditContext auditContext, AuditDatasetType auditDataset);


    @Override
    public AuditDatasetType enrichAuditDatasetFromRequest(AuditDatasetType auditDataset, Object request, Map<String, Object> parameters) {
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(AuditDatasetType auditDataset, Object response, AuditContext auditContext) {
        return true;
    }

    @Override
    public boolean isAuditableResponse(Object response) {
        return true;
    }

}
