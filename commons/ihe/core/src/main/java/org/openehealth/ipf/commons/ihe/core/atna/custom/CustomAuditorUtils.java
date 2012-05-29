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

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.events.ihe.GenericIHEAuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * @author Dmytro Rud
 */
abstract class CustomAuditorUtils {

    public static void configureEvent(
            IHEAuditor auditor,
            boolean serverSide,
            GenericIHEAuditEventMessage event,
            String sourceUserId,
            String userName,
            String destinationUserId,
            String destinationUri,
            String sourceIpAddress)
    {
        event.setAuditSourceId(
                auditor.getAuditSourceId(),
                auditor.getAuditEnterpriseSiteId());

        // Set the source active participant
        event.addSourceActiveParticipant(
                sourceUserId,
                serverSide ? null : auditor.getSystemAltUserId(),
                null,
                serverSide ? sourceIpAddress : auditor.getSystemNetworkId(),
                true);

        // Set the human requestor active participant (from XUA)
        if (! EventUtils.isEmptyOrNull(userName)) {
            event.addHumanRequestorActiveParticipant(userName, null, userName, null);
        }

        // Set the destination active participant
        event.addDestinationActiveParticipant(
                destinationUserId,
                serverSide ? auditor.getSystemAltUserId() : null,
                null,
                EventUtils.getAddressForUrl(destinationUri, false),
                false);
    }
}
