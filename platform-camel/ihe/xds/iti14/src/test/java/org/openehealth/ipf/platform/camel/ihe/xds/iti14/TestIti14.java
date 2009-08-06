/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14;

import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.apache.cxf.transport.servlet.CXFServlet;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.SampleData;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RegisterDocumentSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;

/**
 * Tests the ITI-14 transaction with a webservice and client adapter defined via URIs.
 * 
 * @author Jens Riemschneider
 */
public class TestIti14 extends StandardTestContainer {
    private final String SERVICE1 = "xds-iti14://localhost:" + getPort() + "/xds-iti14-service1";
    private final String SERVICE2 = "xds-iti14://localhost:" + getPort() + "/xds-iti14-service2";

    private final String SERVICE_DD = "xds-iti14://localhost:" + getPort() + "/xds-iti14-service11";
    private final String SERVICE_FT = "xds-iti14://localhost:" + getPort() + "/xds-iti14-service12?audit=false&allowIncompleteAudit=true";
    private final String SERVICE_DT = "xds-iti14://localhost:" + getPort() + "/xds-iti14-service13?allowIncompleteAudit=true";

    private RegisterDocumentSet request;
    private DocumentEntry docEntry;

    @BeforeClass
    public static void setUpClass() throws Exception {
        startServer(new CXFServlet(), "iti-14.xml");
    }

    @Before
    public void setUp() {
        request = SampleData.createRegisterDocumentSet();
        docEntry = request.getDocumentEntries().get(0);
    }
    
    @After
    public void tearDown() {
        getSyslog().reset();
    }
    
    /** Calls the route attached to the ITI-14 endpoint. */
    @Test
    public void testIti14() {
        Response response1 = send(SERVICE1, "service 1");
        assertEquals(Status.SUCCESS, response1.getStatus());

        Response response2 = send(SERVICE2, "service 2");
        assertEquals(Status.SUCCESS, response2.getStatus());
    }

    /** normal request, auditing enabled --> should audit */
    @Test
    public void testIti14_NormalAudit() throws Exception {
        getSyslog().expectedPacketCount(2);
        send(SERVICE_DD, "service 11");
        getSyslog().assertIsSatisfied();
        getSyslog().getPacket(0);
        getSyslog().getPacket(1);
        // TODO: assert proper content of packet1
        // TODO: assert proper content of packet2
    }

    /**
     * arbitrary request, auditing disabled, incomplete audits allowed 
     * (actually a meaningless parameter) --> should NOT audit
     */
    @Test
    public void testIti14_AuditDisabled() throws Exception {
        getSyslog().expectedPacketCount(0);
        send(SERVICE_FT, "service 12");
        getSyslog().assertIsSatisfied();
    }

    /**
     * incomplete request, auditing enabled, incomplete audits 
     * not allowed --> should NOT audit
     */
    @Test
    public void testIti14_Incomplete_IncompleteAuditingNotAllowed() throws Exception  {
        getSyslog().expectedPacketCount(0);
        request.setSubmissionSet(null);
        send(SERVICE_DD, "service 11");
        getSyslog().assertIsSatisfied();
    }

    /** incomplete request, auditing enabled, incomplete audits allowed --> should audit */
    @Test
    public void testIti14_Incomplete_IncompleteAuditingAllowed() throws Exception {
        getSyslog().expectedPacketCount(2);
        request.setSubmissionSet(null);
        send(SERVICE_DT, "service 13");
        getSyslog().assertIsSatisfied();
        getSyslog().getPacket(0);
        getSyslog().getPacket(1);
        // TODO: assert proper content of packet1
        // TODO: assert proper content of packet2
    }
    
    private Response send(String endpoint, String value) {
        docEntry.setComments(new LocalizedString(value));
        return send(endpoint, request, Response.class);
    }
}