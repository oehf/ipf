/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.codes.AuditSourceType;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.openehealth.ipf.commons.audit.queue.SynchronousAuditMessageQueue;

/**
 * @author Christian Ohr
 */
public class DefaultAuditContext implements AuditContext {

    @Getter @Setter
    private boolean auditEnabled = false;

    @Getter @Setter
    private AuditTransmissionProtocol auditTransmissionProtocol;

    @Getter @Setter
    private AuditMessageQueue auditMessageQueue = new SynchronousAuditMessageQueue();

    @Getter @Setter
    private String sourceId = "IPF";

    @Getter @Setter
    private String enterpriseSiteId = "IPF";

    @Getter @Setter
    private AuditSourceType auditSourceType = AuditSourceType.Other;

}
