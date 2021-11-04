/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.hl7.extend;

import org.apache.camel.EndpointInject;
import org.apache.camel.Message;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Christian Ohr
 */
@ContextConfiguration(locations = {"/config/context-ack.xml"})
public class AcknowledgementExtensionTest extends AbstractExtensionTest {

    private static String MSH_EXPECTED = "||ACK^A01";
    private static String ERR1_EXPECTED = "ERR|^^^203&Unsupported version id&HL70357&&Don't like it";
    private static String ERR2_EXPECTED = "ERR|^^^207&Application internal error&HL70357&&Exception";


    @EndpointInject(value = "mock:output")
    private MockEndpoint mockOutput;

    private String resource = "/message/msg-01.hl7";

    @AfterEach
    public void myTearDown() throws Exception {
        mockOutput.reset();
    }

    @Test
    public void testRoute2() throws Exception {
        var message = inputMessage(resource);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input2", message);
        mockOutput.assertIsSatisfied();
        assertTrue(resultString(mockOutput).contains(MSH_EXPECTED));
    }

    @Test
    public void testRoute3() throws Exception {
        var message = inputMessage(resource);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input3", message);
        mockOutput.assertIsSatisfied();
        assertTrue(resultString(mockOutput).contains(ERR1_EXPECTED));
    }

    @Test
    public void testRoute4() throws Exception {
        var message = inputMessage(resource);
        mockOutput.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input4", message);
        mockOutput.assertIsSatisfied();
        var result = resultString(mockOutput);
        assertTrue(result.contains(ERR2_EXPECTED));
    }

    private String resultString(MockEndpoint mock) {
        return resultMessage(mock).getBody(String.class);
    }

    private Message resultMessage(MockEndpoint mock) {
        return mock.getExchanges().get(0).getIn();
    }

    private String inputMessage(String resource) {
        var is = getClass().getResourceAsStream(resource);
        return new Scanner(is, "UTF-8").useDelimiter("\\A").next();
    }

}
