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
package org.openehealth.ipf.commons.ihe.atna;

import junit.framework.TestCase;

import org.openehealth.ipf.commons.ihe.atna.custom.XCPDInitiatingGatewayAuditor;
import org.openehealth.ipf.commons.ihe.atna.custom.XCPDRespondingGatewayAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

/**
 * @author Dmytro Rud
 */
public class XCPDAuditTest extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
    }

    
    public void testIti55InitiatingGatewayAudit() {
        new XCPDInitiatingGatewayAuditor().auditXCPDPatientDiscoveryQueryEvent(
                RFC3881EventOutcomeCodes.SUCCESS,
                "http://141.44.162.126:8090/services/iti55-response",
                "alias<user@issuer>",
                "http://www.icw.int/pxs/iti55-service",
                "<query><coffee /></query>",
                "queryId-12345",
                "homeCommunityId-6789",
                new String[] {
                        "1234^^^&1.2.3.4.5.6&ISO",
                        "durak^^^&6.7.8.9.10&KRYSO"
                }
            );
    }

    
    public void testIti55RespondingGatewayAudit() {
        new XCPDRespondingGatewayAuditor().auditXCPDPatientDiscoveryQueryEvent(
                RFC3881EventOutcomeCodes.SUCCESS,
                "http://141.44.162.126:8090/services/iti55-response",
                "http://requestorUri",
                "alias<user@issuer>",
                "http://www.icw.int/pxs/iti55-service",
                "<query><coffee /></query>",
                "queryId-12345",
                "homeCommunityId-6789",
                new String[] {
                        "1234^^^&1.2.3.4.5.6&ISO",
                        "durak^^^&6.7.8.9.10&KRYSO"
                }
            );
    }

    
    public void testIti56InitiatingGatewayAudit() {
        new XCPDInitiatingGatewayAuditor().auditXCPDPatientLocationQueryEvent(
                RFC3881EventOutcomeCodes.SUCCESS,
                "http://141.44.162.126:8090/services/iti56-response",
                "http://www.icw.int/pxs/iti56-service",
                "alias<user@issuer>",
                "<query><kakao /></query>",
                "31415926^^^&1.2.3.4.5.6&ISO"
            );
    }

    
    public void testIti56RespondingGatewayAudit() {
        new XCPDRespondingGatewayAuditor().auditXCPDPatientLocationQueryEvent(
                RFC3881EventOutcomeCodes.SUCCESS,
                "http://141.44.162.126:8090/services/iti56-response",
                "http://requestorUri",
                "alias<user@issuer>",
                "http://www.icw.int/pxs/iti56-service",
                "<query><kakao /></query>", 
                "31415927^^^&1.2.3.4.5.6&ISO"
            );
    }
}