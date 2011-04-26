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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14.component;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.iti14.Iti14PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import javax.xml.namespace.QName;

/**
 * The Camel component for the ITI-14 transaction.
 */
public class Iti14Component extends AbstractWsComponent<ItiServiceInfo> {
    private final static ItiServiceInfo WS_CONFIG = new ItiServiceInfo(
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Service", "ihe"),
            Iti14PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Binding_Soap11", "ihe"),
            false,
            "wsdl/iti14.wsdl",
            false,
            false,
            false);

    @SuppressWarnings("unchecked") // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Iti14Endpoint(uri, remaining, this, getCustomInterceptors(parameters));
    }

    @Override
    public ItiServiceInfo getWebServiceConfiguration() {
        return WS_CONFIG;
    }
}
