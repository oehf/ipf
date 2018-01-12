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
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;

import java.util.Map;

/**
 * @since 3.1
 */
public abstract class AuditStrategySupport<T extends AuditDataset> implements AuditStrategy<T> {

    @Getter(AccessLevel.PROTECTED)
    private final boolean serverSide;


    /**
     * @param serverSide <code>true</code> when this strategy is a server-side one;
     *                   <code>false</code> otherwise.
     */
    protected AuditStrategySupport(boolean serverSide) {
        this.serverSide = serverSide;
    }


    @Override
    public void doAudit(AuditContext auditContext, T auditDataset) throws Exception {
        auditContext.audit(makeAuditMessage(auditContext, auditDataset));
    }

    /**
     * Constructs an {@link AuditMessage} from a provided {@link AuditDataset}
     *
     * @param auditContext audit context
     * @param auditDataset audit dataset
     * @return audit message
     */
    public abstract AuditMessage[] makeAuditMessage(AuditContext auditContext, T auditDataset);


    @Override
    public T enrichAuditDatasetFromRequest(T auditDataset, Object request, Map<String, Object> parameters) {
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(T auditDataset, Object response) {
        return true;
    }


    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(Object response) {
        return null;
    }

    @Override
    public String getEventOutcomeDescription(Object response) {
        return null;
    }

    @Override
    public boolean isAuditableResponse(Object response) {
        return true;
    }

}
