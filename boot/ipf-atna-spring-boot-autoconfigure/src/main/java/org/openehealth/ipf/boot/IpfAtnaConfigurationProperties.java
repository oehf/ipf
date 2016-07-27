package org.openehealth.ipf.boot;

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

}
