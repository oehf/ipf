package org.openehealth.ipf.boot;

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
    private String securityDomainName = "bootSecurityDomain";

    /**
     * Sets the Audit Message Queue class to be used for sending ATNA records
     */
    private Class<? extends AuditMessageQueue> auditQueueClass = SynchronousAuditQueue.class;

    /**
     * Sets the Audit Sender class to be used for sending ATNA records
     */
    private Class<? extends AuditMessageSender> auditSenderClass = UDPSyslogSenderImpl.class;


    public Class<? extends AuditMessageQueue> getAuditQueueClass() {
        return auditQueueClass;
    }

    public void setAuditQueueClass(Class<? extends AuditMessageQueue> auditQueueClass) {
        this.auditQueueClass = auditQueueClass;
    }

    public Class<? extends AuditMessageSender> getAuditSenderClass() {
        return auditSenderClass;
    }

    public void setAuditSenderClass(Class<? extends AuditMessageSender> auditSenderClass) {
        this.auditSenderClass = auditSenderClass;
    }

    public String getSecurityDomainName() {
        return securityDomainName;
    }

    public void setSecurityDomainName(String securityDomainName) {
        this.securityDomainName = securityDomainName;
    }



}
