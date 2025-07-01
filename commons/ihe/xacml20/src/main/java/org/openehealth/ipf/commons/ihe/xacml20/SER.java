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
import org.openehealth.ipf.commons.ihe.xacml20.iti79.Iti79AuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.iti79.Iti79AuditStrategy;
import org.openehealth.ipf.commons.ihe.xacml20.iti79.Iti79PortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Definitions for the IHE integration profile "Secure Retrieve" (SeR).
 *
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class SER implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements WsInteractionId<WsTransactionConfiguration<Iti79AuditDataset>> {
        ITI_79(ITI_79_WS_CONFIG);

        @Getter
        private final WsTransactionConfiguration<Iti79AuditDataset> wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration<Iti79AuditDataset> ITI_79_WS_CONFIG = new WsTransactionConfiguration<>(
        "ser-iti79",
        "Authorization Decisions Query",
        true,
        new Iti79AuditStrategy(false),
        new Iti79AuditStrategy(true),
        new QName("urn:ihe:iti:2014:ser", "AuthorizationDecisionsManager_Service"),
        Iti79PortType.class,
        new QName("urn:ihe:iti:2014:ser", "AuthorizationDecisionsManager_Port_Soap12"),
        false,
        "wsdl/iti79.wsdl",
        true,
        false,
        true,
        false);

}
