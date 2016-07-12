package org.openehealth.ipf.boot;

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 *
 */
public class ApplicationStartEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private final IHEAuditor actorAuditor;

    public ApplicationStartEventListener(IHEAuditor actorAuditor) {
        this.actorAuditor = actorAuditor;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        actorAuditor.auditActorStartEvent(
                RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS,
                contextRefreshedEvent.getApplicationContext().getApplicationName(),
                System.getProperty("user.name"));
    }
}
