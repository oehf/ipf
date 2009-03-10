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
package org.openehealth.ipf.platform.camel.http;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.junit.Test;

public class IpfHttpComponentTest {
    @Test
    public void testDownload() throws Exception {
        CamelContext context = new DefaultCamelContext();
        Component component = new HttpComponent();
        context.addComponent("http", component);
        
        final InputStream bigStream = new HugeContentInputStream();
        
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                errorHandler(noErrorHandler());
                
                from("direct:start")
                    .to("http://localhost:8745/download")
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            InputStream inputStream = exchange.getIn().getBody(InputStream.class);
                            assertEquals(HugeContentInputStream.CONTENT_SIZE, getLength(inputStream));
                            exchange.getOut().setBody("done");
                        }
                    });
                
                from("jetty:http://localhost:8745/download")
                    .transform().constant(bigStream);
            }            
        });
        
        context.start();
        
        ProducerTemplate<Exchange> template = context.createProducerTemplate();
        
        Exchange exchange = new DefaultExchange(context);        
        Exchange result = template.send("direct:start", exchange);
        assertNull(result.getFault(false));
        assertEquals("done", result.getOut().getBody());
        
        context.stop();
    }
    
    @Test
    public void testUpload() throws Exception {
        CamelContext context = new DefaultCamelContext();
        Component component = new HttpComponent();
        context.addComponent("http", component);
        
        final InputStream bigStream = new HugeContentInputStream();
        final RequestEntity requestEntity = new InputStreamRequestEntity(bigStream, HugeContentInputStream.CONTENT_SIZE);
        
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                errorHandler(noErrorHandler());
                
                from("direct:start")
                    .transform().constant(requestEntity)
                    .to("http://localhost:8745/upload");
                
                from("jetty:http://localhost:8745/upload")
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            InputStream inputStream = exchange.getIn().getBody(InputStream.class);
                            assertEquals(HugeContentInputStream.CONTENT_SIZE, getLength(inputStream));
                            exchange.getOut().setBody("done");
                        }
                    });                    
            }
        });
        
        context.start();
        
        ProducerTemplate<Exchange> template = context.createProducerTemplate();
        
        Exchange exchange = new DefaultExchange(context);        
        Exchange result = template.send("direct:start", exchange);        
        assertNull(result.getFault(false));
        assertEquals("done", result.getOut().getBody(String.class));
        
        context.stop();
    }

    public static int getLength(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length = 0;
        boolean done = false;
        while (!done) {
            int read = inputStream.read(buffer, 0, buffer.length);
            if (read == -1) {
                done = true;
            }
            else {
                length += read;
            }
        }
        return length;
    }    
}
