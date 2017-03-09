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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomXdsAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmytro Rud
 */
public class XdsAuditorTest extends Assert {

    private static final String REPLY_TO_URI         = "reply-to-uri";
    private static final String USER_NAME            = "alias<user@issuer>";
    private static final String SERVER_URI           = "server-uri";
    private static final String CLIENT_IP_ADDRESS    = "141.44.162.126";
    private static final String PATIENT_ID           = "patientId^^^&1.2.3&ISO";
    private static final String SUBMISSION_SET_ID    = "submission-set-id";
    private static final String QUERY_UUID           = "query-uuid";
    private static final String REQUEST_PAYLOAD      = "request-payload";
    private static final String HOME_COMMUNITY_ID    = "home-community-id";
    private static final String[] OBJECT_UUIDS       = {"objectUuid1", "objectUuid2", "objectUuid3"};
    private static final String[] DOCUMENT_OIDS      = {"1.1.1", "1.1.2", "1.1.3"};
    private static final String[] REPOSITORY_OIDS    = {"2.1.1", "2.1.2", "2.1.3"};
    private static final String[] HOME_COMMUNITY_IDS = {"3.1.1", "3.1.2", "3.1.3"};

    private static final List<CodedValueType> PURPOSES_OF_USE;
    static {
        PURPOSES_OF_USE = new ArrayList<>();

        CodedValueType cvt1 = new CodedValueType();
        cvt1.setCode("12");
        cvt1.setCodeSystemName("1.0.14265.1");
        cvt1.setOriginalText("Law Enforcement");
        PURPOSES_OF_USE.add(cvt1);

        CodedValueType cvt2 = new CodedValueType();
        cvt2.setCode("13");
        cvt2.setCodeSystemName("1.0.14265.1");
        cvt2.setOriginalText("Something Else");
        PURPOSES_OF_USE.add(cvt2);
    }

    private MockedSender sender;

    @Before
    public void setUp() throws Exception {
        sender = new MockedSender();
        AuditorModuleContext.getContext().setSender(sender);
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
    }

    @Test
    public void testAuditors() {
        final CustomXdsAuditor auditor = AuditorManager.getCustomXdsAuditor();

        auditor.auditIti51(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_UUID,
                REQUEST_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_ID,
                PURPOSES_OF_USE);

        auditor.auditIti51(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_UUID,
                REQUEST_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_ID,
                PURPOSES_OF_USE);

        auditor.auditIti61(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                SUBMISSION_SET_ID,
                PATIENT_ID,
                PURPOSES_OF_USE);

        auditor.auditIti61(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                SUBMISSION_SET_ID,
                PATIENT_ID,
                PURPOSES_OF_USE);

        auditor.auditIti62(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_ID,
                OBJECT_UUIDS,
                PURPOSES_OF_USE);

        auditor.auditIti62(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                PATIENT_ID,
                OBJECT_UUIDS,
                PURPOSES_OF_USE);

        auditor.auditIti63(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                QUERY_UUID,
                REQUEST_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_ID,
                PURPOSES_OF_USE);

        auditor.auditIti63(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, null,
                QUERY_UUID,
                REQUEST_PAYLOAD,
                HOME_COMMUNITY_ID,
                PATIENT_ID,
                PURPOSES_OF_USE);

        auditor.auditItiY1(true,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_ID,
                DOCUMENT_OIDS,
                REPOSITORY_OIDS,
                HOME_COMMUNITY_IDS,
                PURPOSES_OF_USE);

        auditor.auditItiY1(false,
                RFC3881EventOutcomeCodes.SUCCESS, REPLY_TO_URI, USER_NAME, SERVER_URI, CLIENT_IP_ADDRESS,
                PATIENT_ID,
                DOCUMENT_OIDS,
                REPOSITORY_OIDS,
                HOME_COMMUNITY_IDS,
                PURPOSES_OF_USE);

        assertEquals(10, sender.getMessages().size());
    }

}