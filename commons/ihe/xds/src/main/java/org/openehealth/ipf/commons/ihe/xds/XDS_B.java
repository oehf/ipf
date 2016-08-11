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
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRemoveAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRetrieveAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42PortType;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43PortType;
import org.openehealth.ipf.commons.ihe.xds.iti43.Iti43ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti51.Iti51ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti51.Iti51PortType;
import org.openehealth.ipf.commons.ihe.xds.iti51.Iti51ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti57.Iti57ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti57.Iti57PortType;
import org.openehealth.ipf.commons.ihe.xds.iti57.Iti57ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti61.Iti61ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti61.Iti61PortType;
import org.openehealth.ipf.commons.ihe.xds.iti61.Iti61ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti62.Iti62ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti62.Iti62PortType;
import org.openehealth.ipf.commons.ihe.xds.iti62.Iti62ServerAuditStrategy;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XDS_B implements XdsInteractionProfile {

    private static final XDS_B Instance = new XDS_B();

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {

        ITI_18("xds-iti18",
                "Registry Stored Query",
                true,
                ITI18_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsQueryAuditDataset> getClientAuditStrategy() {
                return Iti18ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsQueryAuditDataset> getServerAuditStrategy() {
                return Iti18ServerAuditStrategy.getInstance();
            }
        },
        ITI_41("xds-iti41",
                "Provide and Register Document Set-b",
                false,
                ITI41_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getClientAuditStrategy() {
                return Iti41ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getServerAuditStrategy() {
                return Iti41ServerAuditStrategy.getInstance();
            }
        },
        ITI_42("xds-iti42",
                "Register Document Set-b",
                false,
                ITI42_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getClientAuditStrategy() {
                return Iti42ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getServerAuditStrategy() {
                return Iti42ServerAuditStrategy.getInstance();
            }
        },
        ITI_43("xds-iti43",
                "Retrieve Document Set",
                false,
                ITI43_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsRetrieveAuditDataset> getClientAuditStrategy() {
                return Iti43ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsRetrieveAuditDataset> getServerAuditStrategy() {
                return Iti43ServerAuditStrategy.getInstance();
            }
        },
        ITI_51("xds-iti51",
                "Multi-Patient Stored Query",
                true,
                ITI51_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsQueryAuditDataset> getClientAuditStrategy() {
                return Iti51ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsQueryAuditDataset> getServerAuditStrategy() {
                return Iti51ServerAuditStrategy.getInstance();
            }
        },
        ITI_57("xds-iti57",
                "XDS Metadata Update",
                false,
                ITI57_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getClientAuditStrategy() {
                return Iti57ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getServerAuditStrategy() {
                return Iti57ServerAuditStrategy.getInstance();
            }
        },
        ITI_61("xds-iti61",
                "Register On-Demand Document Entry",
                false,
                ITI61_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getClientAuditStrategy() {
                return Iti61ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getServerAuditStrategy() {
                return Iti61ServerAuditStrategy.getInstance();
            }
        },
        ITI_62("xds-iti62",
                "Delete Document Set",
                false,
                ITI62_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsRemoveAuditDataset> getClientAuditStrategy() {
                return Iti62ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsRemoveAuditDataset> getServerAuditStrategy() {
                return Iti62ServerAuditStrategy.getInstance();
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
        return false;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration ITI18_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti18PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti18.wsdl",
            true,
            false,
            true,
            false);

    private static WsTransactionConfiguration ITI41_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"),
            Iti41PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti41.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI42_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti42PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti42.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI43_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"),
            Iti43PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti43.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI51_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti51PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti51.wsdl",
            true,
            false,
            true,
            false);

    private final static WsTransactionConfiguration ITI57_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_Service", "ihe"),
            Iti57PortType.class,
            new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti57.wsdl",
            true,
            false,
            false,
            false);


    private final static WsTransactionConfiguration ITI61_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti61PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti61.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI62_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_Service", "ihe"),
            Iti62PortType.class,
            new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti62.wsdl",
            true,
            false,
            false,
            false);
}
