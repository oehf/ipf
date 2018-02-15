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
package org.openehealth.ipf.commons.ihe.core.atna;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomXdsAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmytro Rud
 */
public class XdsAuditorTest extends OhtAuditorTestBase {

    private static final String SUBMISSION_SET_ID    = "submission-set-id";
    private static final String[] OBJECT_UUIDS       = {"objectUuid1", "objectUuid2", "objectUuid3"};
    private static final String[] DOCUMENT_OIDS      = {"1.1.1", "1.1.2", "1.1.3"};
    private static final String[] REPOSITORY_OIDS    = {"2.1.1", "2.1.2", "2.1.3"};
    private static final String[] HOME_COMMUNITY_IDS = {"3.1.1", "3.1.2", "3.1.3"};

    @Test
    public void testAuditors() {
        final CustomXdsAuditor auditor = AuditorManager.getCustomXdsAuditor();

        auditor.auditIti51(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_ID,
                QUERY_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_IDS[0],
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti51(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_ID,
                QUERY_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_IDS[0],
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti61(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                SUBMISSION_SET_ID,
                PATIENT_IDS[0],
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti61(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                SUBMISSION_SET_ID,
                PATIENT_IDS[0],
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti62(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_IDS[0],
                OBJECT_UUIDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti62(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS[0],
                OBJECT_UUIDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti63(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_ID,
                QUERY_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_IDS[0],
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti63(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_ID,
                QUERY_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_IDS[0],
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti86(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_IDS[0],
                DOCUMENT_OIDS,
                REPOSITORY_OIDS,
                HOME_COMMUNITY_IDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti86(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_IDS[0],
                DOCUMENT_OIDS,
                REPOSITORY_OIDS,
                HOME_COMMUNITY_IDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        assertEquals(10, sender.getMessages().size());
    }

}