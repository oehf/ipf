/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mina2.Mina2Endpoint;

import java.util.Map;

/**
 * Camel component for MLLP-based eHealth transactions (like IHE PIX, PDQ, XAD-PID, etc.).
 * @author Dmytro Rud
 */
public abstract class MllpTransactionComponent<AuditDatasetType extends MllpAuditDataset>
        extends MllpComponent<MllpTransactionEndpointConfiguration>
{

    protected MllpTransactionComponent() {
        super();
    }

    protected MllpTransactionComponent(CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected MllpTransactionEndpointConfiguration createConfig(Map<String, Object> parameters) throws Exception {
        return new MllpTransactionEndpointConfiguration(this, parameters);
    }

    @Override
    protected MllpEndpoint createEndpoint(Mina2Endpoint wrappedEndpoint, MllpTransactionEndpointConfiguration config) {
        return new MllpTransactionEndpoint<>(this, wrappedEndpoint, config);
    }

    /**
     * Returns server-side ATNA audit strategy.
     */
    public abstract MllpAuditStrategy<AuditDatasetType> getServerAuditStrategy();

    /**
     * Returns client-side ATNA audit strategy.
     */
    public abstract MllpAuditStrategy<AuditDatasetType> getClientAuditStrategy();
}
