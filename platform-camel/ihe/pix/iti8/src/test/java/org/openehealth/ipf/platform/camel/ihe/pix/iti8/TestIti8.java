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
package org.openehealth.ipf.platform.camel.ihe.pix.iti8;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mina.MinaConsumer;
import org.apache.camel.impl.DefaultExchange;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.atna.UdpServer;
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.consumer.MllpConsumerMarshalInterceptor;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.PipeParser;


/**
 * Unit tests for the PIX Feed transaction a.k.a. ITI-8
 * 
 * @author Dmytro Rud
 */
public class TestIti8 {
   
    private static ProducerTemplate<Exchange> producerTemplate;
    private static CamelContext camelContext;
    private static UdpServer syslog;
   
    private static final int SYSLOG_PORT = 8888;
    
    
    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void setUpClass() throws Exception {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("iti-8.xml");
        producerTemplate = (ProducerTemplate<Exchange>) appContext.getBean("producerTemplate", ProducerTemplate.class);
        camelContext = (CamelContext) appContext.getBean("camelContext", CamelContext.class);

        syslog = new UdpServer(SYSLOG_PORT);
        syslog.start();
        
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(SYSLOG_PORT);

    }
    
    @AfterClass
    public static void stopServer() throws Exception {
        if (syslog != null) {
            syslog.cancel();
            syslog.join();
        }
    }
    

    // -----------------------------------
    // Test program:
    //   1. Happy case
    //   2. Inacceptance on Consumer
    //   3. Inacceptance on Producer
    //   4. Incomplete audit datasets
    //   5. Exceptions in the route
    //   6. Alternative HL7 codec factory
    //   7. Data types on Producer and Consumer sides
    // -----------------------------------

    
    /**
     * Happy case, audit either enabled or disabled.
     * Expected result: ACK response, two or zero audit items.
     */
    @Test
    public void testHappyCaseAndAudit() throws Exception {
        doTestHappyCaseAndAudit("xds-iti8://localhost:8888", 2);
        doTestHappyCaseAndAudit("pix-iti8://localhost:8888?audit=true", 2);
        doTestHappyCaseAndAudit("xds-iti8://localhost:8887?audit=false", 0);
    }
    
    private void doTestHappyCaseAndAudit(String endpointUri, int expectedPacketCount) throws Exception {
        final String body = getMessageString("ADT^A01", "2.3.1"); 

        syslog.expectedPacketCount(expectedPacketCount);
        MessageAdapter msg = send(endpointUri, body);
        Hl7TestUtils.checkHappyCase(msg);
        syslog.assertIsSatisfied();
        syslog.reset();
    }


    /**
     * Inacceptable messages (wrong message type, wrong trigger event, wrong version), 
     * on consumer side, audit enabled.
     * Expected results: NAK responses, no audit.
     * <p>
     * We do not use MLLP producers, because they perform their own acceptance
     * tests and do not pass inacceptable messages to the consumers
     * (it is really a feature, not a bug! ;-)) 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInacceptanceOnConsumer() throws Exception {
        String endpointUri = "pix-iti8://localhost:8890";
        Endpoint endpoint = camelContext.getEndpoint(endpointUri);
        MinaConsumer consumer = (MinaConsumer) endpoint.createConsumer(new FictiveProcessor());
        Processor processor = consumer.getProcessor();
        
        assertTrue(processor instanceof MllpConsumerMarshalInterceptor);
        
        doTestInacceptanceOnConsumer("MDM^T01", "2.3.1", processor);
        doTestInacceptanceOnConsumer("ADT^A04", "2.3.1", processor);
        doTestInacceptanceOnConsumer("ADT^A01", "2.5", processor);
        doTestInacceptanceOnConsumer("ADT^A01", "3.1415926", processor);
    }

    private void doTestInacceptanceOnConsumer(String msh9, String msh12, Processor processor) throws Exception {
        String body = getMessageString(msh9, msh12); 
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(body);

        syslog.expectedPacketCount(0);
        processor.process(exchange);
        String response = (String) exchange.getOut().getBody();
        MessageAdapter msg = MessageAdapters.make(new PipeParser(), response);
        Hl7TestUtils.checkNAK(msg);
        syslog.assertIsSatisfied();
        syslog.reset();
    }
    

    /**
     * Inacceptable messages (wrong message type, wrong trigger event, wrong version), 
     * on producer side, audit enabled.
     * Expected results: raise of corresponding HL7-related exceptions, no audit.
     */
    @Test
    public void testInacceptanceOnProducer() throws Exception {
        doTestInacceptanceOnProducer("MDM^T01", "2.3.1");
        doTestInacceptanceOnProducer("ADT^A04", "2.3.1");
        doTestInacceptanceOnProducer("ADT^A01", "2.4");
    }
    
