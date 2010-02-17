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
package org.openehealth.ipf.platform.camel.ihe.atna.util;

import static org.junit.Assert.*;
import static org.openhealthtools.ihe.atna.auditor.PIXConsumerAuditor.getAuditor;
import static org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;

import org.apache.camel.EndpointInject;
import org.apache.camel.Message;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context.xml" })
public class CamelEndpointSenderTest {

    @EndpointInject(uri="mock:mock")
    private MockEndpoint mock;
    
    @After
    public void tearDown() {
        mock.reset();
    }
    
    @Test
    public void testCamelEndpointAudit() throws Exception {
        getAuditor().auditPIXQueryEvent(SUCCESS, "foo:abc.def", "A", "B", "C", "D", "E", "F", new String[] {"1"});
        Message message = mock.assertExchangeReceived(0).getIn();
        String body = message.getBody(String.class);
        Object header1 = message.getHeader(CamelEndpointSender.HEADER_NAMESPACE + ".destination.address");
        Object header2 = message.getHeader(CamelEndpointSender.HEADER_NAMESPACE + ".destination.port");
        assertTrue(body.contains("<ActiveParticipant UserID=\"A|B\" UserIsRequestor=\"false\">"));
        assertEquals("0.0.0.0", header1);
        assertEquals(-1, header2);
    }
    
}
