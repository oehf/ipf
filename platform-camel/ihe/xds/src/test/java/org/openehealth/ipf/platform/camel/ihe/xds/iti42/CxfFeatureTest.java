/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42;

import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.ws.security.trust.STSClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsRequestClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42PortType;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.soap.SOAPFaultException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.openehealth.ipf.commons.ihe.xds.XDS.Interactions.ITI_42;

public class CxfFeatureTest extends XdsStandardTestContainer {

    static private final String CONTEXT_DESCRIPTOR = "feature-test-resources/server-context.xml";

    @BeforeAll
    public static void setUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testFeatureEndpointWithoutPolicy() {
        JaxWsClientFactory<? extends XdsAuditDataset> clientFactory = new JaxWsRequestClientFactory<>(
                ITI_42.getWsTransactionConfiguration(),
                "http://localhost:" + getPort() + "/xds-iti42",
                null, null,
                null, null, null, null, null, null);
        var client = (Iti42PortType) clientFactory.getClient();
        try {
            client.documentRegistryRegisterDocumentSetB(new SubmitObjectsRequest());
            fail("This line must be not reachable");
        } catch (SOAPFaultException ex) {
            assertTrue(ex.getMessage().contains("These policy alternatives can not be satisfied"));
        }
    }

    @Test
    public void testFeatureEndpointWithPolicy() {
        var bf = new SpringBusFactory();
        var bus = bf.createBus("feature-test-resources/client-context.xml");
        SpringBusFactory.setDefaultBus(bus);
        SpringBusFactory.setThreadDefaultBus(bus);

        var client =
                getClient("feature-test-resources/iti42-with-policy.wsdl", "http://localhost:" + getPort() + "/xds-iti42");

        var requestContext = ((BindingProvider) client).getRequestContext();
        //STSClient stsClient = (STSClient) requestContext.get(SecurityConstants.STS_CLIENT);
        var stsClient = (STSClient) requestContext.get("ws-security.sts.client");
        stsClient.setWsdlLocation("http://localhost:" + getPort() + "/X509?wsdl");

        try {
            client.documentRegistryRegisterDocumentSetB(new SubmitObjectsRequest());
        } catch (SOAPFaultException ex) {
            //ex.printStackTrace();
            fail();
        } finally {
            SpringBusFactory.setThreadDefaultBus(null);
            SpringBusFactory.setDefaultBus(null);
        }
    }

    private Iti42PortType getClient(String wsdlLocation, String serviceURL) {
        var wsdlURL = getClass().getClassLoader().getResource(wsdlLocation);
        var service = Service.create(wsdlURL, ITI_42.getWsTransactionConfiguration().getServiceName());
        var client = (Iti42PortType) service.getPort(ITI_42.getWsTransactionConfiguration().getSei());

        var bindingProvider = (BindingProvider) client;
        var reqContext = bindingProvider.getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
        return client;
    }

}
