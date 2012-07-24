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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.trust.STSClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer;
import org.openehealth.ipf.commons.ihe.ws.server.ServletServer;
import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.iti42.Iti42PortType;
import org.springframework.core.io.ClassPathResource;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class CxfFeatureTest {

    static private int port;
    static private JettyServer server;

    @BeforeClass
    public static void setUp() throws IOException {
        port = ServletServer.getFreePort();
        //port = 8091;
        server = new JettyServer();
        server.setContextResource(new ClassPathResource("feature-test-resources/server-context.xml").getURI().toString());
        server.setPort(port);
        server.setContextPath("");
        server.setServletPath("/*");
        server.setServlet(new CXFServlet());

        server.start();
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }

    @Test
    public void testFeatureEndpointWithoutPolicy() {
        JaxWsClientFactory clientFactory = new XdsClientFactory(
                Iti42Component.WS_CONFIG,
                "http://localhost:" + port + "/xds-iti42",
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

        URL wsdlURL = getClass().getClassLoader().getResource("feature-test-resources/iti42-with-policy.wsdl");
        Service service = Service.create(wsdlURL, Iti42Component.WS_CONFIG.getServiceName());
        Iti42PortType client = (Iti42PortType)service.getPort(Iti42Component.WS_CONFIG.getSei());
        configureBinding(client, "http://localhost:" + port + "/xds-iti42");


        try {
            client.documentRegistryRegisterDocumentSetB(new SubmitObjectsRequest());
        } catch(SOAPFaultException ex) {
            //ex.printStackTrace();
            Assert.fail();
        }
    }


    @Test
    public void testSimpleEndpointWithoutPolicy() {
        JaxWsClientFactory clientFactory = new XdsClientFactory(
                Iti42Component.WS_CONFIG,
                "http://localhost:" + port + "/xds-iti42-no-feature",
                null, null, null);
        Iti42PortType client = (Iti42PortType) clientFactory.getClient();
        try {
            client.documentRegistryRegisterDocumentSetB(new SubmitObjectsRequest());
        } catch(SOAPFaultException ex) {
            Assert.fail();
        }
    }

    private void configureBinding(Object client, String serviceURL) {
        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> reqContext = bindingProvider.getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);

        STSClient stsClient = (STSClient)bindingProvider.getRequestContext().get(SecurityConstants.STS_CLIENT);
        stsClient.setWsdlLocation("http://localhost:" + port + "/X509?wsdl");
    }

}
