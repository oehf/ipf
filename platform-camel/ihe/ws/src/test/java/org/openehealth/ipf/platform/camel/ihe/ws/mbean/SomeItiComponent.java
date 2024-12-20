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
package org.openehealth.ipf.platform.camel.ihe.ws.mbean;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import javax.xml.namespace.QName;
import java.util.Map;

public class SomeItiComponent extends AbstractWsComponent<WsAuditDataset, WsTransactionConfiguration<WsAuditDataset>, WsInteractionId<WsTransactionConfiguration<WsAuditDataset>>> {

    private static final String NS_URI = "urn:iti:some:mai:2011";

    public static final WsTransactionConfiguration<WsAuditDataset> WS_CONFIG = new WsTransactionConfiguration<>(
            "foo",
            "Some Component",
            false,
            null,
            null,
            new QName(NS_URI, "SomeConsumer_Service", "iti"),
            String.class,
            new QName(NS_URI, "SomeConsumer_Binding_Soap12", "iti"),
            false,
            "wsdl/some/some.wsdl",
            true,
            false,
            false,
            false);

    public SomeItiComponent() {
        super(null);
    }

    @Override
    public WsTransactionConfiguration<WsAuditDataset> getWsTransactionConfiguration() {
        return WS_CONFIG;
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new SomeItiEndpoint(uri, remaining, this,
                getAuditContext(parameters),
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                getHttpClientPolicy(parameters));
    }

}
