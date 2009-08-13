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
package org.openehealth.ipf.platform.camel.lbs.cxf.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.cxf.spring.CxfEndpointBean;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.message.MessageContentsList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.platform.camel.core.junit.DirtySpringContextJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * This test creates CXF endpoints implicitly via the routes. These will
 * not automatically be removed after the test execution. To do this it is
 * necessary to dirty the Spring application context. The best way to do this
 * is to run this test via {@link DirtySpringContextJUnit4ClassRunner}.
 *  
 * @author Jens Riemschneider
 */
public abstract class AbstractLbsCxfTest {
    
    protected static final String FILE_CONTENT = "blu bla";

    @Autowired @Qualifier("soapEndpointNoExtract")
    private CxfEndpointBean endpointNoExtract;
    
    @Autowired @Qualifier("soapEndpointExtract")
    private CxfEndpointBean endpointExtract;

    @Autowired @Qualifier("soapEndpointExtractRouter")
    private CxfEndpointBean endpointExtractRouter; 
    
    @Autowired @Qualifier("soapEndpointExtractRouterRealServer")
    private CxfEndpointBean endpointExtractRouterRealServer; 
    
    @Autowired @Qualifier("soapEndpointRealServer")
    private CxfEndpointBean endpointRealServer; 
    
    @Autowired @Qualifier("soapEndpointExtractSwA")
    private CxfEndpointBean endpointExtractSwA; 
    
    private static final String ENDPOINT_NON_CXF = 
        "direct:cxflbs";
    
    @Autowired @Qualifier("serviceBean")
    protected ServiceBean serviceBean;
    
    private File file;
    protected Greeter greeter;
    private BindingProvider provider;

    protected DataHandler inputDataHandler;
    protected DataHandler onewayDataHandler;

    @Autowired
    protected CamelContext camelContext;

    @Autowired
    protected ProducerTemplate producerTemplate;
    
    @Autowired
    private LargeBinaryStore store;
        
    @EndpointInject(uri="mock:mock")
    protected MockEndpoint mock;

    @Before
    public void setUp() throws Exception {
        file = File.createTempFile(getClass().getName(), "txt");
        FileWriter writer = new FileWriter(file);
        writer.write(FILE_CONTENT);
        writer.close();

        DataSource inputDataSource = new FileDataSource(file);
        inputDataHandler = new DataHandler(inputDataSource);
        
        DataSource onewayDataSource = new FileDataSource(file);
        onewayDataHandler = new DataHandler(onewayDataSource);
        
        URL wsdlResource = getClass().getClassLoader().getResource("hello_world.wsdl");
        SOAPService service = new SOAPService(wsdlResource);
        greeter = service.getSoapOverHttp();
        provider = (BindingProvider)greeter;
    }

    protected void enableMTOM() {
        SOAPBinding binding = (SOAPBinding) provider.getBinding(); 
        binding.setMTOMEnabled(true);
    }

    @After
    public void tearDown() throws Exception {
        store.deleteAll();
        file.delete();
        mock.reset();
        serviceBean.setCheckProcessor(null);
    }
    
    @Test
    public void testStandardSOAPCallWithoutResourceExtract() throws Exception {
        enableMTOM(); 
        setEndpoint(endpointNoExtract);
        String response = greeter.greetMe("Hello Camel!!");
        assertEquals("Greetings from Apache Camel!!!! Request was Hello Camel!!", response);
    }

    @Test
    public void testAttachmentSOAPCallWithoutResourceExtract() throws Exception {        
        enableMTOM(); 
        setEndpoint(endpointNoExtract);
        Holder<String> nameHolder = new Holder("Hello Camel!!");
        Holder<DataHandler> handlerHolder = new Holder(inputDataHandler);
        greeter.postMe(nameHolder, handlerHolder, onewayDataHandler);
        
        assertEquals("Greetings from Apache Camel!!!! Request was Hello Camel!!", nameHolder.value);
        assertEquals(FILE_CONTENT + FILE_CONTENT, toString(handlerHolder));
    }

    @Test
    public void testStandardSOAPCallWithResourceExtract() throws Exception {
        setEndpoint(endpointExtract);
        String response = greeter.greetMe("Hello Camel!!");
        assertEquals("Greetings from Apache Camel!!!! Request was Hello Camel!!", response);
    }

