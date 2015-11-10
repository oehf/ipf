/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti47;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationAwareWsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v3.iti47.Iti47AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti47.Iti47PortType;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Component;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3ContinuationAwareEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the ITI-47 transaction (PDQ v3).
 */
public class Iti47Component extends Hl7v3Component<Hl7v3ContinuationAwareWsTransactionConfiguration> {

    private final static String NS_URI = "urn:ihe:iti:pdqv3:2007";
    public final static Hl7v3ContinuationAwareWsTransactionConfiguration WS_CONFIG = new Hl7v3ContinuationAwareWsTransactionConfiguration(
            IpfInteractionId.ITI_47,
            new QName(NS_URI, "PDSupplier_Service", "ihe"),
            Iti47PortType.class,
            new QName(NS_URI, "PDSupplier_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti47/iti47-raw.wsdl",
            "PRPA_IN201306UV02",
            "PRPA_TE201306UV02",
            false,
            false,
            "PRPA_IN201305UV02",
            "PRPA_IN201306UV02");

    @SuppressWarnings("unchecked") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Hl7v3ContinuationAwareEndpoint(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters)) {

            @Override
            protected <T extends AbstractWebService> T getCustomServiceInstance(AbstractWsEndpoint<Hl7v3AuditDataset, Hl7v3ContinuationAwareWsTransactionConfiguration> endpoint) {
                Hl7v3ContinuationAwareEndpoint endpoint2 = (Hl7v3ContinuationAwareEndpoint) endpoint;
                return endpoint2.isSupportContinuation() ?
                        (T)new Iti47ContinuationAwareService(endpoint2) :
                        (T)new Iti47Service();
            }

        };
    }

    @Override
    public Hl7v3ContinuationAwareWsTransactionConfiguration getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getClientAuditStrategy() {
        return new Iti47AuditStrategy(false);
    }

    @Override
    public AuditStrategy<Hl7v3AuditDataset> getServerAuditStrategy() {
        return new Iti47AuditStrategy(true);
    }


}
