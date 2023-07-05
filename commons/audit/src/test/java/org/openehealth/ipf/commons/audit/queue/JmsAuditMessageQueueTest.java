/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.*;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.ApplicationActivityBuilder;
import org.openehealth.ipf.commons.audit.protocol.RecordingAuditMessageTransmission;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Dmytro Rud
 */
@Disabled("JEE 10")
public class JmsAuditMessageQueueTest {

    private static final Logger LOG = LoggerFactory.getLogger(JmsAuditMessageQueueTest.class);

    private static final String JMS_BROKER_URL = "tcp://localhost:61616";
    private static final String JMS_QUEUE_NAME = "atna";

    private JmsAuditMessageQueue atnaQueue;
    private DefaultAuditContext auditContext;
    private RecordingAuditMessageTransmission recorder;


    @BeforeAll
    public static void beforeClass() throws Exception {
        Locale.setDefault(Locale.ENGLISH);

        var jmsBroker = new BrokerService();
        jmsBroker.addConnector(JMS_BROKER_URL);
        jmsBroker.setUseJmx(false);
        jmsBroker.setPersistent(false);
        jmsBroker.deleteAllMessages();
        jmsBroker.start();
    }

    @BeforeEach
    public void setup() {
        recorder = new RecordingAuditMessageTransmission();
        auditContext = new DefaultAuditContext();
        auditContext.setAuditTransmissionProtocol(recorder);
        auditContext.setAuditEnabled(true);
    }

    @AfterEach
    public void tearDown() {
        if (atnaQueue != null) {
            atnaQueue.shutdown();
        }
        recorder.shutdown();
    }

    @Test
    public void testActiveMQ() throws Exception {
        var connectionFactory = new ActiveMQConnectionFactory(JMS_BROKER_URL);
        var pooledConnectionFactory = new JmsPoolConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        var messageListener = new JmsAuditMessageListener(auditContext);

        // Setup Consumer
        var messageListenerContainer = new SimpleMessageListenerContainer();
        messageListenerContainer.setupMessageListener(messageListener);
        messageListenerContainer.setConnectionFactory(pooledConnectionFactory);
        messageListenerContainer.setDestinationName(JMS_QUEUE_NAME);
        messageListenerContainer.start();

        // Setup producer
        atnaQueue = new JmsAuditMessageQueue(pooledConnectionFactory, JMS_QUEUE_NAME, null, null);
        auditContext.setAuditMessageQueue(atnaQueue);

        sendAudit();
        Thread.sleep(500);

        assertEquals(1, recorder.getMessages().size());

    }

    private void sendAudit() {
        LOG.debug("Sending audit record");
        auditContext.audit(
                new ApplicationActivityBuilder.ApplicationStart(EventOutcomeIndicator.Success)
                        .setAuditSource(auditContext)
                        .setApplicationParticipant(
                                "appName",
                                null,
                                null,
                                AuditUtils.getLocalHostName())
                        .addApplicationStarterParticipant(System.getProperty("user.name"))
                        .getMessages());
    }


}
