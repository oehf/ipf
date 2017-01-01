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
import org.openehealth.ipf.commons.ihe.xds.iti14.Iti14ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti14.Iti14PortType;
import org.openehealth.ipf.commons.ihe.xds.iti14.Iti14ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti15.Iti15ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti15.Iti15PortType;
import org.openehealth.ipf.commons.ihe.xds.iti15.Iti15ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti16.Iti16ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti16.Iti16PortType;
import org.openehealth.ipf.commons.ihe.xds.iti16.Iti16ServerAuditStrategy;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XDS_A implements XdsIntegrationProfile {

    private static final XDS_A Instance = new XDS_A();

    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {
        ITI_14(ITI_14_WS_CONFIG),
        ITI_15(ITI_15_WS_CONFIG),
        ITI_16(ITI_16_WS_CONFIG),
        ITI_17(ITI_17_WS_CONFIG);

        @Getter private WsTransactionConfiguration wsTransactionConfiguration;

        @Override
        public XdsIntegrationProfile getInteractionProfile() {
            return Instance;
        }
    }

    @Override
    public boolean isEbXml30Based() {
        return false;
    }

    @Override
    public boolean requiresHomeCommunityId() {
        return false;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration ITI_14_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti14",
            "Register Document Set",
            false,
            new Iti14ClientAuditStrategy(),
            new Iti14ServerAuditStrategy(),
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Service", "ihe"),
            Iti14PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Binding_Soap11", "ihe"),
            false,
            "wsdl/iti14.wsdl",
            false,
            false,
            false,
            false);

    private final static WsTransactionConfiguration ITI_15_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti15",
            "Provide and Register Document Set",
            false,
            new Iti15ClientAuditStrategy(),
            new Iti15ServerAuditStrategy(),
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Service", "ihe"),
            Iti15PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Binding_Soap11", "ihe"),
            false,
            "wsdl/iti15.wsdl",
            false,
            true,
            false,
            false);

    private final static WsTransactionConfiguration ITI_16_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti16",
            "Query Registry",
            true,
            new Iti16ClientAuditStrategy(),
            new Iti16ServerAuditStrategy(),
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Service", "ihe"),
            Iti16PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Binding_Soap11", "ihe"),
            false,
            "wsdl/iti16.wsdl",
            false,
            false,
            true,
            false);

    private final static WsTransactionConfiguration ITI_17_WS_CONFIG = new WsTransactionConfiguration(
            "xds-iti17",
            "Retrieve Documents",
            false,
            null,
            null,
            new QName("dummy", "dummy", "dummy"),
            Void.class,
            new QName("dummy", "dummy", "dummy"),
            false,
            "dummy",
            false,
            false,
            true,
            false);
}
