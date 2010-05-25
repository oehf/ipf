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
package org.openehealth.ipf.platform.camel.lbs.http.process;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProtocolException;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.openehealth.ipf.commons.lbs.resource.ResourceCompatibleDataSource;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.commons.lbs.resource.ResourceFactory;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.commons.lbs.utils.CorruptedInputStream;
import org.openehealth.ipf.platform.camel.core.junit.DirtySpringContextJUnit4ClassRunner;
import org.openehealth.ipf.platform.camel.lbs.http.process.ResourceList;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * This test creates HTTP endpoints implicitly via the routes. These will be
 * not automatically be removed after the test execution. To do this it is
 * necessary to dirty the Spring application context. The best way to do this
 * is to run this test via {@link DirtySpringContextJUnit4ClassRunner}.
 *  
 * @author Jens Riemschneider
 */
public abstract class AbstractLbsHttpTest {
    
    public static final String ENDPOINT_NO_EXTRACT = 
        "http://localhost:9452/lbstest_no_extract";

    public static final String ENDPOINT_EXTRACT = 
        "http://localhost:9452/lbstest_extract";
    
    public static final String ENDPOINT_PING = 
        "http://localhost:9452/lbstest_ping";
    
    public static final String ENDPOINT_EXTRACT_FACTORY_VIA_BEAN = 
        "http://localhost:9452/lbstest_extract_factory_via_bean";

    public static final String ENDPOINT_EXTRACT_ROUTER = 
        "http://localhost:9452/lbstest_extract_router";
    
    public static final String ENDPOINT_SEND_ONLY = 
        "direct:lbstest_send_only";
    
    public static final String ENDPOINT_NON_HTTP = 
        "direct:lbstest_non_http";
        
    @EndpointInject(uri="mock:mock")
    protected MockEndpoint mock;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    protected ProducerTemplate producerTemplate;
    
    @Autowired
    protected LargeBinaryStore store;
    
    @Resource(name = "resourceFactory")
    private ResourceFactory factory;
    
    protected File file;

    protected HttpClient httpClient;

    private TestOutputGenerator outputGenerator;
    
    @Before
    public void setUp() throws Exception {
        file = File.createTempFile(getClass().getName(), "txt");
        FileWriter writer = new FileWriter(file);
        writer.write("blu bla");
        writer.close();
        httpClient = new HttpClient();
        outputGenerator = new TestOutputGenerator("testoutput");
    }

    @After
    public void tearDown() throws Exception {        
        mock.reset();
        // MockEndpoint#reset does not set the default message processor back to
        // null (as of Camel 1.5). We have to do that manually.
        mock.whenAnyExchangeReceived(null);
        
        file.delete();
        store.deleteAll();
    }
    
