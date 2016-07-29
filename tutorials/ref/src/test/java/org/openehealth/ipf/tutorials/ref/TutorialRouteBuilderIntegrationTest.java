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
package org.openehealth.ipf.tutorials.ref;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author Martin Krasser
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TutorialRouteBuilderIntegrationTest extends Assert {

    private static Client client;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        client = new Client();
        client.setContentType("text/xml");
        client.setServerUrl(new URL("http://localhost:8081/tutorial"));
        client.setDefaultMaxConnectionsPerHost(20);
        client.setHandler(new AssertingResponseHandler());
    }

    @Test
    public void testSendOrders() throws Exception {
        testSendOrder("/order/order-animals.xml");
        testSendOrder("/order/order-books.xml");
    }
    
    private void testSendOrder(String resource) throws Exception {
        InputStream order = getClass().getResourceAsStream(resource);
        try {
            client.execute(order);
        } finally {
            IOUtils.closeQuietly(order);
        }
        
    }
    
    private static class AssertingResponseHandler implements ResponseHandler {

        @Override
        public void handleResponse(InputStream response) throws Exception {
            String s = IOUtils.toString(response, Charset.defaultCharset());
            assertEquals("message valid", s);
        }
        
    }
    
}
