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
import org.openehealth.ipf.commons.ihe.hpd.chcidd.ChCiddPortType;
import org.openehealth.ipf.commons.ihe.hpd.chciq.ChCiqPortType;
import org.openehealth.ipf.commons.ihe.hpd.chpidd.ChPiddPortType;
import org.openehealth.ipf.commons.ihe.hpd.iti58.Iti58PortType;
import org.openehealth.ipf.commons.ihe.hpd.iti59.*;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmytro Rud
 */
public class HPD implements IntegrationProfile {

    @AllArgsConstructor
    public enum ReadInteractions implements WsInteractionId<WsTransactionConfiguration<WsAuditDataset>> {
        ITI_58(ITI_58_WS_CONFIG),
        CH_PIDD(CH_PIDD_WS_CONFIG),
        CH_CIQ(CH_CIQ_WS_CONFIG),
        CH_CIDD(CH_CIDD_WS_CONFIG);

        @Getter
        private final WsTransactionConfiguration<WsAuditDataset> wsTransactionConfiguration;
    }

    @AllArgsConstructor
    public enum FeedInteractions implements WsInteractionId<WsTransactionConfiguration<Iti59AuditDataset>> {
        ITI_59(ITI_59_WS_CONFIG);

        @Getter
        private final WsTransactionConfiguration<Iti59AuditDataset> wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        List<InteractionId> interactions = new ArrayList<>();
        interactions.addAll(Arrays.asList(FeedInteractions.values()));
        interactions.addAll(Arrays.asList(ReadInteractions.values()));
        return Collections.unmodifiableList(interactions);
    }

    private final static WsTransactionConfiguration<WsAuditDataset> ITI_58_WS_CONFIG = new WsTransactionConfiguration<>(
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

    private final static WsTransactionConfiguration<Iti59AuditDataset> ITI_59_WS_CONFIG = new WsTransactionConfiguration<>(
            "hpd-iti59",
            "Provider Information Feed",
            true,
            new Iti59ClientAuditStrategy(),
            new Iti59ServerAuditStrategy(),
            new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Service"),
            Iti59PortType.class,
            new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Binding"),
            false,
            "wsdl/iti59.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration<WsAuditDataset> CH_PIDD_WS_CONFIG = new WsTransactionConfiguration<>(
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

    private final static WsTransactionConfiguration<WsAuditDataset> CH_CIQ_WS_CONFIG = new WsTransactionConfiguration<>(
            "ch-ciq",
            "Community Information Query (Swiss EPR transaction)",
            true,
            null, // audit trail is not defined for CH-CIQ
            null, // audit trail is not defined for CH-CIQ
            new QName("urn:ch:admin:bag:epr:cpi:2017", "CommunityPortalIndex"),
            ChCiqPortType.class,
            new QName("urn:ch:admin:bag:epr:cpi:2017", "WSHttpBinding_ICommunityPortalIndex"),
            false,
            "wsdl/ch-ciq.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration<WsAuditDataset> CH_CIDD_WS_CONFIG = new WsTransactionConfiguration<>(
            "ch-cidd",
            "Community Information Delta Download (Swiss EPR transaction)",
            true,
            null, // audit trail is not defined for CH-CIDD
            null, // audit trail is not defined for CH-CIDD
            new QName("urn:ch:admin:bag:epr:cpi:2017", "CommunityPortalIndex"),
            ChCiddPortType.class,
            new QName("urn:ch:admin:bag:epr:cpi:2017", "WSHttpBinding_ICommunityPortalIndex"),
            false,
            "wsdl/ch-cidd.wsdl",
            true,
            false,
            false,
            false);

}
