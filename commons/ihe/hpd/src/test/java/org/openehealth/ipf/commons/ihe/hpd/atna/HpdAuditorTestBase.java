/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.hpd.atna;

import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.EventId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorTestBase;

/**
 * @author Christian Ohr
 */
public class HpdAuditorTestBase extends AuditorTestBase {

    protected static final String[] PROVIDER_IDS = new String[]{
            "2.16.10.89.200:UPIN:800-800-8000:Active",
            "2.16.10.98.123:NPI:666789-800:Active",
            "1.89.11.00.123:HospId:786868:Active"};

    protected void assertCommonHpdAuditAttributes(AuditMessage auditMessage,
                                                  EventOutcomeIndicator eventOutcomeIndicator,
                                                  EventId eventId,
                                                  EventActionCode eventActionCode,
                                                  boolean serverSide) {
        assertCommonAuditAttributes(auditMessage, eventOutcomeIndicator, eventId, eventActionCode,
                REPLY_TO_URI, SERVER_URI, serverSide, false);
    }

}
