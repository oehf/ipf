/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.lbs.mina.mllp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.platform.camel.core.junit.DirtySpringContextJUnit4ClassRunner;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.uhn.hl7v2.llp.MinLLPReader;
import ca.uhn.hl7v2.llp.MinLLPWriter;
import ca.uhn.hl7v2.model.Message;


/**
 * This test uses {@link DirtySpringContextJUnit4ClassRunner} which is an 
 * alternative to {@link SpringJUnit4ClassRunner} that recreates the Spring
 * application context for the next test class. 
 * <p><b>
 * Do not simply copy this code. It could result it bad performance of tests.
 * Use the standard {@link SpringJUnit4ClassRunner} if you don't need this, 
 * which is usually the case.
 * </b><p>
 * This class requires that the Spring application context is recreated because
 * it creates Mina endpoints. These endpoints will not be thrown away and use 
 * the tcp/ip ports and endpoint names. Subsequent quests could fail because
 * the ports are in use and exchanges might not be received by the correct 
 * endpoint.
 * <p>
 * Note: There are no groovy-related tests for the mina support because no DSL
 * changes are required by the codec. Therefore, the tests would not test anything
 * in addition to Java-based tests.
 * 
 * @author Jens Riemschneider
 */
@RunWith(DirtySpringContextJUnit4ClassRunner.class) // DO NOT SIMPLY COPY!!! see above
@ContextConfiguration(locations = { "/context-lbs-route-mina-java.xml" })
public class LbsMllpViaMinaTest {
    
    private static final int PORT_TEXT_NO_EXTRACT = 6123; 
    private static final int PORT_HL7_UNMARSHAL_NO_EXTRACT = 6124; 
    private static final int PORT_HL7_EXTRACT = 6125;
    private static final int PORT_HL7_UNMARSHAL_EXTRACT = 6126;
    private static final int PORT_HL7_ROUTER_MARSHAL_EXTRACT = 6127;
    private static final int PORT_HL7_ROUTER_EXTRACT = 6128;

    @EndpointInject(uri="mock:mock")
    private MockEndpoint mock;
    
    private File file;
    private InetAddress localHost;
    
    @Autowired
    private LargeBinaryStore store;

    @Before
    public void setUp() throws Exception {
        file = File.createTempFile(getClass().getName(), "txt");
        FileWriter writer = new FileWriter(file);
        writer.write("blu bla");
        writer.close();

        localHost = InetAddress.getByName("localhost");
    }

    @After
    public void tearDown() throws Exception {        
        mock.reset();
        // MockEndpoint#reset does not set the default message processor back to
        // null (as of Camel 1.5). We have to do that manually.
        mock.whenAnyExchangeReceived(null);
        
        file.delete();
    }

