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
package org.openehealth.ipf.commons.ihe.xacml20;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqAuditStrategy;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqPortType;
import org.openehealth.ipf.commons.ihe.xacml20.chppq1.ChPpq1ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xacml20.chppq1.ChPpq1PortType;
import org.openehealth.ipf.commons.ihe.xacml20.chppq1.ChPpq1ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xacml20.chppq2.ChPpq2AuditStrategy;
import org.openehealth.ipf.commons.ihe.xacml20.chppq2.ChPpq2PortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Definitions for the Swiss profile "Privacy Policy Query" (PPQ).
 *
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class CH_PPQ implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements WsInteractionId<WsTransactionConfiguration<ChPpqAuditDataset>> {
        /**
         * @deprecated split into PPQ-1 and PPQ-2 in the Swiss EPR specification from March 2018.
         */
        @Deprecated
        CH_PPQ(CH_PPQ_WS_CONFIG),

        CH_PPQ_1(CH_PPQ_1_WS_CONFIG),
        CH_PPQ_2(CH_PPQ_2_WS_CONFIG);

        @Getter private WsTransactionConfiguration wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    /**
     * @deprecated split into PPQ-1 and PPQ-2 in the Swiss EPR specification from March 2018.
     */
    @Deprecated
    private final static WsTransactionConfiguration<ChPpqAuditDataset> CH_PPQ_WS_CONFIG = new WsTransactionConfiguration<>(
            "ch-ppq",
            "Privacy Policy Query",
            false,
            new ChPpqAuditStrategy(false),
            new ChPpqAuditStrategy(true),
            new QName("urn:ihe:iti:ppq:2016", "PolicyRepository_Service"),
            ChPpqPortType.class,
            new QName("urn:ihe:iti:ppq:2016", "PolicyRepository_Binding_Soap12"),
            false,
            "wsdl/ch-ppq.wsdl",
            true,
            false,
            true,
            false);

    private final static WsTransactionConfiguration<ChPpqAuditDataset> CH_PPQ_1_WS_CONFIG = new WsTransactionConfiguration<>(
            "ch-ppq1",
            "Privacy Policy Feed",
            false,
            new ChPpq1ClientAuditStrategy(),
            new ChPpq1ServerAuditStrategy(),
            new QName("urn:ihe:iti:ppq:2016", "PolicyRepository_Service"),
            ChPpq1PortType.class,
            new QName("urn:ihe:iti:ppq:2016", "PolicyRepository_Binding_Soap12"),
            false,
            "wsdl/ch-ppq-1.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration<ChPpqAuditDataset> CH_PPQ_2_WS_CONFIG = new WsTransactionConfiguration<>(
            "ch-ppq2",
            "Privacy Policy Query",
            false,
            new ChPpq2AuditStrategy(false),
            new ChPpq2AuditStrategy(true),
            new QName("urn:ihe:iti:ppq:2016", "PolicyRepository_Service"),
            ChPpq2PortType.class,
            new QName("urn:ihe:iti:ppq:2016", "PolicyRepository_Binding_Soap12"),
            false,
            "wsdl/ch-ppq-2.wsdl",
            true,
            false,
            true,
            false);

}
