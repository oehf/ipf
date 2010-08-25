/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3;

import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

/**
 * Test for Camel-only route.
 * @author Dmytro Rud
 */
public class TestCamelOnly extends StandardTestContainer {

    private static String requestMessage, responseMessage;

    
    @BeforeClass
    public static void setUpClass() {
        requestMessage  = readFile("translation/pdq/v3/PDQ.xml");
        responseMessage = readFile("translation/pdq/v2/PDQ_Response.hl7");
        startServer(new CXFServlet(), "camel-only.xml");
    }
    
    
    @Test
    public void testCamelOnly() {
        String endpointUri = "pdqv3-iti47://localhost:" + getPort() + "/iti47Service";
        Exchange responseExchange = (Exchange) send(endpointUri, getRequestMessage());
        String response = Exchanges.resultMessage(responseExchange).getBody(String.class);
        assert response.contains("<text>Message validation failed");
    }

    
    private static String readFile(String fn) {
        try {
            InputStream is = CamelOnlyRouteBuilder.class.getClassLoader().getResourceAsStream(fn);
            String s = IOUtils.toString(is);
            IOUtils.closeQuietly(is);
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
    public static String getRequestMessage() {
        return requestMessage;
    }

    
    public static String getResponseMessage() {
        return responseMessage;
    }

}
