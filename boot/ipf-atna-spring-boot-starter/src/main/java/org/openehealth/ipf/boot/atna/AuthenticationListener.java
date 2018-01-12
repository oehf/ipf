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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.UserAuthenticationBuilder;
import org.springframework.boot.actuate.security.AbstractAuthenticationAuditListener;
import org.springframework.boot.actuate.security.AuthenticationAuditListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public class AuthenticationListener extends AbstractAuthenticationAuditListener {

    private final AuditContext auditContext;
    private final AuthenticationAuditListener delegateListener;

    public AuthenticationListener(AuditContext auditContext) {
        this.auditContext = requireNonNull(auditContext);
        this.delegateListener = new AuthenticationAuditListener();
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent authenticationEvent) {
        delegateListener.onApplicationEvent(authenticationEvent);

        EventOutcomeIndicator outcome = authenticationEvent instanceof AbstractAuthenticationFailureEvent ?
                EventOutcomeIndicator.MajorFailure :
                EventOutcomeIndicator.Success;

        Object details = authenticationEvent.getAuthentication().getDetails();
        if (details instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) details;
            Object principal = authenticationEvent.getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                UserAuthenticationBuilder builder = new UserAuthenticationBuilder.Login(outcome)
                                .setAuditSource(auditContext);
                if (userDetails.getUsername() != null) {
                    builder.setAuthenticatedParticipant(
                            userDetails.getUsername(),
                            webAuthenticationDetails.getRemoteAddress());
                };
                if (webAuthenticationDetails.getRemoteAddress() != null) {
                    builder.setAuthenticatingSystemParticipant(
                            auditContext.getSendingApplication(),
                            webAuthenticationDetails.getRemoteAddress());
                }

                try {
                    auditContext.audit(builder.getMessage());
                } catch (Exception e) {
                    throw new AuditException("Auditing failed: ", e);
                }
            }
        }
    }
}
