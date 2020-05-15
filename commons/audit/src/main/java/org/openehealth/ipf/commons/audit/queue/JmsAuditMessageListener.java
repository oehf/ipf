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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditMetadataProvider;
import org.openehealth.ipf.commons.audit.DefaultAuditMetadataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.audit.queue.AbstractAuditMessageQueue.X_IPF_ATNA_APPLICATION;
import static org.openehealth.ipf.commons.audit.queue.AbstractAuditMessageQueue.X_IPF_ATNA_HOSTNAME;
import static org.openehealth.ipf.commons.audit.queue.AbstractAuditMessageQueue.X_IPF_ATNA_PROCESSID;
import static org.openehealth.ipf.commons.audit.queue.AbstractAuditMessageQueue.X_IPF_ATNA_TIMESTAMP;

/**
 * JMS Message Listener that receives audit messages from a queue and sends them
 * to an audit repository. It is recommended to use infrastructure classes such
 * as Spring's JMS MessageListenerContainers to control transactional behavior,
 * message redelivery and other features.
 * <p>
 * This class supports JMS headers transferred by {@link JmsAuditMessageQueue} and
 * reuses them for setting the RFC 5424 headers of the actual syslog audit.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class JmsAuditMessageListener implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(JmsAuditMessageListener.class);

    private final AuditContext auditContext;

    public JmsAuditMessageListener(AuditContext auditContext) {
        this.auditContext = requireNonNull(auditContext, "AuditContext must not be null");
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            String hostname = message.getStringProperty(X_IPF_ATNA_HOSTNAME);
            String processID = message.getStringProperty(X_IPF_ATNA_PROCESSID);
            String application = message.getStringProperty(X_IPF_ATNA_APPLICATION);
            String timestamp = message.getStringProperty(X_IPF_ATNA_TIMESTAMP);

            AuditMetadataProvider defaultProvider = auditContext.getAuditMetadataProvider();
            DefaultAuditMetadataProvider auditMetadataProvider = new DefaultAuditMetadataProvider(
                    hostname != null ? hostname : defaultProvider.getHostname(),
                    processID != null ? processID : defaultProvider.getProcessID(),
                    application != null ? application : defaultProvider.getSendingApplication(),
                    timestamp != null ? timestamp : defaultProvider.getTimestamp()
            );
            auditContext.getAuditTransmissionProtocol().send(auditContext, auditMetadataProvider, text);
        } catch (JMSException jmsException1) {
            LOG.error("Could not obtain text from JMS message", jmsException1);
        } catch (Exception e) {
            LOG.warn("Could not send audit message, rolling back", e);
            throw new RuntimeException(e);
        }
    }

}
