/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.custom;

import lombok.Setter;
import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpointConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Component for custom MLLP components. The HL7v2 configuration as well as the ATNA audit strategies must be
 * provided as endpoint parameters "hl7TransactionConfig", "clientAuditStrategy", and "serverAuditStrategy".
 *
 * @author Christian Ohr
 */
public class CustomMllpComponent<AuditDatasetType extends MllpAuditDataset> extends MllpTransactionComponent<AuditDatasetType> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomMllpComponent.class);

    @Setter
    private Hl7v2TransactionConfiguration<AuditDatasetType> transactionConfiguration;

    public CustomMllpComponent() {
        super(null);
    }

    public CustomMllpComponent(CamelContext camelContext) {
        super(camelContext, null);
    }

    @Override
    protected MllpTransactionEndpointConfiguration createConfig(String uri, Map<String, Object> parameters) throws Exception {
        MllpTransactionEndpointConfiguration transactionConfig = super.createConfig(uri, parameters);
        Hl7v2TransactionConfiguration configuration = resolveAndRemoveReferenceParameter(parameters, "hl7TransactionConfig", Hl7v2TransactionConfiguration.class);
        if (this.transactionConfiguration == null) {
            if (configuration == null) {
                throw new IllegalArgumentException("Must provide hl7TransactionConfig attribute with custom MLLP component");
            } else {
                this.transactionConfiguration = configuration;
            }
        } else {
            if (configuration != null) {
                throw new IllegalArgumentException("Must not override preconfigured hl7TransactionConfig in custom MLLP component");
            }
        }
        if (transactionConfig.isAudit() && getClientAuditStrategy() == null) {
            throw new IllegalArgumentException("Consumer or Producer require ATNA audit, but no clientAuditStrategy is defined for custom MLLP component");
        }
        if (transactionConfig.isAudit() && getServerAuditStrategy() == null) {
            throw new IllegalArgumentException("Consumer or Producer require ATNA audit, but no serverAuditStrategy is defined for custom MLLP component");
        }
        return transactionConfig;
    }

    @Override
    public Hl7v2TransactionConfiguration<AuditDatasetType> getHl7v2TransactionConfiguration() {
        return transactionConfiguration;
    }

    @Override
    public AuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return transactionConfiguration.getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return transactionConfiguration.getServerAuditStrategy();
    }

    @Override
    public NakFactory<AuditDatasetType> getNakFactory() {
        return new NakFactory(transactionConfiguration);
    }

}
