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

package org.openehealth.ipf.platform.camel.ihe.xds.iti18;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.iti18.Iti18PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import javax.xml.namespace.QName;

/**
 * The Camel component for the ITI-18 transaction.
 */
public class Iti18Component extends AbstractWsComponent<ItiServiceInfo> {
    private final static ItiServiceInfo WS_CONFIG = new ItiServiceInfo(
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti18PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti18.wsdl",
            true,
            false,
            true);

    @Override
    @SuppressWarnings("unchecked") // Required because of base class
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new Iti18Endpoint(uri, remaining, this, getCustomInterceptors(parameters));
    }

    @Override
    public ItiServiceInfo getWebServiceConfiguration() {
        return WS_CONFIG;
    }
}