    @Test
    public void testTextWithoutResourceExtract() throws Exception {
        mock.expectedMessageCount(1);

        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getOut().setBody("You said: " + exchange.getIn().getBody());
            }            
        });
        
        Socket socket = new Socket(localHost, PORT_TEXT_NO_EXTRACT);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("Hello\r\n");
            writer.flush();
            assertStreamStartsWith("You said: Hello", socket.getInputStream());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
            socket.close();
        }
        
        mock.assertIsSatisfied();
    }

    @Test
    public void testHL7UnmarshalNoExtract() throws Exception {
        mock.expectedMessageCount(1);

        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Message msg = exchange.getIn().getBody(Message.class);
                assertNotNull(msg);
                Exchanges.resultMessage(exchange).setBody("OK");
            }
        });
        
        Socket socket = new Socket(localHost, PORT_HL7_UNMARSHAL_NO_EXTRACT);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("MSH|^~\\&|MYSENDER|MYRECEIVER|MYAPPLICATION||200612211200||QRY^A19|1234|P|2.4\r\n");
            writer.write("QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||");
            writer.flush();
            assertStreamStartsWith("OK", socket.getInputStream());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
            socket.close();
        }

        mock.assertIsSatisfied();
    }

    @Test
    public void testHL7WithResourceExtract() throws Exception {
        mock.expectedMessageCount(2);

        StringBuffer textBuffer = new StringBuffer("MSH|^~\\&|MYSENDER|MYRECEIVER|MYAPPLICATION||200612211200||QRY^A19|1234|P|2.4\r\n");
        for (int idx = 0; idx < 100; ++idx) {
            textBuffer.append("QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||\r\n");
        }
        final String text = textBuffer.toString();
        final URI resourceUri[] = new URI[2];
        
        mock.whenAnyExchangeReceived(new Processor() {
            private int cnt = 0;
            
            @Override
            public void process(Exchange exchange) throws Exception {
                ResourceDataSource resource = exchange.getIn().getBody(ResourceDataSource.class);
                assertNotNull(resource);
                resourceUri[cnt] = resource.getResourceUri();
                ++cnt;
                assertTrue(store.contains(resourceUri[0]));
                InputStream receivedStream = exchange.getIn().getBody(InputStream.class);
                try {
                    String receivedText = IOUtils.toString(receivedStream);
                    assertEquals(text, receivedText);
                    exchange.getOut().setBody("OK");
                }
                finally {
                    receivedStream.close();
                }
            }
        });
        
        Socket socket = new Socket(localHost, PORT_HL7_EXTRACT);
        MinLLPWriter writer = null;
        MinLLPReader reader = null;
        try {
            writer = new MinLLPWriter(socket.getOutputStream());            
            writer.writeMessage(text);
            writer.writeMessage(text);
            reader = new MinLLPReader(socket.getInputStream());
            
            assertEquals("OK", getMessageBlocking(reader, 5000));
            assertEquals("OK", getMessageBlocking(reader, 5000));
        }
        finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            socket.close();
        }
        
        // The resources are deleted immediately after the session is closed.
        // However, the receiver will also continue right after the session is closed. 
        // There is a minimal chance that the resource has not been removed yet. So we 
        // need to wait a little.
        int failCount = 0;
        while ((store.contains(resourceUri[0]) || store.contains(resourceUri[1])) && failCount < 5) {
            ++failCount;
            Thread.sleep(100);
        }
        
        assertFalse("Resource was not removed: " + resourceUri[0], 
                store.contains(resourceUri[0]));
        assertFalse("Resource was not removed: " + resourceUri[1], 
                store.contains(resourceUri[1]));
        
        mock.assertIsSatisfied();        
    }
    
    @Test
    public void testHL7UnmarshalWithResourceExtract() throws Exception {
        mock.expectedMessageCount(2);

        StringBuffer textBuffer = new StringBuffer("MSH|^~\\&|MYSENDER|MYRECEIVER|MYAPPLICATION||200612211200||QRY^A19|1234|P|2.4\r\n");
        textBuffer.append("QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||\r\n");
        final String text = textBuffer.toString();
        
        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Message hl7Message = exchange.getIn().getBody(Message.class);
                assertNotNull(hl7Message);
                assertNotNull(hl7Message.get("MSH"));
                assertNotNull(hl7Message.get("QRD"));
                
                exchange.getOut().setBody("OK");
            }
        });
        
        Socket socket = new Socket(localHost, PORT_HL7_UNMARSHAL_EXTRACT);
        MinLLPWriter writer = null;
        MinLLPReader reader = null;
        try {
            writer = new MinLLPWriter(socket.getOutputStream());            
            writer.writeMessage(text);
            writer.writeMessage(text);
            reader = new MinLLPReader(socket.getInputStream());
            
            assertEquals("OK", getMessageBlocking(reader, 5000));
            assertEquals("OK", getMessageBlocking(reader, 5000));
        }
        finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            socket.close();
        }

        mock.assertIsSatisfied();
    }
    
    @Test
    public void testHL7MarshalRouterWithResourceExtract() throws Exception {
        mock.expectedMessageCount(2);

        StringBuffer textBuffer = new StringBuffer("MSH|^~\\&|MYSENDER|MYRECEIVER|MYAPPLICATION||200612211200||QRY^A19|1234|P|2.4\r\n");
        textBuffer.append("QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||\r\n");
        final String text = textBuffer.toString();
        
        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Message hl7Message = exchange.getIn().getBody(Message.class);
                assertNotNull(hl7Message);
                assertNotNull(hl7Message.get("MSH"));
                assertNotNull(hl7Message.get("QRD"));
                
                exchange.getOut().setBody("OK");
            }
        });
        
        Socket socket = new Socket(localHost, PORT_HL7_ROUTER_MARSHAL_EXTRACT);
        MinLLPWriter writer = null;
        MinLLPReader reader = null;
        try {
            writer = new MinLLPWriter(socket.getOutputStream());            
            writer.writeMessage(text);
            writer.writeMessage(text);
            reader = new MinLLPReader(socket.getInputStream());
            
            assertEquals("OK", getMessageBlocking(reader, 5000));
            assertEquals("OK", getMessageBlocking(reader, 5000));
        }
        finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            socket.close();
        }

        mock.assertIsSatisfied();
    }
    
    @Test
    public void testHL7RouterWithResourceExtract() throws Exception {
        mock.expectedMessageCount(2);

        StringBuffer textBuffer = new StringBuffer("MSH|^~\\&|MYSENDER|MYRECEIVER|MYAPPLICATION||200612211200||QRY^A19|1234|P|2.4\r\n");
        textBuffer.append("QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||\r\n");
        final String text = textBuffer.toString();
        
        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Message hl7Message = exchange.getIn().getBody(Message.class);
                assertNotNull(hl7Message);
                assertNotNull(hl7Message.get("MSH"));
                assertNotNull(hl7Message.get("QRD"));
                
                exchange.getOut().setBody("OK");
            }
        });
        
        Socket socket = new Socket(localHost, PORT_HL7_ROUTER_EXTRACT);
        MinLLPWriter writer = null;
        MinLLPReader reader = null;
        try {
            writer = new MinLLPWriter(socket.getOutputStream());            
            writer.writeMessage(text);
            writer.writeMessage(text);
            reader = new MinLLPReader(socket.getInputStream());
            
            assertEquals("OK", getMessageBlocking(reader, 5000));
            assertEquals("OK", getMessageBlocking(reader, 5000));
        }
        finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            socket.close();
        }

        mock.assertIsSatisfied();
    }
    
    private String getMessageBlocking(MinLLPReader reader, int timeout) throws Exception {
        String message = null;
        while (timeout >= 0 && message == null) {
            message = reader.getMessage();
            if (message == null) {
                Thread.sleep(500);
                timeout -= 500;
            }
        }
        return message;
    }
    
    private void assertStreamStartsWith(String expected, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            assertEquals(expected, reader.readLine());
        }
        finally {
            reader.close();
        }
    }
}
