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
 * @author Clay Sebourn
 */
public class RadAuditorTest extends TestCase {

    private static final String USER_ID                 = "user-id";
    private static final String USER_NAME               = "alias<user@issuer>";
    private static final String CLIENT_IP_ADDRESS       = "141.44.162.126";
    private static final String SERVER_URI              = "server-uri";
    private static final String[] STUDY_INSTANCE_UUIDS  = {"study-instance_uuid-1", "study-instance_uuid-1", "study-instance_uuid-2"};
    private static final String[] SERIES_INSTANCE_UUIDS = {"series-instance_uuid-11", "series-instance_uuid-12", "series-instance_uuid-21"};
    private static final String[] DOCUMENT_UUIDS        = {"document_uuid-1-11-1", "document_uuid1-11-2", "document_uuid2-21-1"};
    private static final String[] REPOSITORY_UUIDS      = {"repository_uuid1", "repository_uuid2", "repository_uuid1"};
    private static final String[] HOME_COMMUNITY_UUIDS  = {"home-community_uuid1", "home-community_uuid1", "home-community_uuid1"};
    private static final String PATIENT_ID              = "patientId^^^&1.2.3&ISO";


    protected void setUp() throws Exception {
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
    }


    public void testAuditors() {
        final CustomXdsAuditor auditor = AuditorManager.getCustomXdsAuditor();

        // Client RAD-69 event
        auditor.auditRad69(
            false,
            RFC3881EventOutcomeCodes.SUCCESS,
            USER_ID,
            USER_NAME,
            SERVER_URI,
            null,
            STUDY_INSTANCE_UUIDS,
            SERIES_INSTANCE_UUIDS,
            DOCUMENT_UUIDS,
            REPOSITORY_UUIDS,
            HOME_COMMUNITY_UUIDS,
            PATIENT_ID);

        // Server RAD-69 event
        auditor.auditRad69(
            true,
            RFC3881EventOutcomeCodes.SUCCESS,
            USER_ID,
            USER_NAME,
            SERVER_URI,
            CLIENT_IP_ADDRESS,
            STUDY_INSTANCE_UUIDS,
            SERIES_INSTANCE_UUIDS,
            DOCUMENT_UUIDS,
            REPOSITORY_UUIDS,
            HOME_COMMUNITY_UUIDS,
            PATIENT_ID);

        // Client RAD-75 event
        auditor.auditRad75(
            false,
            RFC3881EventOutcomeCodes.SUCCESS,
            USER_ID,
            USER_NAME,
            SERVER_URI,
            null,
            STUDY_INSTANCE_UUIDS,
            SERIES_INSTANCE_UUIDS,
            DOCUMENT_UUIDS,
            REPOSITORY_UUIDS,
            HOME_COMMUNITY_UUIDS,
            PATIENT_ID);

        // Server RAD-75
        auditor.auditRad75(
            true,
            RFC3881EventOutcomeCodes.SUCCESS,
            USER_ID,
            USER_NAME,
            SERVER_URI,
            CLIENT_IP_ADDRESS,
            STUDY_INSTANCE_UUIDS,
            SERIES_INSTANCE_UUIDS,
            DOCUMENT_UUIDS,
            REPOSITORY_UUIDS,
            HOME_COMMUNITY_UUIDS,
            PATIENT_ID);
   }
}