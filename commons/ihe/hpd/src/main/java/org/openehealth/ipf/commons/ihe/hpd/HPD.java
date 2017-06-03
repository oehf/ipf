/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.hpd.chpidd.ChPiddPortType;
import org.openehealth.ipf.commons.ihe.hpd.iti58.Iti58PortType;
import org.openehealth.ipf.commons.ihe.hpd.iti59.Iti59AuditStrategy;
import org.openehealth.ipf.commons.ihe.hpd.iti59.Iti59PortType;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmytro Rud
 */
public class HPD implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements WsInteractionId<WsTransactionConfiguration> {
        ITI_58(ITI_58_WS_CONFIG),
        ITI_59(ITI_59_WS_CONFIG),
        CH_PIDD(CH_PIDD_WS_CONFIG);

        @Getter private WsTransactionConfiguration wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration ITI_58_WS_CONFIG = new WsTransactionConfiguration(
            "hpd-iti58",
            "Provider Information Query",
            true,
            null, // audit trail is not defined for ITI-58
            null, // audit trail is not defined for ITI-58
            new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Service"),
            Iti58PortType.class,
            new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Binding"),
            false,
            "wsdl/iti58.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI_59_WS_CONFIG = new WsTransactionConfiguration(
            "hpd-iti59",
            "Provider Information Feed",
            true,
            new Iti59AuditStrategy(false),
            new Iti59AuditStrategy(true),
            new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Service"),
            Iti59PortType.class,
            new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Binding"),
            false,
            "wsdl/iti59.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration CH_PIDD_WS_CONFIG = new WsTransactionConfiguration(
            "ch-pidd",
            "Provider Information Delta Download (Swiss HPD extension)",
            true,
            null, // audit trail is not defined for CH-PIDD
            null, // audit trail is not defined for CH-PIDD
            new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Service"),
            ChPiddPortType.class,
            new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Binding"),
            false,
            "wsdl/ch-pidd.wsdl",
            true,
            false,
            false,
            false);

}
