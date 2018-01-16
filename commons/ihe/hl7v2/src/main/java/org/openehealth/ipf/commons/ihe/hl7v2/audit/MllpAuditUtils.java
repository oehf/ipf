/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2.audit;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.EventTypeCode;
import org.openehealth.ipf.commons.audit.event.SecurityAlertBuilder;
import org.openehealth.ipf.commons.audit.model.AuditMessage;


abstract public class MllpAuditUtils {

    /**
     * Audits an authentication node failure.
     *
     * @param hostAddress the address of the node that is responsible for the failure.
     */
    public static AuditMessage auditAuthenticationNodeFailure(AuditContext auditContext, String message, String hostAddress) {
        return new SecurityAlertBuilder(EventOutcomeIndicator.SeriousFailure, message, EventTypeCode.NodeAuthentication)
                .setAuditSource(auditContext.getAuditSourceId(), auditContext.getAuditEnterpriseSiteId())
                .addReportingActiveParticipant("IPF MLLP Component", null, null, null, null, false)
                .addAlertUriSubjectParticipantObject(hostAddress, null, null)
                .getMessage();
    }


}
