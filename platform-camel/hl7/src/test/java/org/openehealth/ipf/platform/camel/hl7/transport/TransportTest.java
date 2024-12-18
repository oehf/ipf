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
package org.openehealth.ipf.platform.camel.hl7.transport;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author Martin Krasser
 */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = { "/config/context-transport.xml" })
public class TransportTest {

    @Autowired
    protected ProducerTemplate producerTemplate;
    
    @EndpointInject(value="mock:output")
    protected MockEndpoint mockOutput;
    
    @AfterEach
    public void tearDown() {
        mockOutput.reset();
    }

    @Test
    public void testMessage02() throws Exception {
        var message = inputMessage("message/msg-02.hl7");
        var content = IOUtils.toString(getClass().getResourceAsStream("/message/msg-02.content"), Charset.defaultCharset());
        mockOutput.expectedBodiesReceived(content);
        producerTemplate.sendBody("netty:tcp://127.0.0.1:8888?sync=true&decoders=#hl7decoder&encoders=#hl7encoder", message);
        mockOutput.assertIsSatisfied();
    }

    private static String inputMessage(String resource) {
        return new Scanner(TransportTest.class.getResourceAsStream("/" + resource)).useDelimiter("\\A").next();
    }
    
}
