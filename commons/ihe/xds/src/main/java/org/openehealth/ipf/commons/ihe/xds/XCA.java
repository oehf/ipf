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

package org.openehealth.ipf.commons.ihe.xds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRetrieveAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.iti38.Iti38ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti38.Iti38PortType;
import org.openehealth.ipf.commons.ihe.xds.iti38.Iti38ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti38.asyncresponse.Iti38AsyncResponsePortType;
import org.openehealth.ipf.commons.ihe.xds.iti39.Iti39ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti39.Iti39PortType;
import org.openehealth.ipf.commons.ihe.xds.iti39.Iti39ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti39.asyncresponse.Iti39AsyncResponsePortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XCA implements XdsInteractionProfile {

    private static final XCA Instance = new XCA();

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {

        ITI_38("xca-iti38",
                "Cross Gateway Query",
                true,
                ITI38_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsQueryAuditDataset> getClientAuditStrategy() {
                return Iti38ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsQueryAuditDataset> getServerAuditStrategy() {
                return Iti38ServerAuditStrategy.getInstance();
            }
        },
        ITI_38_ASYNC("xca-iti38-async-response",
                "Cross Gateway Query",
                true,
                ITI38_ASYNC_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsQueryAuditDataset> getClientAuditStrategy() {
                return null;
            }

            @Override
            public AuditStrategy<XdsQueryAuditDataset> getServerAuditStrategy() {
                return Iti38ClientAuditStrategy.getInstance(); // really!
            }
        },
        ITI_39("xca-iti39",
                "Cross Gateway Retrieve",
                false,
                ITI39_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsRetrieveAuditDataset> getClientAuditStrategy() {
                return Iti39ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsRetrieveAuditDataset> getServerAuditStrategy() {
                return Iti39ServerAuditStrategy.getInstance();
            }
        },
        ITI_39_ASYNC("xca-iti39-async-response",
                "Cross Gateway Retrieve",
                false,
                ITI39_ASYNC_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsRetrieveAuditDataset> getClientAuditStrategy() {
                return null;
            }

            @Override
            public AuditStrategy<XdsRetrieveAuditDataset> getServerAuditStrategy() {
                return Iti39ClientAuditStrategy.getInstance(); // really!
            }
        };

        @Getter private String name;
        @Getter private String description;
        @Getter private boolean query;
        @Getter private WsTransactionConfiguration wsTransactionConfiguration;

        @Override
        public XdsInteractionProfile getInteractionProfile() {
            return Instance;
        }

    }

    @Override
    public boolean isEbXml30Based() {
        return true;
    }

    @Override
    public boolean requiresHomeCommunityId() {
        return true;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration ITI38_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Service", "ihe"),
            Iti38PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti38.wsdl",
            true,
            false,
            true,
            true);

    private final static WsTransactionConfiguration ITI38_ASYNC_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Service", "ihe"),
            Iti38AsyncResponsePortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Binding", "ihe"),
            false,
            "wsdl/iti38-asyncresponse.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI39_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Service", "ihe"),
            Iti39PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti39.wsdl",
            true,
            false,
            false,
            true);

    private final static WsTransactionConfiguration ITI39_ASYNC_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Service", "ihe"),
            Iti39AsyncResponsePortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Binding", "ihe"),
            false,
            "wsdl/iti39-asyncresponse.wsdl",
            true,
            false,
            false,
            false);
}
