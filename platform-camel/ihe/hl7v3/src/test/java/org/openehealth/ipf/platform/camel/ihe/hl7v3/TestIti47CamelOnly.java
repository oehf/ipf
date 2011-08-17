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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import java.io.IOException;
import java.io.InputStream;

import groovy.lang.Closure;
import org.apache.camel.Exchange;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.map.BidiMappingService;
import org.openehealth.ipf.commons.map.extend.MappingExtension;
import org.openehealth.ipf.modules.hl7.extend.HapiModelExtension;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;
import org.springframework.core.io.ClassPathResource;

/**
 * Test for Camel-only route.
 * @author Dmytro Rud
 */
public class TestIti47CamelOnly extends StandardTestContainer {

    private static String requestMessage, responseMessage;

    
    @BeforeClass
    public static void setUpClass() {
        BidiMappingService mappingService = new BidiMappingService();
        mappingService.setMappingScript(new ClassPathResource("META-INF/map/hl7-v2-v3-translation.map"));
        MappingExtension mappingExtension = new MappingExtension();
        mappingExtension.setMappingService(mappingService);
        ((Closure) mappingExtension.getExtensions()).call();

        HapiModelExtension hapiExtension = new HapiModelExtension();
        hapiExtension.setMappingService(mappingService);
        ((Closure) hapiExtension.getExtensions()).call();

        requestMessage  = readFile("translation/pdq/v3/PDQ.xml");
        responseMessage = readFile("translation/pdq/v2/PDQ_Response.hl7");
        startServer(new CXFServlet(), "camel-only.xml");
    }
    
    
    @Test
    public void testCamelOnly() {
        String endpointUri = "pdqv3-iti47://localhost:" + getPort() + "/iti47Service";
        Exchange responseExchange = (Exchange) send(endpointUri, getRequestMessage());
        String response = Exchanges.resultMessage(responseExchange).getBody(String.class);
        Assert.assertTrue(response.contains("<typeCode code=\"AA\"/>"));
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
