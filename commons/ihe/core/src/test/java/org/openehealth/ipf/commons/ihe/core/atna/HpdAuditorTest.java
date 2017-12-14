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
package org.openehealth.ipf.commons.ihe.core.atna;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.atna.custom.HpdAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmytro Rud
 */
public class HpdAuditorTest extends AuditorTestBase {

    private static final List<String> PROVIDER_IDS = Arrays.asList(
            "2.16.10.89.200:UPIN:800-800-8000:Active",
            "2.16.10.98.123:NPI:666789-800:Active",
            "1.89.11.00.123:HospId:786868:Active");

    @Test
    public void testAuditors() {
        final HpdAuditor auditor = AuditorManager.getHpdAuditor();

        // operation ADD
        auditor.auditIti59(true,
                RFC3881EventCodes.RFC3881EventActionCodes.CREATE,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PROVIDER_IDS,
                null, null,
                PURPOSES_OF_USE,
                USER_ROLES);

        auditor.auditIti59(false,
                RFC3881EventCodes.RFC3881EventActionCodes.CREATE,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PROVIDER_IDS,
                null, null,
                PURPOSES_OF_USE,
                USER_ROLES);

        // operation DELETE
        auditor.auditIti59(true,
                RFC3881EventCodes.RFC3881EventActionCodes.DELETE,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                null,
                "uid=john.doe,ou=People,dc=example,dc=com",
                null,
                PURPOSES_OF_USE,
                USER_ROLES);

        // operation MODIFY DN
        auditor.auditIti59(true,
                RFC3881EventCodes.RFC3881EventActionCodes.DELETE,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                null,
                "uid=john.doe,ou=People,dc=example,dc=com",
                "cn=John Doe+telephoneNumber=+1 123-456-7890",
                PURPOSES_OF_USE,
                USER_ROLES);

        assertEquals(4, sender.getMessages().size());
    }

}