    private void doTestInacceptanceOnProducer(String msh9, String msh12) throws Exception {
        String endpointUri = "xds-iti8://localhost:8890";
        String body = getMessageString(msh9, msh12);

        boolean failed = true;
        
        syslog.expectedPacketCount(0);
        try {
            send(endpointUri, body);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if((e instanceof HL7Exception) || (cause instanceof HL7Exception) ||
               (e instanceof AbstractHL7v2Exception) || (cause instanceof AbstractHL7v2Exception))
            {
                failed = false;
            }
        }
        syslog.assertIsSatisfied();
        syslog.reset();
        
        assertFalse(failed);
    }
    

    /**
     * Incomplete messages (absent PID segment), incomplete audit enabled.
     * Expected results: corresponding count of audit items (0-1-2).
     */
    @Test
    public void testIncompleteAudit() throws Exception {
        // both consumer-side and producer-side
        doTestIncompleteAudit("xds-iti8://localhost:8889?allowIncompleteAudit=true", 2);
        // consumer-side only
        doTestIncompleteAudit("pix-iti8://localhost:8889", 1);
        // producer-side only
        doTestIncompleteAudit("xds-iti8://localhost:8888?allowIncompleteAudit=true", 1);
        // producer-side only, but fictive
        doTestIncompleteAudit("pix-iti8://localhost:8888?allowIncompleteAudit=true&audit=false", 0);
    }

    private void doTestIncompleteAudit(String endpointUri, int expectedPacketCount) throws Exception {
        String body = getMessageString("ADT^A01", "2.3.1", false); 
        syslog.expectedPacketCount(expectedPacketCount);
        MessageAdapter msg = send(endpointUri, body);
        Hl7TestUtils.checkHappyCase(msg);
        syslog.assertIsSatisfied();
        syslog.reset();
    }


    /**
     * Tests how the exceptions in tte route are handled.
     */
    @Test
    public void testExceptions() {
        String body = getMessageString("ADT^A01", "2.3.1");
        doTestException("pix-iti8://localhost:8891", body, "you cry");
        doTestException("pix-iti8://localhost:8892", body, "lazy dog");
    }

    private void doTestException(String endpointUri, String body, String wantedOutputContent) {
        MessageAdapter msg = send(endpointUri, body);
        Hl7TestUtils.checkNAK(msg);
        assertTrue(msg.toString().contains(wantedOutputContent));
    }
    
    
    /**
     * Checks whether alternative HL7 codec factories can be used.
     */
    @Test
    public void testAlterativeHl7CodecFactory() {
        String endpointUri1 = "pix-iti8://fake.address.no.uri:80?codec=alternativeCodec";
        MllpEndpoint endpoint1 = (MllpEndpoint) camelContext.getEndpoint(endpointUri1);
        String endpointUri2 = "xds-iti8://localhost:8891";
        MllpEndpoint endpoint2 = (MllpEndpoint) camelContext.getEndpoint(endpointUri2);
        assertEquals("UTF-8", endpoint1.getCharsetName());
        assertEquals("ISO-8859-1", endpoint2.getCharsetName());
    }
    

