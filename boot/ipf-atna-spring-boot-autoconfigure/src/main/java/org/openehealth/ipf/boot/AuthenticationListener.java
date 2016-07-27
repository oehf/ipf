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

package org.openehealth.ipf.boot;

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.springframework.boot.actuate.security.AbstractAuthenticationAuditListener;
import org.springframework.boot.actuate.security.AuthenticationAuditListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 */
public class AuthenticationListener extends AbstractAuthenticationAuditListener {

    private final IHEAuditor actorAuditor;
    private final AuthenticationAuditListener delegateListener;

    public AuthenticationListener(IHEAuditor actorAuditor) {
        this.actorAuditor = actorAuditor;
        this.delegateListener = new AuthenticationAuditListener();
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent authenticationEvent) {
        delegateListener.onApplicationEvent(authenticationEvent);

        RFC3881EventOutcomeCodes outcome = authenticationEvent instanceof AbstractAuthenticationFailureEvent ?
                RFC3881EventOutcomeCodes.MAJOR_FAILURE :
                RFC3881EventOutcomeCodes.SUCCESS;
        WebAuthenticationDetails details = (WebAuthenticationDetails) authenticationEvent.getAuthentication().getDetails();
        UserDetails ud = (UserDetails) authenticationEvent.getAuthentication().getPrincipal();
        actorAuditor.auditUserAuthenticationLoginEvent(
                outcome,
                false,
                ud.getUsername(),
                details.getRemoteAddress(),
                details.getRemoteAddress());
    }
}
