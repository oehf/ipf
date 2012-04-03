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

import junit.framework.TestCase;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomXdsAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

/**
 * @author Dmytro Rud
 */
public class XdsAuditorTest extends TestCase {

    private static final String REPLY_TO_URI        = "reply-to-uri";
    private static final String USER_NAME           = "user-name";
    private static final String SERVER_URI          = "server-uri";
    private static final String CLIENT_IP_ADDRESS   = "141.44.162.126";
    private static final String PATIENT_ID          = "patientId^^^&1.2.3&ISO";
    private static final String SUBMISSION_SET_ID   = "submission-set-id";
    private static final String QUERY_UUID          = "query-uuid";
    private static final String REQUEST_PAYLOAD     = "request-payload";
    private static final String HOME_COMMUNITY_ID   = "home-community-id";


    protected void setUp() throws Exception {
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
    }


    public void testAuditors() {
        final CustomXdsAuditor auditor = AuditorManager.getCustomXdsAuditor();

        auditor.auditIti61(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                SUBMISSION_SET_ID,
                PATIENT_ID);

        auditor.auditIti61(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                SUBMISSION_SET_ID,
                PATIENT_ID);

        auditor.auditIti63(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_UUID,
                REQUEST_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_ID);

        auditor.auditIti63(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_UUID,
                REQUEST_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_ID);
    }

}