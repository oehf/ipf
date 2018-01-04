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
package org.openehealth.ipf.platform.camel.ihe.hl7v2;

import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2InteractionId;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.MllpAuditDataset;

/**
 * Interface for Camel components which handle HL7v2 messages,
 * independent of transport protocol.
 *
 * @author Dmytro Rud
 */
public interface Hl7v2ConfigurationHolder<T extends MllpAuditDataset> {

    /**
     * Returns component configuration.
     */
    default Hl7v2TransactionConfiguration<T> getHl7v2TransactionConfiguration() {
        return getInteractionId().getHl7v2TransactionConfiguration();
    }

    /**
     * Returns transaction-specific NAK factory.
     */
    default NakFactory<T> getNakFactory() {
        return getInteractionId().getNakFactory();
    }

    Hl7v2InteractionId<T> getInteractionId();

}
