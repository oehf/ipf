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
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.iti38.Iti38AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.iti38.Iti38PortType;
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
public class XCA implements XdsIntegrationProfile {

    private static final XCA Instance = new XCA();

    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {
        ITI_38(ITI_38_WS_CONFIG),
        ITI_38_ASYNC_RESPONSE(ITI_38_ASYNC_RESPONSE_WS_CONFIG),
        ITI_39(ITI_39_WS_CONFIG),
        ITI_39_ASYNC_RESPONSE(ITI_39_ASYNC_RESPONSE_WS_CONFIG);

        @Getter
        private final WsTransactionConfiguration<? extends XdsAuditDataset> wsTransactionConfiguration;

        @Override
        public XdsIntegrationProfile getInteractionProfile() {
            return Instance;
        }
    }

    @Override
    public HomeCommunityIdOptionality getHomeCommunityIdOptionality() {
        return HomeCommunityIdOptionality.ON_MISSING_PATIENT_ID;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration<XdsQueryAuditDataset> ITI_38_WS_CONFIG = new WsTransactionConfiguration<>(
            "xca-iti38",
            "Cross Gateway Query",
            true,
            new Iti38AuditStrategy(false),
            new Iti38AuditStrategy(true),
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Service", "ihe"),
            Iti38PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti38.wsdl",
            true,
            false,
            true,
            true);

    private final static WsTransactionConfiguration<XdsQueryAuditDataset> ITI_38_ASYNC_RESPONSE_WS_CONFIG = new WsTransactionConfiguration<>(
            "xca-iti38-async-response",
            "Cross Gateway Query",
            true,
            null,
            new Iti38AuditStrategy(false),     // really!
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Service", "ihe"),
            Iti38AsyncResponsePortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "InitiatingGateway_Binding", "ihe"),
            false,
            "wsdl/iti38-asyncresponse.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration<XdsNonconstructiveDocumentSetRequestAuditDataset> ITI_39_WS_CONFIG = new WsTransactionConfiguration<>(
            "xca-iti39",
            "Cross Gateway Retrieve",
            false,
            new Iti39ClientAuditStrategy(),
            new Iti39ServerAuditStrategy(),
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Service", "ihe"),
            Iti39PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Binding_Soap12", "ihe"),
            true,
            "wsdl/iti39.wsdl",
            true,
            false,
            false,
            true);

    private final static WsTransactionConfiguration<XdsNonconstructiveDocumentSetRequestAuditDataset> ITI_39_ASYNC_RESPONSE_WS_CONFIG = new WsTransactionConfiguration<>(
            "xca-iti39-async-response",
            "Cross Gateway Retrieve",
            false,
            null,
            new Iti39ClientAuditStrategy(),     // really!
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
