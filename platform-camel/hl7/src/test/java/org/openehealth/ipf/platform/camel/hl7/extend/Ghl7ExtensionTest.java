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
package org.openehealth.ipf.platform.camel.hl7.extend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.Message;
import org.junit.Ignore;
import org.junit.Test;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v22.message.ADT_A01;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/config/context-extend.xml" })
public class Ghl7ExtensionTest extends AbstractExtensionTest {

    private String resource = "message/msg-01.hl7";
    private String resourceUTF8 = "message/msg-01-utf8.hl7";
    
    @Test
    public void testMarshalDefault() throws Exception {
        testMarshal("direct:input1");
    }

    @Test
    public void testMarshal() throws Exception {
        testMarshal("direct:input2");
    }

    @Test
    public void testUnmarshal() throws Exception {
        testUnmarshal("direct:input3");
    }

    @Test
    public void testValidateDefault() throws Exception {
        testUnmarshal("direct:input4");
    }
    
    // TODO fails on Hudson
    @Ignore
    public void testUnmarshalMarshalOtherEncoding() throws Exception {
        InputStream stream = inputStream(resourceUTF8);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input5", stream);
        mockOutput.assertIsSatisfied();
        Message result = resultMessage();
        // We expect an ByteArray
        assertEquals("[B", result.getBody().getClass().getName());
        String s = new String(((byte[])result.getBody()), "ISO-8859-1");
        assertTrue(s.contains("Nächname"));
        assertTrue(s.contains("Vörname"));
    }

    private void testMarshal(String endpoint) throws Exception {
        MessageAdapter<ADT_A01> message = inputMessage(resource);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody(endpoint, message);
        mockOutput.assertIsSatisfied();
        assertEquals(message.toString(), resultString());
    }
    
    private void testUnmarshal(String endpoint) throws Exception {
        InputStream stream = inputStream(resource);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody(endpoint, stream);
        mockOutput.assertIsSatisfied();
        assertEquals(inputMessage(resource).toString(), resultAdapter().toString());
    }

    private String resultString() {
        return resultMessage().getBody(String.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractMessage> MessageAdapter<T> resultAdapter() {
        return (MessageAdapter<T>)resultMessage().getBody();
    }

    private Message resultMessage() {
        return mockOutput.getExchanges().get(0).getIn();
    }
    
    private static InputStream inputStream(String resource) throws IOException {
        return new ClassPathResource(resource).getInputStream();
    }

    private static <T extends AbstractMessage> MessageAdapter<T> inputMessage(String resource) {
        return MessageAdapters.load(resource);
    }
    
}
