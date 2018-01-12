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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

import static java.util.Objects.requireNonNull;

/**
 * Message Queue that sends off audit messages into a JMS queue. It is strongly recommended
 * that the connection factory implements a pool or caches connections for performance reasons.
 * Use an instance of {@link JmsAuditMessageListener} to asynchronously receive the audit
 * messages and send them off to a respository.
 *
 * @author Dmytro Rud
 * @author Christian Ohr
 *
 * @see JmsAuditMessageListener
 */
public class JmsAuditMessageQueue extends AbstractAuditMessageQueue {

    private static final Logger LOG = LoggerFactory.getLogger(JmsAuditMessageQueue.class);

    private final ConnectionFactory connectionFactory;
    private final String queueName;
    private final String userName;
    private final String password;

    /**
     * @param connectionFactory JMS connection factory
     * @param queueName         JMS destination of ATNA messages
     * @param userName          user name for JMS authentication
     * @param password          user password for JMS authentication
     * @throws JMSException if no connection could be established
     */
    public JmsAuditMessageQueue(ConnectionFactory connectionFactory, String queueName,
                                String userName, String password) {
        this.connectionFactory = requireNonNull(connectionFactory, "ConnectionFactory must not be null");
        this.queueName = queueName != null ? queueName : "atna-audit";
        this.userName = userName;
        this.password = password;
    }


    @Override
    protected void handle(AuditContext auditContext, String... auditRecords) throws Exception {
        Connection connection = connectionFactory.createConnection(userName, password);
        connection.start();
        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);
            for (String auditMessage : auditRecords) {
                TextMessage message = session.createTextMessage(auditMessage);
                producer.send(message);
            }
        } finally {
            if (connection != null) connection.close();
        }
    }

    @Override
    public void flush() {
        // nop
    }

}
