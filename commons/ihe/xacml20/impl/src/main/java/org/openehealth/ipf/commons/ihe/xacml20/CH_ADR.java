/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xacml20;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xacml20.chadr.ChAdrAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.chadr.ChAdrAuditStrategy;
import org.openehealth.ipf.commons.ihe.xacml20.chadr.ChAdrPortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Definitions for the Swiss national profile "Authorization Decision Request" (CH:ADR).
 *
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class CH_ADR implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements WsInteractionId<WsTransactionConfiguration<ChAdrAuditDataset>> {
        CH_ADR(CH_ADR_WS_CONFIG);

        @Getter
        private final WsTransactionConfiguration<ChAdrAuditDataset> wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration<ChAdrAuditDataset> CH_ADR_WS_CONFIG = new WsTransactionConfiguration<>(
        "ch-adr",
        "Authorization Decisions Query",
        true,
        new ChAdrAuditStrategy(false),
        new ChAdrAuditStrategy(true),
        new QName("urn:ihe:iti:2014:ser", "AuthorizationDecisionsManager_Service"),
        ChAdrPortType.class,
        new QName("urn:ihe:iti:2014:ser", "AuthorizationDecisionsManager_Port_Soap12"),
        false,
        "wsdl/ch-adr.wsdl",
        true,
        false,
        false,
        false);


}
