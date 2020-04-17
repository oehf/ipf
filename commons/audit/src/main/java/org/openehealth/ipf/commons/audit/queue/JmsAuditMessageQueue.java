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

import javax.jms.*;

import static java.util.Objects.requireNonNull;

/**
 * Message Queue that sends audit messages into a JMS queue. It is strongly recommended
 * that the connection factory implements a pool or caches connections for performance reasons.
 * Use an instance of {@link JmsAuditMessageListener} to asynchronously receive the audit
 * messages and send them to a repository.
 * <p>
 * This is primarily meant to send audit messages to a JMS-based relay that eventually sends the
 * audit record to an audit repository. Therefore, RFC 5425 metadata is placed in X-IPF-ATNA-*
 * JMS properties, so the listener is able to restore them.
 *
 * @author Dmytro Rud
 * @author Christian Ohr
 * @see JmsAuditMessageListener
 * @since 3.5
 */
public class JmsAuditMessageQueue extends AbstractAuditMessageQueue {

    private final ConnectionFactory connectionFactory;
    private final String queueName;
    private final String userName;
    private final String password;

    /**
     * @param connectionFactory JMS connection factory
     * @param queueName         JMS destination of ATNA messages
     * @param userName          user name for JMS authentication
     * @param password          user password for JMS authentication
     */
    public JmsAuditMessageQueue(ConnectionFactory connectionFactory, String queueName,
                                String userName, String password) {
        this.connectionFactory = requireNonNull(connectionFactory, "ConnectionFactory must not be null");
        this.queueName = queueName != null ? queueName : "atna-audit";
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected void handle(AuditContext auditContext, String auditMessage) {
        try {
            Connection connection = connectionFactory.createConnection(userName, password);
            connection.start();
            try {
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Queue queue = session.createQueue(queueName);
                MessageProducer producer = session.createProducer(queue);
                TextMessage message = session.createTextMessage(auditMessage);
                message.setStringProperty(X_IPF_ATNA_TIMESTAMP, auditContext.getAuditMetadataProvider().getTimestamp());
                message.setStringProperty(X_IPF_ATNA_HOSTNAME, auditContext.getAuditMetadataProvider().getHostname());
                message.setStringProperty(X_IPF_ATNA_PROCESSID, auditContext.getAuditMetadataProvider().getProcessID());
                message.setStringProperty(X_IPF_ATNA_APPLICATION, auditContext.getSendingApplication());
                producer.send(message);
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            auditContext.getAuditExceptionHandler().handleException(auditContext, e, auditMessage);
        }
    }

}
