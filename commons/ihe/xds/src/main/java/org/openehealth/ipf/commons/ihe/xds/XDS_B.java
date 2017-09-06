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
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
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
import org.openehealth.ipf.commons.ihe.xds.iti51.Iti51AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti51.Iti51PortType;
import org.openehealth.ipf.commons.ihe.xds.iti57.Iti57AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti57.Iti57PortType;
import org.openehealth.ipf.commons.ihe.xds.iti61.Iti61AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti61.Iti61PortType;
import org.openehealth.ipf.commons.ihe.xds.iti62.Iti62AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti62.Iti62PortType;
import org.openehealth.ipf.commons.ihe.xds.iti86.Iti86AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti86.Iti86PortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XDS_B implements XdsIntegrationProfile {

    private static final XDS_B Instance = new XDS_B();

    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {
        ITI_18(ITI_18_WS_CONFIG),
        ITI_41(ITI_41_WS_CONFIG),
        ITI_42(ITI_42_WS_CONFIG),
        ITI_43(ITI_43_WS_CONFIG),
        ITI_51(ITI_51_WS_CONFIG),
        ITI_57(ITI_57_WS_CONFIG),
        ITI_61(ITI_61_WS_CONFIG),
        ITI_62(ITI_62_WS_CONFIG),
        ITI_86(ITI_86_WS_CONFIG);

        @Getter private WsTransactionConfiguration wsTransactionConfiguration;

        @Override
        public XdsIntegrationProfile getInteractionProfile() {
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

    private final static WsTransactionConfiguration ITI_18_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti18",
            "Registry Stored Query",
            true,
            new Iti18ClientAuditStrategy(),
            new Iti18ServerAuditStrategy(),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti18PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti18.wsdl",
            true,
            false,
            true,
            false);

    private final static WsTransactionConfiguration ITI_41_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti41",
            "Provide and Register Document Set-b",
            false,
            new Iti41ClientAuditStrategy(),
            new Iti41ServerAuditStrategy(),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"),
            Iti41PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti41.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI_42_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti42",
            "Register Document Set-b",
            false,
            new Iti42ClientAuditStrategy(),
            new Iti42ServerAuditStrategy(),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti42PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti42.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI_43_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti43",
            "Retrieve Document Set",
            false,
            new Iti43ClientAuditStrategy(),
            new Iti43ServerAuditStrategy(),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"),
            Iti43PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti43.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI_51_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti51",
            "Multi-Patient Stored Query",
            true,
            new Iti51AuditStrategy(false),
            new Iti51AuditStrategy(true),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti51PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti51.wsdl",
            true,
            false,
            true,
            false);

    private final static WsTransactionConfiguration ITI_57_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti57",
            "XDS Metadata Update",
            false,
            new Iti57AuditStrategy(false),
            new Iti57AuditStrategy(true),
            new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_Service", "ihe"),
            Iti57PortType.class,
            new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti57.wsdl",
            true,
            false,
            false,
            false);


    private final static WsTransactionConfiguration ITI_61_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti61",
            "Register On-Demand Document Entry",
            false,
            new Iti61AuditStrategy(false),
            new Iti61AuditStrategy(true),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti61PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti61.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI_62_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti62",
            "Delete Document Set",
            false,
            new Iti62AuditStrategy(false),
            new Iti62AuditStrategy(true),
            new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_Service", "ihe"),
            Iti62PortType.class,
            new QName("urn:ihe:iti:xds-b:2010", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti62.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI_86_WS_CONFIG = new WsTransactionConfiguration(
            "rmd-iti86",
            "Remove Documents",
            false,
            new Iti86AuditStrategy(false),
            new Iti86AuditStrategy(true),
            new QName("urn:ihe:iti:rmd:2017", "DocumentRepository_Service", "ihe"),
            Iti86PortType.class,
            new QName("urn:ihe:iti:rmd:2017", "DocumentRepository_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti86.wsdl",
            true,
            false,
            false,
            false);

}
