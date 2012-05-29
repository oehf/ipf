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
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomPixAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

/**
 * @author Dmytro Rud
 */
public class PixAuditorTest extends TestCase {

    private static final String CLIENT_IP               = "141.44.162.126";
    private static final String SENDING_FACILITY        = "sendingFacility";
    private static final String SENDING_APPLICATION     = "sendingApplication";
    private static final String RECEIVING_FACILITY      = "receivingFacility";
    private static final String RECEIVING_APPLICATION   = "receivingApplication";
    private static final String SERVER_URI              = "mllp://document.registry.org";
    private static final String MESSAGE_ID              = "messageId";
    private static final String SOURCE_PATIENT_ID       = "source^^^&1.2.3&ISO";
    private static final String NEW_PATIENT_ID          = "new^^^&1.3.14&ISO";
    private static final String OLD_PATIENT_ID          = "old^^^&1.8.7&ISO";



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
    }

    
    public void testAuditors() {
        final CustomPixAuditor auditor = AuditorManager.getCustomPixAuditor();

        auditor.auditIti64(
                true,
                RFC3881EventOutcomeCodes.SUCCESS,
                CLIENT_IP,
                SENDING_FACILITY,
                SENDING_APPLICATION,
                SERVER_URI,
                RECEIVING_FACILITY,
                RECEIVING_APPLICATION,
                MESSAGE_ID,
                SOURCE_PATIENT_ID,
                NEW_PATIENT_ID,
                OLD_PATIENT_ID);

        auditor.auditIti64(
                false,
                RFC3881EventOutcomeCodes.SUCCESS,
                CLIENT_IP,
                SENDING_FACILITY,
                SENDING_APPLICATION,
                SERVER_URI,
                RECEIVING_FACILITY,
                RECEIVING_APPLICATION,
                MESSAGE_ID,
                SOURCE_PATIENT_ID,
                NEW_PATIENT_ID,
                OLD_PATIENT_ID);

    }

}