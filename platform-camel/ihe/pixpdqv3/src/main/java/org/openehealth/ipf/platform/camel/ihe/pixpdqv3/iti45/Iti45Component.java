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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti45;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.hl7v3.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.pixpdqv3.iti45.Iti45PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import javax.xml.namespace.QName;

/**
 * The Camel component for the ITI-45 transaction (PIX v3).
 */
public class Iti45Component extends AbstractWsComponent<Hl7v3ServiceInfo> {
    private static final String NS_URI = "urn:ihe:iti:pixv3:2007";
    public static final Hl7v3ServiceInfo WS_CONFIG = new Hl7v3ServiceInfo(
            IpfInteractionId.ITI_45,
            new QName(NS_URI, "PIXManager_Service", "ihe"),
            Iti45PortType.class,
            new QName(NS_URI, "PIXManager_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti45/iti45-raw.wsdl",
            "PRPA_IN201310UV02",
            true,
            false);

    @SuppressWarnings("unchecked") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Iti45Endpoint(uri, remaining, this, getCustomInterceptors(parameters));
    }

    @Override
    public Hl7v3ServiceInfo getWebServiceConfiguration() {
        return WS_CONFIG;
    }
}
