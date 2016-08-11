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

package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3InteractionId;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

/**
 * Base class for all Camel components implementing IHE HL7v3 transactions
 *
 * @param <ConfigType> configuration type
 * @since 3.1
 */
public abstract class Hl7v3Component<ConfigType extends Hl7v3WsTransactionConfiguration>
        extends AbstractWsComponent<Hl7v3AuditDataset, ConfigType> {

    private final Hl7v3InteractionId interactionId;

    public Hl7v3Component(Hl7v3InteractionId interactionId) {
        super();
        this.interactionId = interactionId;
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getClientAuditStrategy() {
        return interactionId.getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getServerAuditStrategy() {
        return interactionId.getServerAuditStrategy();
    }

    @Override
    public ConfigType getWsTransactionConfiguration() {
        return interactionId.getWsTransactionConfiguration();
    }
}
