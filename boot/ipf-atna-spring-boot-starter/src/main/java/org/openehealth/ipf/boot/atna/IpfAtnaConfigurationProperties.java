/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.boot.atna;

import lombok.Getter;
import lombok.Setter;
import org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue;
import org.openhealthtools.ihe.atna.auditor.queue.SynchronousAuditQueue;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;
import org.openhealthtools.ihe.atna.auditor.sender.UDPSyslogSenderImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@ConfigurationProperties(prefix = "ipf.atna")
public class IpfAtnaConfigurationProperties {

    /**
     * Security Domain name
     */
    @Getter @Setter
    private String securityDomainName = "bootSecurityDomain";

    /**
     * Sets the Audit Message Queue class to be used for sending ATNA records
     */
    @Getter @Setter
    private Class<? extends AuditMessageQueue> auditQueueClass = SynchronousAuditQueue.class;

    /**
     * Sets the Audit Sender class to be used for sending ATNA records
     */
    @Getter @Setter
    private Class<? extends AuditMessageSender> auditSenderClass = UDPSyslogSenderImpl.class;

    /**
     * Sets the host of the audit repository
     */
    @Getter @Setter
    private String repositoryHost = "localhost";

    /**
     * Sets the port of the audit repository
     */
    @Getter @Setter
    private int repositoryPort = 514;

    @Getter @Setter
    private String auditSourceId;

    @Getter @Setter
    private String auditEnterpriseSiteId;
}
