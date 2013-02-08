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
package org.openehealth.ipf.platform.camel.cda.extend;

import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.EndpointInject;
import org.apache.camel.Message;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.openehealth.ipf.modules.cda.CDAR2Parser;
import org.openehealth.ipf.modules.cda.CDAR2Renderer;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import static org.junit.Assert.assertEquals;

/**
 * @author Christian Ohr
 */
@ContextConfiguration(locations = { "/config/context-extend.xml" })
public class CDAModelExtensionTest extends AbstractExtensionTest {

    private String cdaExample = "message/SampleCDADocument.xml";
    private String ccdExample = "message/SampleCCDDocument.xml";

    @EndpointInject(uri="mock:error")
    protected MockEndpoint mockError;
    
    
    @Test
    public void testMarshalDefault() throws Exception {
        testMarshalCDA("direct:input1", cdaExample);
        testMarshalCDA("direct:input1", ccdExample);
    }
    
    @Test
    public void testUnmarshalDefault() throws Exception {
        testUnmarshalCDA("direct:input2", cdaExample);
        testUnmarshalCDA("direct:input2", ccdExample);
    }    

    

    private void testMarshalCDA(String endpoint, String file) throws Exception {
        mockOutput.reset();
        mockError.reset();
        ClinicalDocument message = inputMessage(file);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody(endpoint, message);
        mockOutput.assertIsSatisfied();
        assertEquals(messageAsString(message), resultString());
    }
    
    private void testUnmarshalCDA(String endpoint, String file) throws Exception {
        mockOutput.reset();
        mockError.reset();
        InputStream stream = inputStream(file);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody(endpoint, stream);
        mockOutput.assertIsSatisfied();
        assertEquals(messageAsString(inputMessage(file)), messageAsString(resultAdapter()));
    }
    
    private void testValidateCDA(String endpoint, String file) throws Exception {
        mockOutput.reset();
        mockError.reset();
        InputStream stream = inputStream(file);
        mockOutput.expectedMessageCount(1);
        mockError.expectedMessageCount(0);
        producerTemplate.sendBody(endpoint, stream);
        mockOutput.assertIsSatisfied();
        mockError.assertIsSatisfied();
    }

    private String resultString() {
        return resultMessage().getBody(String.class);
    }

    private ClinicalDocument resultAdapter() {
        return resultMessage().getBody(ClinicalDocument.class);
    }

    private Message resultMessage() {
        return mockOutput.getExchanges().get(0).getIn();
    }   
    
    private static InputStream inputStream(String resource) throws IOException {
        return new ClassPathResource(resource).getInputStream();
    }

    private ClinicalDocument inputMessage(String resource) throws IOException {
        return new CDAR2Parser().parse(inputStream(resource), (Object[])null);
    }
    
    private String messageAsString(ClinicalDocument message) {
        return new CDAR2Renderer().render(message, (Object[])null);
    }
    
}
