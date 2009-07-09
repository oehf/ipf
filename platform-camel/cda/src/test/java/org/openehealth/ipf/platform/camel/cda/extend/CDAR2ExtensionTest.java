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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.EndpointInject;
import org.apache.camel.Message;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.openehealth.ipf.modules.cda.CDAR2Parser;
import org.openehealth.ipf.modules.cda.CDAR2Renderer;
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Christian Ohr
 */
@ContextConfiguration(locations = { "/config/context-extend.xml" })
public class CDAR2ExtensionTest extends AbstractExtensionTest {

    private String resource = "message/SampleCDADocument.xml";

    @EndpointInject(uri="mock:error")
    protected MockEndpoint mockError;
    
    @After
    public void tearDown() throws Exception {
        mockOutput.reset();
        mockError.reset();
    }
    
    @Test
    public void testMarshalDefault() throws Exception {
        testMarshal("direct:input1");
    }
    
    @Test
    public void testUnmarshalDefault() throws Exception {
        testUnmarshal("direct:input2");
    }    
    
    @Test
    public void testValidateSuccess() throws Exception {
        testValidate("direct:input3");
    }
    

    private void testMarshal(String endpoint) throws Exception {
        POCDMT000040ClinicalDocument message = inputMessage(resource);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody(endpoint, message);
        mockOutput.assertIsSatisfied();
        assertEquals(messageAsString(message), resultString());
    }
    
    private void testUnmarshal(String endpoint) throws Exception {
        InputStream stream = inputStream(resource);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody(endpoint, stream);
        mockOutput.assertIsSatisfied();
        assertEquals(messageAsString(inputMessage(resource)), messageAsString(resultAdapter()));
    }
    
    private void testValidate(String endpoint) throws Exception {
        InputStream stream = inputStream(resource);
        mockOutput.expectedMessageCount(1);
        mockError.expectedMessageCount(0);
        producerTemplate.sendBody(endpoint, stream);
        mockOutput.assertIsSatisfied();
        mockError.assertIsSatisfied();
    }

    private String resultString() {
        return resultMessage().getBody(String.class);
    }

    private POCDMT000040ClinicalDocument resultAdapter() {
        return (POCDMT000040ClinicalDocument)resultMessage().getBody();
    }

    private Message resultMessage() {
        return mockOutput.getExchanges().get(0).getIn();
    }   
    
    private static InputStream inputStream(String resource) throws IOException {
        return new ClassPathResource(resource).getInputStream();
    }

    private POCDMT000040ClinicalDocument inputMessage(String resource) throws IOException {
        return new CDAR2Parser().parse(inputStream(resource), (Object[])null);
    }
    
    private String messageAsString(POCDMT000040ClinicalDocument message) {
        return new CDAR2Renderer().render(message, (Object[])null);
    }
    
}
