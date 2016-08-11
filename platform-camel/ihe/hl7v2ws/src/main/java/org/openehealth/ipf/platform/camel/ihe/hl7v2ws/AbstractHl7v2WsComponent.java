/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v2ws.Hl7v2WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

/**
 * @author Dmytro Rud
 */
abstract public class AbstractHl7v2WsComponent<AuditDatasetType extends WsAuditDataset>
        extends AbstractWsComponent<AuditDatasetType, WsTransactionConfiguration>
        implements Hl7v2ConfigurationHolder {

    private final Hl7v2WsInteractionId interactionId;

    public AbstractHl7v2WsComponent(Hl7v2WsInteractionId interactionId) {
        this.interactionId = interactionId;
    }

    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return interactionId.getHl7v2TransactionConfiguration();
    }

    @Override
    public NakFactory getNakFactory() {
        return interactionId.getNakFactory();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return interactionId.getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return interactionId.getServerAuditStrategy();
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return interactionId.getWsTransactionConfiguration();
    }
}
