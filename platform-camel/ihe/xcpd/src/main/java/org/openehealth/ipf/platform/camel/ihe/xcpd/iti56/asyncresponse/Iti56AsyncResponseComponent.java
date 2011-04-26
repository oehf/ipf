/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti56.asyncresponse;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.xcpd.iti56.asyncresponse.Iti56AsyncResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the ITI-56 (XCPD) async response.
 */
public class Iti56AsyncResponseComponent extends AbstractWsComponent<Hl7v3ServiceInfo> {
    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    private final static Hl7v3ServiceInfo WS_CONFIG = new Hl7v3ServiceInfo(
            new QName(NS_URI, "RespondingGateway_Response_Service", "xcpd"),
            Iti56AsyncResponsePortType.class,
            new QName(NS_URI, "RespondingGateway_Response_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti56/iti56-asyncresponse-raw.wsdl",
            null,
            null,
            null,
            false,
            false);


    @SuppressWarnings("unchecked") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Iti56AsyncResponseEndpoint(uri, remaining, this, getCustomInterceptors(parameters));
    }

    @Override
    public Hl7v3ServiceInfo getWebServiceConfiguration() {
        return WS_CONFIG;
    }
}
