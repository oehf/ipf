/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.Collection;
import java.util.List;

import static org.openehealth.ipf.commons.ihe.core.atna.custom.CustomAuditorUtils.configureEvent;

/**
 * Implementation of HDM Auditors to send audit messages for
 * <ul>
 *     <li>ITI-59</li>
 * </ul>
 *
 * @author Dmytro Rud
 */
public class HpdAuditor extends IHEAuditor {

    public static HpdAuditor getAuditor() {
        AuditorModuleContext ctx = AuditorModuleContext.getContext();
        return (HpdAuditor) ctx.getAuditor(HpdAuditor.class);
    }

    public void auditIti59(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventActionCodes eventActionCode,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String directoryUri,
            String clientIpAddress,
            Collection<String> providerIds,
            List<CodedValueType> purposesOfUse)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        HpdEvent event = new HpdEvent(
                !serverSide,
                eventActionCode,
                eventOutcome,
                purposesOfUse);

        configureEvent(this, serverSide, event, replyToUri, userName, directoryUri, directoryUri, clientIpAddress);
        if (! EventUtils.isEmptyOrNull(providerIds)) {
            providerIds.forEach(event::addProviderParticipantObject);
        }
        audit(event);
    }

}
