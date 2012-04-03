/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.XDSAuditor;
import org.openhealthtools.ihe.atna.auditor.events.ihe.GenericIHEAuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * @author Dmytro Rud
 */
abstract class CustomAuditor extends XDSAuditor {

    protected void configureEvent(
            boolean serverSide,
            GenericIHEAuditEventMessage event,
            String replyToUri,
            String userName,
            String serverUri,
            String clientIpAddress)
    {
        event.setAuditSourceId(
                getAuditSourceId(),
                getAuditEnterpriseSiteId());

        // Set the source active participant
        event.addSourceActiveParticipant(
                replyToUri,
                serverSide ? null : getSystemAltUserId(),
                null,
                serverSide ? clientIpAddress : getSystemNetworkId(),
                true);

        // Set the human requestor active participant (from XUA)
        if (! EventUtils.isEmptyOrNull(userName)) {
            event.addHumanRequestorActiveParticipant(userName, null, userName, null);
        }

        // Set the destination active participant
        event.addDestinationActiveParticipant(
                serverUri,
                serverSide ? getSystemAltUserId() : null,
                null,
                EventUtils.getAddressForUrl(serverUri, false),
                false);
    }
}
