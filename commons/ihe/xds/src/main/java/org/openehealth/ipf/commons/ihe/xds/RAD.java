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
import org.openehealth.ipf.commons.ihe.xds.rad69.Rad69AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad69.Rad69PortType;
import org.openehealth.ipf.commons.ihe.xds.rad75.Rad75AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.rad75.Rad75PortType;
import org.openehealth.ipf.commons.ihe.xds.rad75.asyncresponse.Rad75AsyncResponsePortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class RAD implements XdsIntegrationProfile {

    private static final RAD Instance = new RAD();

    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {
        RAD_69(RAD_69_WS_CONFIG),
        RAD_75(RAD_75_WS_CONFIG),
        RAD_75_ASYNC_RESPONSE(RAD_75_ASYNC_RESPONSE_WS_CONFIG);

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

    private final static WsTransactionConfiguration RAD_69_WS_CONFIG = new WsTransactionConfiguration(
            "xdsi-rad69",
            "Retrieve Imaging Document Set",
            false,
            new Rad69AuditStrategy(false),
            new Rad69AuditStrategy(true),
            new QName("urn:ihe:rad:xdsi-b:2009", "DocumentRepository_Service", "iherad"),
            Rad69PortType.class,
            new QName("urn:ihe:rad:xdsi-b:2009", "DocumentRepository_Binding_Soap12", "iherad"),
            true,
            "wsdl/rad69.wsdl",
            true,
            false,
            false,
            false);

    private final static WsTransactionConfiguration RAD_75_WS_CONFIG = new WsTransactionConfiguration(
            "xcai-rad75",
            "Cross Gateway Retrieve Imaging Document Set",
            false,
            new Rad75AuditStrategy(false),
            new Rad75AuditStrategy(true),
            new QName("urn:ihe:rad:xdsi-b:2009", "RespondingGateway_Service", "iherad"),
            Rad75PortType.class,
            new QName("urn:ihe:rad:xdsi-b:2009", "RespondingGateway_Binding_Soap12", "iherad"),
            true,
            "wsdl/rad75.wsdl",
            true,
            false,
            false,
            true);

    private final static WsTransactionConfiguration RAD_75_ASYNC_RESPONSE_WS_CONFIG = new WsTransactionConfiguration(
            "xcai-rad75-async-response",
            "Cross Gateway Retrieve Imaging Document Set",
            false,
            null,
            new Rad75AuditStrategy(false),      // really!
            new QName("urn:ihe:rad:xdsi-b:2009", "InitiatingGateway_Service", "iherad"),
            Rad75AsyncResponsePortType.class,
            new QName("urn:ihe:rad:xdsi-b:2009", "InitiatingGateway_Binding", "iherad"),
            false,
            "wsdl/rad75-asyncresponse.wsdl",
            true,
            false,
            false,
            false);
}
