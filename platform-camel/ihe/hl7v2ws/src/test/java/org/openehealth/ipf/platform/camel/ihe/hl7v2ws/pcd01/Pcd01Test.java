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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.load;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AcceptanceException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.AbstractHl7v2Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.AbstractProducerInterceptor;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;
/**
 * 
 * @author Mitko Kolev
 * 
 */
public class Pcd01Test extends StandardTestContainer {
    
    public static final String PCD_01_SPEC_REQUEST = load("pcd01/pcd01-request.hl7").toString();
    public static final String PCD_01_SPEC_RESPONSE = load("pcd01/pcd01-response.hl7").toString();
   

    @BeforeClass
    public static void setUpClass() {
        startServer(new CXFServlet(), "pcd-01.xml");
    }

    @Before
    public void setUp() {
        MyFailureHandler.resetCounter();
    }

    @Test
    public void testHappyCase() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/devicedata";
        String response = requestBody(uri, PCD_01_SPEC_REQUEST);
        assertResponseEquals(PCD_01_SPEC_RESPONSE, response);
        assertEquals(0, MyFailureHandler.getCount());
    }
    
    @Test
    public void testHappyCaseInboundValidation() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/route_inbound_validation";
        String response = requestBody(uri, PCD_01_SPEC_REQUEST);
        assertResponseEquals(PCD_01_SPEC_RESPONSE, response);
        assertEquals(0, MyFailureHandler.getCount());
    }
    
    @Test
    public void testHappyCaseInboundAndOutboundValidation() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/route_inbound_and_outbound_validation";
        String response = requestBody(uri, PCD_01_SPEC_REQUEST);
        assertResponseEquals(PCD_01_SPEC_RESPONSE, response);
        assertEquals(0, MyFailureHandler.getCount());
    }

    @Test(expected = Hl7v2AcceptanceException.class)
    public void testInacceptableRequestOnProducer() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/devicedata";
        requestBody(uri, PCD_01_SPEC_REQUEST.replace("|2.6|", "|2.5|"));
        assertEquals(0, MyFailureHandler.getCount());
    }

    @Test
    public void testInacceptableRequestOnConsumer() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/devicedata";
        Endpoint endpoint = getCamelContext().getEndpoint(uri);
        Processor processor = endpoint.createProducer();
        while (processor instanceof AbstractHl7v2Interceptor) {
            processor = ((AbstractHl7v2Interceptor) processor).getWrappedProcessor();
        }
        Exchange exchange = new DefaultExchange(getCamelContext());
        exchange.getIn().setBody(PCD_01_SPEC_REQUEST.replace("|2.6|", "|2.5|"));
        processor.process(exchange);
        assertEquals(1, MyFailureHandler.getCount());
    }

    @Test
    public void testApplicationError() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/route_throws_exception";
        String response = requestBody(uri, PCD_01_SPEC_REQUEST);
        assertTrue(response.startsWith("MSH|^~\\&|"));
        assertTrue("The response message must contain the cause", response.contains("java.lang.RuntimeException"));
        assertTrue("On application error the request message id must be returned.", response.contains("MSA|AE|MSGID1234"));
        assertEquals(0, MyFailureHandler.getCount());
    }
    
    @Test
    public void testInboundValidation() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/route_inbound_validation";
        String response = requestBody(uri, PCD_01_SPEC_REQUEST);
        assertTrue(response.startsWith("MSH|^~\\&|"));
        assertResponseEquals(PCD_01_SPEC_RESPONSE, response);
        assertEquals(0, MyFailureHandler.getCount());
    }
    
    @Test
    public void testInboundValidationError() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/route_inbound_validation";
        //this must be a validation error
        String invalidMSG = PCD_01_SPEC_REQUEST.replace("|1.0.1|", "||");
        String response = requestBody(uri, invalidMSG);
        assertTrue(response.startsWith("MSH|^~\\&|"));
        assertTrue(response.contains("MSA|AE"));
        assertTrue(response.contains("OBX-4"));
        assertEquals(0, MyFailureHandler.getCount());
    }

    @Test
    public void testInboundAndOutboundValidationError() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort() + "/route_inbound_and_outbound_validation";
        //this must be a validation error
        String response = requestBody(uri, PCD_01_SPEC_REQUEST);
        assertTrue(response.startsWith("MSH|^~\\&|"));
        assertResponseEquals(PCD_01_SPEC_RESPONSE, response);
        assertEquals(0, MyFailureHandler.getCount());
    }
    
    @Test
    public void testDefaultAcceptedResponse() throws Exception {
        String uri = "pcd-pcd01://localhost:" + getPort()
                + "/route_unacceptable_response";
        String response = requestBody(uri, PCD_01_SPEC_REQUEST);
        assertTrue(response.startsWith("MSH|^~\\&|"));
        assertTrue(response.contains("|ACK^R01^ACK|"));
        assertTrue(response.contains("MSA|AR|MSGID1234"));
        assertTrue(response.contains("ERR|||203^Unsupported version id^HL70357^^Invalid HL7 version 2.5|E|||Invalid HL7 version 2.5"));
        assertEquals(1, MyFailureHandler.getCount());
    }

    private String requestBody(String uri, String msg) {
        Object  response = send(uri, msg);
        return Exchanges.resultMessage((Exchange)response).getBody(String.class);
    }
    
    private void assertResponseEquals(String expected, String response){
        //use the same algorithm to parse the String message
        assertEquals(expected, MessageAdapters.make(response).toString());
    }
 
    public static void main(String args []){
        startServer(new CXFServlet(), "pcd-01.xml", false, DEMO_APP_PORT);
    }
}
