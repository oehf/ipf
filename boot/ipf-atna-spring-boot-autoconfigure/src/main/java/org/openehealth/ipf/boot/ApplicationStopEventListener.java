package org.openehealth.ipf.boot;

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 *
 */
public class ApplicationStopEventListener implements ApplicationListener<ContextClosedEvent> {

    private final IHEAuditor actorAuditor;

    public ApplicationStopEventListener(IHEAuditor actorAuditor) {
        this.actorAuditor = actorAuditor;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        actorAuditor.auditActorStopEvent(
                RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS,
                contextClosedEvent.getApplicationContext().getApplicationName(),
                System.getProperty("user.name"));
    }
}
