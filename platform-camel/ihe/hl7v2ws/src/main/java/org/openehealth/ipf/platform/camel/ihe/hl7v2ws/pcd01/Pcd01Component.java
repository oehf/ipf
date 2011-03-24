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

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.Pcd01PortType;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.modules.hl7.parser.PipeParser;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.pixpdq.BasicNakFactory;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultWsComponent;

import javax.xml.namespace.QName;
import java.util.Map;


/**
 * Camel component for the IHE PCD-01 transaction.
 */
public class Pcd01Component extends DefaultWsComponent {
    private static final String NS_URI = "urn:ihe:pcd:dec:2010";
    public static final ItiServiceInfo WS_CONFIG = new ItiServiceInfo(
            new QName(NS_URI, "DeviceObservationConsumer_Service", "ihe"),
            Pcd01PortType.class,
            new QName(NS_URI, "DeviceObservationConsumer_Binding_Soap12", "ihe"),
            false,
            "wsdl/pcd01/pcd01.wsdl",
            true,
            false,
            false);

    public static final MllpTransactionConfiguration HL7V2_CONFIG = new MllpTransactionConfiguration(
            "2.6",
            "PCD01",
            "IPF",
            207,
            207,
            new String[] {"ORU"},
            new String[] {"R01"},
            new String[] {"ACK"},
            new String[] {"*"},
            null,
            null,
            new PipeParser(),
            new BasicNakFactory());


    @Override
    @SuppressWarnings("unchecked") // Required because of base class
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Pcd01Endpoint(uri, remaining, this, getCustomInterceptors(parameters));
    }
    
}
