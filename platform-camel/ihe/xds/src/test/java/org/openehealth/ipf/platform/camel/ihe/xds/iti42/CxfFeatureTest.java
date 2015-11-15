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

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.trust.STSClient;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

public class CxfFeatureTest extends StandardTestContainer {

    static private String CONTEXT_DESCRIPTOR = "feature-test-resources/server-context.xml";

    @BeforeClass
    public static void setUp() throws IOException {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR);
    }

//    public static void main(String[] args) {
//        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
//    }

    @Test
    public void testFeatureEndpointWithoutPolicy() {
        JaxWsClientFactory clientFactory = new XdsClientFactory(
                Iti42Component.WS_CONFIG,
                "http://localhost:" + getPort() + "/xds-iti42",
                null, null, null);
        Iti42PortType client = (Iti42PortType) clientFactory.getClient();
        try {
            client.documentRegistryRegisterDocumentSetB(new SubmitObjectsRequest());
        } catch(SOAPFaultException ex) {
            Assert.assertTrue(ex.getMessage().contains("These policy alternatives can not be satisfied"));
        }
    }

    @Test
    public void testFeatureEndpointWithPolicy() {
        SpringBusFactory bf = new SpringBusFactory();
	    Bus bus = bf.createBus("feature-test-resources/client-context.xml");
	    SpringBusFactory.setDefaultBus(bus);
	    SpringBusFactory.setThreadDefaultBus(bus);

        Iti42PortType client =
                getClient("feature-test-resources/iti42-with-policy.wsdl", "http://localhost:" + getPort() + "/xds-iti42");

        Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
        //STSClient stsClient = (STSClient) requestContext.get(SecurityConstants.STS_CLIENT);
        STSClient stsClient = (STSClient) requestContext.get("ws-security.sts.client");
        stsClient.setWsdlLocation("http://localhost:" + getPort() + "/X509?wsdl");

        try {
            client.documentRegistryRegisterDocumentSetB(new SubmitObjectsRequest());
        } catch(SOAPFaultException ex) {
            //ex.printStackTrace();
            Assert.fail();
        } finally {
            SpringBusFactory.setThreadDefaultBus(null);
            SpringBusFactory.setDefaultBus(null);
        }
    }

    private Iti42PortType getClient(String wsdlLocation, String serviceURL) {
        URL wsdlURL = getClass().getClassLoader().getResource(wsdlLocation);
        Service service = Service.create(wsdlURL, Iti42Component.WS_CONFIG.getServiceName());
        Iti42PortType client = (Iti42PortType)service.getPort(Iti42Component.WS_CONFIG.getSei());

        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> reqContext = bindingProvider.getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
        return client;
    }

}
