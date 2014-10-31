/*
 * Copyright 2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws.pcd01;

import javax.xml.namespace.QName;

import ca.uhn.hl7v2.ErrorCode;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.Pcd01PortType;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.gazelle.validation.profile.PcdTransactions;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v2ws.AbstractHl7v2WsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;


/**
 * Camel component for the IHE PCD-01 transaction.
 */
public class Pcd01Component extends AbstractHl7v2WsComponent {
    private static final String NS_URI = "urn:ihe:pcd:dec:2010";
    public static final WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
            new QName(NS_URI, "DeviceObservationConsumer_Service", "ihe"),
            Pcd01PortType.class,
            new QName(NS_URI, "DeviceObservationConsumer_Binding_Soap12", "ihe"),
            false,
            "wsdl/pcd01/pcd01.wsdl",
            true,
            false,
            false,
            false);

    public static final Hl7v2TransactionConfiguration HL7V2_CONFIG = new Hl7v2TransactionConfiguration(
            "2.6",
            "PCD01",
            "IPF",
            ErrorCode.APPLICATION_INTERNAL_ERROR,
            ErrorCode.APPLICATION_INTERNAL_ERROR,
            new String[] {"ORU"},
            new String[] {"R01"},
            new String[] {"ACK"},
            new String[] {"*"},
            null,
            null,
            HapiContextFactory.createHapiContext(PcdTransactions.PCD1));

    private static final NakFactory NAK_FACTORY = new NakFactory(HL7V2_CONFIG, false, "ACK^R01^ACK");


    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return HL7V2_CONFIG;
    }

    @Override
    public NakFactory getNakFactory() {
        return NAK_FACTORY;
    }

    @Override
    public WsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public WsAuditStrategy getClientAuditStrategy() {
        return null;  // not defined for this transaction
    }

    @Override
    public WsAuditStrategy getServerAuditStrategy() {
        return null;   // not defined for this transaction
    }

    @Override
    public AbstractWebService getServiceInstance(AbstractWsEndpoint<?> endpoint) {
        return new Pcd01Service();
    }
}