    @Test
    public void testAttachmentSOAPCallWithResourceExtract() throws Exception {        
        enableMTOM(); 
        setEndpoint(endpointExtract);
        serviceBean.setCheckProcessor(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                MessageContentsList params = exchange.getIn().getBody(MessageContentsList.class);
                Holder<DataHandler> param = (Holder<DataHandler>) params.get(1);
                assertTrue(param.value.getDataSource() instanceof ResourceDataSource);
                DataHandler onewayParam = (DataHandler) params.get(2);
                assertTrue(onewayParam.getDataSource() instanceof ResourceDataSource);
            }
        });        
        
        Holder<String> nameHolder = new Holder("Hello Camel!!");
        Holder<DataHandler> handlerHolder = new Holder(inputDataHandler);
        greeter.postMe(nameHolder, handlerHolder, onewayDataHandler);
        
        assertEquals("Greetings from Apache Camel!!!! Request was Hello Camel!!", nameHolder.value);
        assertEquals(FILE_CONTENT + FILE_CONTENT, toString(handlerHolder));
    }
    
    @Test
    public void testDelayedResourceRemoval() throws Exception {
        enableMTOM(); 
        setEndpoint(endpointExtract);

        final URI resourceUri[] = new URI[1];
        serviceBean.setCheckProcessor(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                MessageContentsList params = exchange.getIn().getBody(MessageContentsList.class);
                DataHandler param = (DataHandler) params.get(0);
                assertTrue(param.getDataSource() instanceof ResourceDataSource);
                ResourceDataSource resource = (ResourceDataSource) param.getDataSource();
                resourceUri[0] = resource.getResourceUri();
            }
        });        
        
        DataHandler outputDataHandler = greeter.pingMe(inputDataHandler);        
        InputStream inputStream = outputDataHandler.getInputStream();
        try {
            assertEquals(FILE_CONTENT, IOUtils.toString(inputStream));
        }
        finally {
            inputStream.close();
        }
        
        assertFalse("Temporary resource was not removed from store", 
                store.contains(resourceUri[0]));
    }
    
    @Test
    public void testStandardSOAPCallRouter() throws Exception {
        setEndpoint(endpointExtractRouter);       
        String response = greeter.greetMe("Hello Camel!!");
        assertEquals("Greetings from Apache Camel!!!! Request was Hello Camel!!", response);
    }

    @Test
    public void testAttachmentSOAPCallRouter() throws Exception {        
        enableMTOM(); 
        setEndpoint(endpointExtractRouter);
        serviceBean.setCheckProcessor(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                MessageContentsList params = exchange.getIn().getBody(MessageContentsList.class);
                Holder<DataHandler> param = (Holder<DataHandler>) params.get(1);
                assertTrue(param.value.getDataSource() instanceof ResourceDataSource);
                DataHandler onewayParam = (DataHandler) params.get(2);
                assertTrue(onewayParam.getDataSource() instanceof ResourceDataSource);
            }
        });        
        
        Holder<String> nameHolder = new Holder("Hello Camel!!");
        Holder<DataHandler> handlerHolder = new Holder(inputDataHandler);
        greeter.postMe(nameHolder, handlerHolder, onewayDataHandler);
        
        assertEquals("Greetings from Apache Camel!!!! Request was Hello Camel!!", nameHolder.value);
        assertEquals(FILE_CONTENT + FILE_CONTENT, toString(handlerHolder));
    }

    /**
     * This test uses a route that extracts the attached data into the store and then
     * routes the request on to the real server. The intention is not to test that
     * the extraction works (we've seen this in other tests already). The result of
     * the extraction changes the SOAP message. This change should be fine with a real
     * service attached to the outgoing side.
     * <p>
     * The point is that we can store a large binary, make changes to a SOAP call and
     * hand it on to the real service.
     * @throws Exception
     */
    @Test
    public void testRoutingToRealServer() throws Exception {
        enableMTOM(); 
        setEndpoint(endpointExtractRouterRealServer);

        Object implementor = new GreeterImpl();
        String address = endpointRealServer.getAddress();
        Endpoint endpoint = Endpoint.publish(address, implementor);
        try {
            Holder<String> nameHolder = new Holder("Hello Camel!!");
            Holder<DataHandler> handlerHolder = new Holder(inputDataHandler);
            greeter.postMe(nameHolder, handlerHolder, onewayDataHandler);

            assertEquals("Hello from the real world. Request was Hello Camel!!", nameHolder.value);
            assertEquals("I received: " + FILE_CONTENT + "/" + FILE_CONTENT, toString(handlerHolder));
        }
        finally {        
            endpoint.stop();
        }
    }
    
    @Test
    public void testStoreDoesNotHandleNonCxfMessage() throws Exception {
        Exchange sendExchange = new DefaultExchange(camelContext);
        sendExchange.getIn().setBody("testbody");

        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("testbody");
        
        producerTemplate.send(ENDPOINT_NON_CXF, sendExchange);

        mock.assertIsSatisfied();
    }    
    
    protected String toString(Holder<DataHandler> handlerHolder) throws IOException {
        InputStream inputStream = handlerHolder.value.getInputStream();
        try {
            return IOUtils.toString(inputStream);
        }
        finally {
            inputStream.close();
        }
    }
    
    @Test
    public void testAttachmentSOAPCallWithResourceExtractSwA() throws Exception {        
        setEndpoint(endpointExtractSwA);
        serviceBean.setCheckProcessor(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                MessageContentsList params = exchange.getIn().getBody(MessageContentsList.class);
                Holder<DataHandler> param = (Holder<DataHandler>) params.get(1);
                assertTrue(param.value.getDataSource() instanceof ResourceDataSource);
                DataHandler onewayParam = (DataHandler) params.get(2);
                assertTrue(onewayParam.getDataSource() instanceof ResourceDataSource);
            }
        });        
        
        Holder<String> nameHolder = new Holder("Hello Camel!!");
        Holder<DataHandler> handlerHolder = new Holder(inputDataHandler);
        greeter.postMe(nameHolder, handlerHolder, onewayDataHandler);
        
        assertEquals("Greetings from Apache Camel!!!! Request was Hello Camel!!", nameHolder.value);
        assertEquals(FILE_CONTENT + FILE_CONTENT, toString(handlerHolder));
    }
    
    protected void setEndpoint(CxfEndpointBean endpoint) {
        provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint.getAddress());
    }

    public static final class CheckOutputDataSource extends DelegateProcessor {
        @Override
        protected void processNext(Exchange exchange) throws Exception {
            super.processNext(exchange);
            MessageContentsList params = exchange.getIn().getBody(MessageContentsList.class);
            for (Object param : params) {
                if (param instanceof Holder) {
                    Holder<?> holder = (Holder<?>) param;
                    if (holder.value instanceof DataHandler) {
                        DataHandler handler = (DataHandler) holder.value;
                        assertEquals("Output was not replaced with stored resource",
                                ResourceDataSource.class, handler.getDataSource().getClass());
                    }
                }
            }
        }
    }
}
