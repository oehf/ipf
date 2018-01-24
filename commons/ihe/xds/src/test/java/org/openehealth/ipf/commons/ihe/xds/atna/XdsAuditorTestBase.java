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

package org.openehealth.ipf.commons.ihe.xds.atna;

import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.EventId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorTestBase;

/**
 * @author Christian Ohr
 */
public class XdsAuditorTestBase extends AuditorTestBase {

    protected static final String[] DOCUMENT_OIDS = {"1.1.1", "1.1.2", "1.1.3"};
    protected static final String[] REPOSITORY_OIDS = {"2.1.1", "2.1.2", "2.1.3"};
    protected static final String[] HOME_COMMUNITY_IDS = {"3.1.1", "3.1.2", "3.1.3"};
    protected static final String[] OBJECT_UUIDS = {"objectUuid1", "objectUuid2", "objectUuid3"};
    protected static final String[] STUDY_INSTANCE_UUIDS = {"study-instance_uuid-1", "study-instance_uuid-1", "study-instance_uuid-2"};
    protected static final String[] SERIES_INSTANCE_UUIDS = {"series-instance_uuid-11", "series-instance_uuid-12", "series-instance_uuid-21"};

    protected void assertCommonXdsAuditAttributes(AuditMessage auditMessage,
                                                  EventOutcomeIndicator eventOutcomeIndicator,
                                                  EventId eventId,
                                                  EventActionCode eventActionCode,
                                                  boolean serverSide,
                                                  boolean requiresPatient) {
        assertCommonAuditAttributes(auditMessage, eventOutcomeIndicator, eventId, eventActionCode,
                REPLY_TO_URI, SERVER_URI, serverSide, requiresPatient);
    }
}
