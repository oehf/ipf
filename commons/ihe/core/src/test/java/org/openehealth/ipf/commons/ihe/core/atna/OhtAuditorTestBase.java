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
package org.openehealth.ipf.commons.ihe.core.atna;

import org.junit.Before;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmytro Rud
 * @deprecated
 */
class OhtAuditorTestBase {

    protected static final String REPLY_TO_URI = "http://141.44.162.126:8090/services/iti55-response";
    protected static final String USER_ID = "user-id";
    protected static final String USER_NAME = "alias<user@issuer>";
    protected static final String QUERY_PAYLOAD = "<query><coffee /></query>";
    protected static final String SERVER_URI = "http://www.icw.int/pxs/iti55-service";
    protected static final String QUERY_ID = "queryIdExtension@queryIdRoot";
    protected static final String MESSAGE_ID = "messageIdExtension@messageIdRoot";
    protected static final String HOME_COMMUNITY_ID = "urn:oid:3.14.15.926";
    protected static final String CLIENT_IP_ADDRESS = "141.44.162.126";


    protected static final String SENDING_FACILITY = "SF";
    protected static final String SENDING_APPLICATION = "SA";
    protected static final String RECEIVING_FACILITY = "RF";
    protected static final String RECEIVING_APPLICATION = "RA";

    protected static final String[] PATIENT_IDS = new String[]{"1234^^^&1.2.3.4.5.6&ISO", "durak^^^&6.7.8.9.10&KRYSO"};

    protected static final List<CodedValueType> PURPOSES_OF_USE;

    static {
        PURPOSES_OF_USE = new ArrayList<>();
        CodedValueType cvt = new CodedValueType();

        cvt.setCode("12");
        cvt.setCodeSystemName("1.0.14265.1");
        cvt.setOriginalText("Law Enforcement");
        PURPOSES_OF_USE.add(cvt);

        cvt.setCode("13");
        cvt.setCodeSystemName("1.0.14265.1");
        cvt.setOriginalText("Something Else");
        PURPOSES_OF_USE.add(cvt);
    }

    protected static final List<CodedValueType> USER_ROLES;

    static {
        USER_ROLES = new ArrayList<>();
        CodedValueType cvt = new CodedValueType();

        cvt.setCode("ABC");
        cvt.setCodeSystemName("1.2.3.4.5");
        cvt.setOriginalText("Role_ABC");
        PURPOSES_OF_USE.add(cvt);

        cvt.setCode("DEF");
        cvt.setCodeSystemName("1.2.3.4.5.6");
        cvt.setOriginalText("Role_DEF");
        PURPOSES_OF_USE.add(cvt);
    }

    protected MockedSender sender;

    @Before
    public void setUp() {
        sender = new MockedSender();
        AuditorModuleContext.getContext().setSender(sender);
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
    }


}