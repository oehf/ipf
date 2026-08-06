/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.hl7v2ws;

import org.openehealth.ipf.commons.ihe.core.TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2InteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public interface Hl7v2WsInteractionId extends Hl7v2InteractionId, WsInteractionId {

    /**
     * Transactions of this kind are the only ones with two transaction configurations, an HL7v2 one
     * for the message semantics and a Web Service one for the transport, so both parent interfaces
     * offer a way to answer this and the ambiguity has to be resolved explicitly. The Web Service
     * configuration is returned because it is the one describing the endpoint, which is what generic
     * callers such as telemetry are after; the HL7v2 configuration remains available through
     * {@link #getHl7v2TransactionConfiguration()}.
     */
    @Override
    default TransactionConfiguration getTransactionConfiguration() {
        return getWsTransactionConfiguration();
    }
}
