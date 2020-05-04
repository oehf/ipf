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

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.ApplicationActivityBuilder;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context.xml"})
public class CamelAuditMessageQueueTest {

    @Autowired
    private AuditContext auditContext;

    @EndpointInject(value = "mock:mock")
    private MockEndpoint mock;

    @After
    public void tearDown() {
        mock.reset();
    }

    @Test
    public void testCamelEndpointAudit() {
        var auditMessage = new ApplicationActivityBuilder.ApplicationStart(EventOutcomeIndicator.Success, null).getMessage();
        auditContext.audit(auditMessage);

        var message = mock.assertExchangeReceived(0).getIn();
        var body = message.getBody(AuditMessage.class);
        var header1 = message.getHeader(CamelAuditMessageQueue.HEADER_NAMESPACE + ".destination.address");
        var header2 = message.getHeader(CamelAuditMessageQueue.HEADER_NAMESPACE + ".destination.port");

        assertEquals(EventIdCode.ApplicationActivity, body.getEventIdentification().getEventID());
        assertEquals("0.0.0.0", header1);
        assertEquals(-1, header2);
    }

}
