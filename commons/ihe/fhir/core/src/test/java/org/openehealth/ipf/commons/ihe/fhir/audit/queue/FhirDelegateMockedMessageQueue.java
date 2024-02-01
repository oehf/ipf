package org.openehealth.ipf.commons.ihe.fhir.audit.queue;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.queue.AbstractMockedAuditMessageQueue;
import org.openehealth.ipf.commons.audit.queue.AsynchronousAuditMessageQueue;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.openehealth.ipf.commons.audit.queue.SynchronousAuditMessageQueue;

import java.util.List;

public class FhirDelegateMockedMessageQueue implements AbstractMockedAuditMessageQueue {

    private final AuditMessageQueue delegate;

    public FhirDelegateMockedMessageQueue() {
        this.delegate = new AsynchronousAuditMessageQueue();
    }

    @Override
    public void audit(AuditContext auditContext, AuditMessage... auditMessages) {
        delegate.audit(auditContext, auditMessages);
    }

    @Override
    public void flush() {
        delegate.flush();
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public List<AuditMessage> getMessages() {
        return null;
    }

    @Override
    public void clear() {

    }
}
