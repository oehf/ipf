/*
 * Copyright 2010 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.core.atna.custom.Hl7v3Auditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmytro Rud
 */
public class Hl7v3AuditorTest extends AuditorTestBase {

    @Test
    public void testAuditors() {
        final Hl7v3Auditor auditor = AuditorManager.getHl7v3Auditor();

        auditor.auditIti44Add(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_IDS,
                MESSAGE_ID,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti44Add(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS,
                MESSAGE_ID,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti44Revise(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_IDS,
                MESSAGE_ID,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti44Revise(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS,
                MESSAGE_ID,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti44Delete(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_IDS[0],
                MESSAGE_ID,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti44Delete(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS[0],
                MESSAGE_ID,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti45(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_PAYLOAD,
                PATIENT_IDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti45(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_PAYLOAD,
                PATIENT_IDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti46(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_IDS,
                MESSAGE_ID,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti46(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS,
                MESSAGE_ID,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti47(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_PAYLOAD,
                PATIENT_IDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti47(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_PAYLOAD,
                PATIENT_IDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti55(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_PAYLOAD,
                QUERY_ID,
                HOME_COMMUNITY_ID,
                PATIENT_IDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti55(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_PAYLOAD,
                QUERY_ID,
                HOME_COMMUNITY_ID,
                PATIENT_IDS,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti56(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_PAYLOAD,
                PATIENT_IDS[0],
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti56(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_PAYLOAD,
                PATIENT_IDS[0],
                PURPOSES_OF_USE,
                USER_ROLES);

        assertEquals(16, sender.getMessages().size());
    }

}