    @Test
    public void testTextWithoutResourceExtract() throws Exception {
        PostMethod method = new PostMethod(ENDPOINT_NO_EXTRACT);
        method.setRequestEntity(new StringRequestEntity("testtext", "text/plain", null));

        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange received) throws Exception {
                // Must be done in here because the connection is only valid during
                // processing
                String body = received.getIn().getBody(String.class);
                assertEquals("testtext", body);
            }            
        });
        httpClient.executeMethod(method);
        method.getResponseBody();
        method.releaseConnection();
        mock.assertIsSatisfied();
    }

    @Test
    public void testFileWithoutResourceExtract() throws Exception {        
        PostMethod method = new PostMethod(ENDPOINT_NO_EXTRACT);
        method.setRequestEntity(new FileRequestEntity(file, "unknown/unknown"));

        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange received) throws Exception {
                // Must be done in here because the connection is only valid during
                // processing
                HttpServletRequest request = received.getIn().getBody(HttpServletRequest.class);
                assertNotNull(request);
                assertEquals("unknown/unknown", request.getContentType());
                
                String body = received.getIn().getBody(String.class);
                assertEquals("blu bla", body);
            }            
        });
        httpClient.executeMethod(method);
        method.releaseConnection();
        mock.assertIsSatisfied();
    }
    
    @Ignore @Test
    public void testMultipartWithoutResourceExtract() throws Exception {        
        PostMethod method = new PostMethod(ENDPOINT_NO_EXTRACT);
        Part[] parts = new Part[] {
                new FilePart("file1", file),
                new FilePart("file2", file),
                new StringPart("text", "testtext")
        };
        method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange received) throws Exception {
                // Must be done in here because the connection is only valid during
                // processing
                HttpServletRequest request = received.getIn().getBody(HttpServletRequest.class);
                assertNotNull(request);
                assertTrue("did not receive a multipart message", 
                        ServletFileUpload.isMultipartContent(request));
                
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List files = upload.parseRequest(request);
                assertEquals(3, files.size());
                
                FileItem fileItem = (FileItem)files.get(0);
                assertEquals("file1", fileItem.getFieldName());
                assertEquals("blu bla", fileItem.getString());

                fileItem = (FileItem)files.get(1);
                assertEquals("file2", fileItem.getFieldName());
                assertEquals("blu bla", fileItem.getString());
                
                fileItem = (FileItem)files.get(2);
                assertEquals("text", fileItem.getFieldName());
                assertEquals("testtext", fileItem.getString());
            }            
        });

        httpClient.executeMethod(method);
        method.releaseConnection();
        mock.assertIsSatisfied();
    }
    
    @Test
    public void testTextEndpointExtract() throws Exception {
        testText(ENDPOINT_EXTRACT); 
    }
    
    @Test
    public void testTextEndpointFactoryViaBean() throws Exception {
        testText(ENDPOINT_EXTRACT_FACTORY_VIA_BEAN);
    }
    
    @Test
    public void testTextEndpointRouter() throws Exception {
        testText(ENDPOINT_EXTRACT_ROUTER);
        
        Exchange exchange = mock.getReceivedExchanges().get(0);
        assertEquals("I was here", exchange.getIn().getHeader("tag"));
    }

    private void testText(final String endpoint) throws Exception {
        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(outputGenerator);

        PostMethod method = new PostMethod(endpoint);
        method.setRequestEntity(new StringRequestEntity("testtext", "text/plain", null));
        httpClient.executeMethod(method);        
        assertEquals("testoutput", method.getResponseBodyAsString());
        method.releaseConnection();

        mock.assertIsSatisfied();
        Map<String, String> receivedContent = outputGenerator.getReceivedContent();        
        assertEquals(1, receivedContent.size());
        assertEquals("testtext", receivedContent.get("unnamed"));
    }

    @Test
    public void testFileEndpointExtract() throws Exception {
        testFile(ENDPOINT_EXTRACT); 
    }
    
    @Test
    public void testFileEndpointFactoryViaBean() throws Exception {
        testFile(ENDPOINT_EXTRACT_FACTORY_VIA_BEAN);
    }

    @Test
    public void testFileEndpointRouter() throws Exception {
        testFile(ENDPOINT_EXTRACT_ROUTER);
    }
    
    protected void testFile(final String endpoint) throws Exception {
        PostMethod method = new PostMethod(endpoint);
        method.setRequestEntity(new FileRequestEntity(file, "unknown/unknown"));

        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(outputGenerator);

        httpClient.executeMethod(method);
        assertEquals("testoutput", method.getResponseBodyAsString());
        method.releaseConnection();

        mock.assertIsSatisfied();
        String receivedBody = outputGenerator.getReceivedBody();        
        assertEquals("blu bla", receivedBody);

        ResourceList receivedResources = outputGenerator.getReceivedResources();
        assertEquals(1, receivedResources.size());
        ResourceDataSource resource = receivedResources.get(0);        
        
        assertFalse("clean up did not remove temporary resource",
                store.contains(resource.getResourceUri()));
    }
    
    @Test
    public void testPingFile() throws Exception {
        PostMethod method = new PostMethod(ENDPOINT_PING);
        method.setRequestEntity(new FileRequestEntity(file, "unknown/unknown"));

        final URI[] resourceUri = new URI[1];
        
        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                ResourceDataSource resource = 
                    exchange.getIn().getBody(ResourceDataSource.class);
                resourceUri[0] = resource.getResourceUri();
            }            
        });

        httpClient.executeMethod(method);
        assertEquals("blu bla", method.getResponseBodyAsString());
        method.releaseConnection();

        // The resource is deleted immediately after the stream has been written.
        // However, the receiver will also continue right after the stream has
        // been written. There is a minimal chance that the resource has not
        // been removed yet. So we need to wait a little.
        int failCount = 0;
        while (store.contains(resourceUri[0]) && failCount < 5) {
            ++failCount;
            Thread.sleep(100);
        }
        assertFalse("resource not removed from the store", 
                store.contains(resourceUri[0]));
    }
    
    @Ignore @Test
    public void testMultipartEndpointExtract() throws Exception {
        testMultipart(ENDPOINT_EXTRACT); 
    }
    
    @Ignore @Test
    public void testMultipartEndpointFactoryViaBean() throws Exception {
        testMultipart(ENDPOINT_EXTRACT_FACTORY_VIA_BEAN);
    }

    @Ignore @Test
    public void testMultipartEndpointRouter() throws Exception {
        testMultipart(ENDPOINT_EXTRACT_ROUTER);
    }

    private void testMultipart(String endpoint) throws Exception {
        PostMethod method = new PostMethod(endpoint);
        Part[] parts = new Part[] {
                new FilePart("file1", file),
                new FilePart("file2", file),
                new StringPart("text1", "testtext1"),
                new StringPart("text2", "testtext2")
        };
        method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(outputGenerator);

        httpClient.executeMethod(method);
        assertEquals("testoutput", method.getResponseBodyAsString());
        method.releaseConnection();

        mock.assertIsSatisfied();
        Map<String, String> receivedContent = outputGenerator.getReceivedContent();        
        assertEquals(4, receivedContent.size());
        assertEquals("blu bla", receivedContent.get("file1"));
        assertEquals("blu bla", receivedContent.get("file2"));
        assertEquals("testtext1", receivedContent.get("text1"));
        assertEquals("testtext2", receivedContent.get("text2"));
  
        assertEquals("blu bla", outputGenerator.getReceivedBody());
    }
    
    @Ignore @Test
    public void testMultipartSendOnly() throws Exception {
        Exchange sendExchange = new DefaultExchange(camelContext);
        
        FileDataSource dataSource1 = new FileDataSource(file);
        InputStream inputStream = dataSource1.getInputStream();
        ResourceDataSource resource1 = 
            factory.createResource("test", "text/plain", "text", "first", inputStream);
        inputStream.close();
        
        ByteArrayDataSource dataSource2 = new ByteArrayDataSource("testdata".getBytes(), "text/plain");        
        inputStream = dataSource2.getInputStream();
        ResourceDataSource resource2 = 
            factory.createResource("test", "text/plain", "text", "second", dataSource2.getInputStream());
        inputStream.close();

        ResourceList resources = new ResourceList();
        resources.add(resource1);
        resources.add(resource2);
        sendExchange.getIn().setBody(resources);
        sendExchange.setPattern(ExchangePattern.InOut);

        mock.expectedMessageCount(1);
        mock.whenAnyExchangeReceived(outputGenerator);
        
        Exchange output = producerTemplate.send(ENDPOINT_SEND_ONLY, sendExchange);

        mock.assertIsSatisfied();

        assertEquals("testoutput", output.getOut().getBody(String.class));
        
        Map<String, String> receivedContent = outputGenerator.getReceivedContent();        
        assertEquals(2, receivedContent.size());
        assertEquals("blu bla", receivedContent.get("first"));
        assertEquals("testdata", receivedContent.get("second"));
    }
    
    @Test
    public void testStoreDoesNotHandleNonHttpMessage() throws Exception {
        Exchange sendExchange = new DefaultExchange(camelContext);
        sendExchange.getIn().setBody("nix http");

        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("nix http");
        
        producerTemplate.send(ENDPOINT_NON_HTTP, sendExchange);

        mock.assertIsSatisfied();
    }
    
    /**
     * Regression test of a problem I saw when debugging the HTTP stuff. Closing 
     * the input stream wasn't handled as expected. This test ensures that an 
     * exception during the send of the HTTP message closes the input stream 
     */
    @Test
    public void testFetchHandlesCorruptStreamMultipart() throws Exception {
        Exchange sendExchange = new DefaultExchange(camelContext);
        CorruptedInputStream inputStream = new CorruptedInputStream();        
        ResourceCompatibleDataSource dataSource1 = createMock(ResourceCompatibleDataSource.class);
        expect(dataSource1.getInputStream()).andReturn(inputStream).anyTimes();
        expect(dataSource1.getContentType()).andReturn("text/plain").anyTimes();
        expect(dataSource1.getName()).andReturn("test").anyTimes();
        expect(dataSource1.getContentLength()).andReturn(245L).anyTimes();
        replay(dataSource1);

        ByteArrayDataSource dataSource2 = new ByteArrayDataSource("testdata".getBytes(), "text/plain");
        
        ResourceDataSource resource1 = new ResourceDataSource("first", dataSource1);
        ResourceDataSource resource2 = new ResourceDataSource("second", dataSource2);

        ResourceList resources = new ResourceList();
        resources.add(resource1);
        resources.add(resource2);
        sendExchange.getIn().setBody(resources);

        mock.expectedMessageCount(0);

        Exchange output = producerTemplate.send(ENDPOINT_SEND_ONLY, sendExchange);
        
        mock.assertIsSatisfied();
        
        assertTrue("Stream was not closed", inputStream.isClosed());

        assertEquals(IOException.class, output.getException().getClass());
    }
    
    /**
     * Regression test of a problem I saw when debugging the HTTP stuff. Closing 
     * the input stream wasn't handled as expected. This test ensures that an 
     * exception during the send of the HTTP message closes the input stream 
     */
    @Test
    public void testFetchHandlesCorruptStreamSingle() throws Exception {
        Exchange sendExchange = new DefaultExchange(camelContext);
        CorruptedInputStream inputStream = new CorruptedInputStream();        
        ResourceCompatibleDataSource dataSource = createMock(ResourceCompatibleDataSource.class);
        expect(dataSource.getInputStream()).andReturn(inputStream).anyTimes();
        expect(dataSource.getContentType()).andReturn("text/plain").anyTimes();
        expect(dataSource.getName()).andReturn("test").anyTimes();
        expect(dataSource.getContentLength()).andReturn(245L).anyTimes();
        replay(dataSource);
        
        ResourceDataSource resource = new ResourceDataSource("content", dataSource);

        ResourceList resources = new ResourceList();
        resources.add(resource);
        sendExchange.getIn().setBody(resources);

        mock.expectedMessageCount(0);

        Exchange output = producerTemplate.send(ENDPOINT_SEND_ONLY, sendExchange);
        
        mock.assertIsSatisfied();

        assertTrue("Stream was not closed", inputStream.isClosed());

        assertEquals(ProtocolException.class, output.getException().getClass());
    }

    protected final class TestOutputGenerator implements Processor {
        private final String output;
        private final Map<String, String> receivedContent = new HashMap<String, String>();
        private String receivedBody;
        private ResourceList receivedResources;

        private TestOutputGenerator(String output) {
            this.output = output;
        }

        public String getReceivedBody() {
            return receivedBody;
        }

        @Override
        public void process(Exchange exchange) throws Exception {
            ResourceList resources = exchange.getIn().getBody(ResourceList.class);
            receivedResources = resources;
            for (ResourceDataSource resource : resources) {
                InputStream inputStream = resource.getInputStream();
                try {
                    receivedContent.put(resource.getId(), IOUtils.toString(inputStream));
                }
                finally {
                    inputStream.close();
                }
            }
            
            receivedBody = exchange.getIn().getBody(String.class);
            
            exchange.getOut().setBody(new ByteArrayInputStream(output.getBytes()));            
        }
        
        public Map<String, String> getReceivedContent() {
            return receivedContent;
        }
        
        public ResourceList getReceivedResources() {
            return receivedResources;
        }
    }

    private static class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String contentType;

        public ByteArrayDataSource(byte[] data, String contentType) {
            this.data = data;
            this.contentType = contentType;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("read-only data source");
        }

        public String getContentType() {
            return contentType;
        }

        public String getName() {
            return "test";
        }
    }
}
