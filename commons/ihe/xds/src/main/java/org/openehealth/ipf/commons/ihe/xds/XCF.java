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
import org.openehealth.ipf.commons.ihe.xds.iti63.Iti63ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti63.Iti63PortType;
import org.openehealth.ipf.commons.ihe.xds.iti63.Iti63ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti63.asyncresponse.Iti63AsyncResponsePortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XCF implements XdsInteractionProfile {

    private static final XCF Instance = new XCF();

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {

        ITI_63("xcf-iti63",
                "Cross Gateway Fetch",
                true,
                ITI63_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsQueryAuditDataset> getClientAuditStrategy() {
                return Iti63ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<XdsQueryAuditDataset> getServerAuditStrategy() {
                return Iti63ServerAuditStrategy.getInstance();
            }
        },
        ITI_63_ASYNC("xcf-iti63-async-response",
                "Cross Gateway Fetch",
                true,
                ITI63_ASYNC_WS_CONFIG) {
            @Override
            public AuditStrategy<XdsQueryAuditDataset> getServerAuditStrategy() {
                return Iti63ClientAuditStrategy.getInstance(); // really!
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

    private final static WsTransactionConfiguration ITI63_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Service", "ihe"),
            Iti63PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti63.wsdl",
            true,
            false,
            true,
            true);

    private final static WsTransactionConfiguration ITI63_ASYNC_WS_CONFIG = new WsTransactionConfiguration(
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Service", "ihe"),
            Iti63AsyncResponsePortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Binding", "ihe"),
            false,
            "wsdl/iti63-asyncresponse.wsdl",
            true,
            false,
            false,
            false);
}