    /**
     * Checks whether various request data types are being handled properly.
     */
    @Test
    public void testRequestDataTypes() throws Exception {
        final String endpointUri = "pix-iti8://localhost:8887?audit=false";
        final String originalBody = getMessageString("ADT^A01", "2.3.1");
        Object body;

        /*
         * Refer to {@link MllpMarshalUtils#typeSupported()} 
         * for the list of currently supported types. 
         */
        
        // String
        body = originalBody;
        send(endpointUri, body);
        
        // MessageAdapter
        body = MessageAdapters.make(new PipeParser(), originalBody);
        send(endpointUri, body);

        // HAPI message
        body = ((MessageAdapter) body).getTarget();
        send(endpointUri, body);
        
        // File
        URL fileUrl = getClass().getClassLoader().getResource("hl7v2message.txt");
        body = new File(fileUrl.toURI());
        send(endpointUri, body);
        
        // InputStream
        body = new ByteArrayInputStream(originalBody.getBytes());
        send(endpointUri, body);
        
        // NIO ByteBuffer
        body = ByteBuffer.wrap(originalBody.getBytes());
        send(endpointUri, body);
        
        // byte[]
        body = originalBody.getBytes();
        send(endpointUri, body);
        
        
        /* --------------- values of unsupported types and null should cause exceptions --------------- */
        boolean exceptionThrown = false;
        try {
            body = null;
            send(endpointUri, body);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            body = new Integer(12345);
            send(endpointUri, body);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    
    }
    

    /**
     * Checks whether various response data types are being handled properly.
     */
    @Test
    public void testResponseDataTypes() {
        final String endpointUri = "pix-iti8://localhost:8880?audit=false";
        final String body = getMessageString("ADT^A01", "2.3.1");
        RouteBuilder.cleanCheckedContentTypes();
        
        // 0-8 should return ACks
        for(int i = 0; i <= 8; ++i) {
            MessageAdapter msg = send(endpointUri, body);
            Hl7TestUtils.checkHappyCase(msg);
        }
        
        // 9-12 should throw exceptions 
        int exceptionsCount = 0;
        for(int i = 9; i <= 12; ++i) {
            try {
                send(endpointUri, body);
            } catch (Exception e) {
                ++exceptionsCount;
            }
        }
        assertEquals(4, exceptionsCount);
        
        // 13-18 should return NAKs
        for(int i = 13; i <= 18; ++i) {
            MessageAdapter msg = send(endpointUri, body);
            Hl7TestUtils.checkNAK(msg);
        }
        
        // prove that we have not missed any of the pre-configured data types 
        assertTrue(RouteBuilder.allContentTypesChecked());
    }
    
    
    // ----- helper methond and classes -----
    
    private MessageAdapter send(String endpoint, Object body) {        
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(body);
        Message response = Exchanges.resultMessage(producerTemplate.send(endpoint, exchange));
        return response.getBody(MessageAdapter.class);
    }
    
    private String getMessageString(String msh9, String msh12) {
        return getMessageString(msh9, msh12, true);
    }
    
    private String getMessageString(String msh9, String msh12, boolean pid) {
        StringBuilder sb = new StringBuilder("MSH|^~\\&|MESA_PD_SUPPLIER|XYZ_HOSPITAL|||20081204114742||")
            .append(msh9)
            .append("|123456|T|")
            .append(msh12)
            .append("|||ER\n")
            .append("EVN|A01|20081204114742\n");
        if(pid) {
            sb.append("PID|1||001^^^XREF2005~002^^^HIMSS2005||Multiple^Christof^Maria^Prof.|Eisner|")
              .append("19530429|M|||Bahnhofstr. 1^^Testort^^01234^DE^H|||||||AccNr01^^^ANICPA|")
              .append("111-222-333|\n");
        }
        return sb.append("PV1|1|I|").toString();
    }
    

    private static class FictiveProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            // does nothing, just because does not have to do anything
        }
    }
}
