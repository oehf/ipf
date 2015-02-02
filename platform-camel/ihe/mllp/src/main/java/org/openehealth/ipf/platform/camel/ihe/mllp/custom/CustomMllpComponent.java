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

import java.util.Map;

import lombok.Getter;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpointConfiguration;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * Component for custom MLLP components. The HL7v2 configuration as well as the ATNA audit strategies must be
 * provided as endpoint parameters "hl7TransactionConfig", "clientAuditStrategy", and "serverAuditStrategy".
 *
 * @author Christian Ohr
 */
public class CustomMllpComponent<AuditDatasetType extends MllpAuditDataset> extends MllpTransactionComponent<AuditDatasetType> {

    private Hl7v2TransactionConfiguration configuration;

    @Getter
    private MllpAuditStrategy<AuditDatasetType> clientAuditStrategy;
    @Getter
    private MllpAuditStrategy<AuditDatasetType> serverAuditStrategy;


    public CustomMllpComponent() {
        super();
    }

    public CustomMllpComponent(CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected MllpTransactionEndpointConfiguration createConfig(Map<String, Object> parameters) throws Exception {
        MllpTransactionEndpointConfiguration transactionConfig = super.createConfig(parameters);
        configuration = resolveAndRemoveReferenceParameter(parameters, "hl7TransactionConfig", Hl7v2TransactionConfiguration.class);
        if (configuration == null) {
            throw new BeanInitializationException("Must provide hl7TransactionConfig attribute with custom MLLP component");
        }
        clientAuditStrategy = resolveAndRemoveReferenceParameter(parameters, "clientAuditStrategy", MllpAuditStrategy.class);
        if (transactionConfig.isAudit() && clientAuditStrategy == null) {
            throw new BeanInitializationException("Consumer or Producer require ATNA audit, but no clientAuditStrategy is defined for custom MLLP component");
        }
        serverAuditStrategy = resolveAndRemoveReferenceParameter(parameters, "serverAuditStrategy", MllpAuditStrategy.class);
        if (transactionConfig.isAudit() && serverAuditStrategy == null) {
            throw new BeanInitializationException("Consumer or Producer require ATNA audit, but no serverAuditStrategy is defined for custom MLLP component");
        }
        return transactionConfig;
    }

    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return configuration;
    }

    @Override
    public NakFactory getNakFactory() {
        return new NakFactory(configuration);
    }
}
