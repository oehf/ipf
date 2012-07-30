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

import junit.framework.TestCase;

import org.openehealth.ipf.commons.ihe.core.atna.custom.Hl7v3Auditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

/**
 * @author Dmytro Rud
 */
public class Hl7v3AuditorTest extends TestCase {

    private static final String REPLY_TO_URI    = "http://141.44.162.126:8090/services/iti55-response";
    private static final String USER_NAME       = "alias<user@issuer>";
    private static final String QUERY_PAYLOAD   = "<query><coffee /></query>";
    private static final String SERVER_URI      = "http://www.icw.int/pxs/iti55-service";
    private static final String QUERY_ID        = "queryIdExtension@queryIdRoot";
    private static final String MESSAGE_ID      = "messageIdExtension@messageIdRoot";
    private static final String HOME_COMM_ID    = "urn:oid:3.14.15.926";
    private static final String CLIENT_IP       = "141.44.162.126";

    private static final String[] PATIENT_IDS = new String[] {
            "1234^^^&1.2.3.4.5.6&ISO",
            "durak^^^&6.7.8.9.10&KRYSO"
    };


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
    }

    
    public void testAuditors() {
        final Hl7v3Auditor auditor = AuditorManager.getHl7v3Auditor();

        auditor.auditIti44Add(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP,
                PATIENT_IDS,
                MESSAGE_ID);

        auditor.auditIti44Add(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS,
                MESSAGE_ID);

        auditor.auditIti44Revise(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP,
                PATIENT_IDS,
                MESSAGE_ID);

        auditor.auditIti44Revise(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS,
                MESSAGE_ID);

        auditor.auditIti44Delete(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP,
                PATIENT_IDS[0],
                MESSAGE_ID);

        auditor.auditIti44Delete(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS[0],
                MESSAGE_ID);

        auditor.auditIti45(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP,
                QUERY_PAYLOAD,
                PATIENT_IDS);

        auditor.auditIti45(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_PAYLOAD,
                PATIENT_IDS);

        auditor.auditIti46(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP,
                PATIENT_IDS,
                MESSAGE_ID);

        auditor.auditIti46(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_IDS,
                MESSAGE_ID);

        auditor.auditIti47(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP,
                QUERY_PAYLOAD,
                PATIENT_IDS);

        auditor.auditIti47(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_PAYLOAD,
                PATIENT_IDS);

        auditor.auditIti55(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP,
                QUERY_PAYLOAD,
                QUERY_ID,
                HOME_COMM_ID,
                PATIENT_IDS);

        auditor.auditIti55(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_PAYLOAD,
                QUERY_ID,
                HOME_COMM_ID,
                PATIENT_IDS);

        auditor.auditIti56(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP,
                QUERY_PAYLOAD,
                PATIENT_IDS[0]);

        auditor.auditIti56(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_PAYLOAD,
                PATIENT_IDS[0]);

    }

}