/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xca.iti38.asyncresponse;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xca.iti38.asyncresponse.Iti38AsyncResponsePortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import java.util.Map;

/**
 * The Camel component for the ITI-38 (XCA) async response.
 */
public class Iti38AsyncResponseComponent extends AbstractWsComponent<ItiServiceInfo> {
    private final static ItiServiceInfo WS_CONFIG = new ItiServiceInfo(
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Response_Service", "ihe"),
            Iti38AsyncResponsePortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_Response_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti38-asyncresponse.wsdl",
            true,
            false,
            false);

    @SuppressWarnings("unchecked") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Iti38AsyncResponseEndpoint(uri, remaining, this, getCustomInterceptors(parameters));
    }

    @Override
    public ItiServiceInfo getWebServiceConfiguration() {
        return WS_CONFIG;
    }
}
