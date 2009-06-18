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
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import org.apache.cxf.transport.servlet.CXFServlet;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestWebContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.server.UdpServer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryPackageType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.utils.Ebxml21TestUtils;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

/**
 * Tests the ITI-14 transaction with a webservice and client adapter defined via URIs.
 * 
 * @author Jens Riemschneider
 */
@SuppressWarnings("unused")
public class TestIti14 extends StandardTestWebContainer {
    private static final String SERVICE1 = "xds-iti14://localhost:9091/xds-iti14-service1";
    private static final String SERVICE2 = "xds-iti14://localhost:9091/xds-iti14-service2";

    private static final String SERVICE_DD = "xds-iti14://localhost:9091/xds-iti14-service11";
    private static final String SERVICE_FT = "xds-iti14://localhost:9091/xds-iti14-service12?audit=false&allowIncompleteAudit=true";
    private static final String SERVICE_DT = "xds-iti14://localhost:9091/xds-iti14-service13?allowIncompleteAudit=true";

    private static final int SYSLOG_PORT = 8888;
    private static UdpServer syslog;

    @BeforeClass
    public static void setUp() throws Exception {
        startServer(new CXFServlet(), "iti-14.xml");
        // installTestInterceptors(Iti14TestAuditFinalInterceptor.class);

        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(SYSLOG_PORT);

        syslog = new UdpServer(SYSLOG_PORT);
        syslog.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        syslog.cancel();
        syslog.join();
    }

    @After
    public void tearDown() {
        syslog.reset();
    }

    
    /** Calls the route attached to the ITI-14 endpoint. */
    @Test
    public void testIti14() {
        SubmitObjectsRequest request = Ebxml21TestUtils.createTestSubmitObjectRequest();
        RegistryResponse response1 = 
            (RegistryResponse)getProducerTemplate().requestBody(SERVICE1, request);
        assertEquals("service 1: ok", response1.getStatus());
        RegistryResponse response2 = 
            (RegistryResponse)getProducerTemplate().requestBody(SERVICE2, request);
        assertEquals("service 2: ok", response2.getStatus());
    }

    /**
     * normal request, auditing enabled --> should audit
     */
    @Test
    public void testIti14_NormalAudit() throws Exception {
        syslog.expectedPacketCount(2);
        SubmitObjectsRequest request = Ebxml21TestUtils.createTestSubmitObjectRequest();
        RegistryResponse response = 
            (RegistryResponse)getProducerTemplate().requestBody(SERVICE_DD, request);
        syslog.assertIsSatisfied();
        String packet1 = syslog.getPacket(0);
        String packet2 = syslog.getPacket(1);
        // TODO: assert proper content of packet1
        // TODO: assert proper content of packet2
    }

    /**
     * arbitrary request, auditing disabled, incomplete audits allowed 
     * (actually a meaningless parameter) --> should NOT audit
     */
    @Test
    public void testIti14_AuditDisabled() throws Exception {
        syslog.expectedPacketCount(0);
        SubmitObjectsRequest request = Ebxml21TestUtils.createTestSubmitObjectRequest();
        RegistryResponse response = 
            (RegistryResponse) getProducerTemplate().requestBody(SERVICE_FT, request);
        syslog.assertIsSatisfied();
    }

    /**
     * incomplete request, auditing enabled, incomplete audits 
     * not allowed --> should NOT audit
     */
    @Test
    public void testIti14_Incomplete_IncompleteAuditingNotAllowed() throws Exception  {
        syslog.expectedPacketCount(0);
        SubmitObjectsRequest request = createIncompleteRequest();
        RegistryResponse response = (RegistryResponse) getProducerTemplate()
                .requestBody(SERVICE_DD, request);
        syslog.assertIsSatisfied();
    }

    /**
     * incomplete request, auditing enabled, incomplete audits 
     * allowed --> should audit
     */
    @Test
    public void testIti14_Incomplete_IncompleteAuditingAllowed() throws Exception {
        syslog.expectedPacketCount(2);
        SubmitObjectsRequest request = createIncompleteRequest();
        RegistryResponse response = (RegistryResponse) getProducerTemplate()
                .requestBody(SERVICE_DT, request);
        syslog.assertIsSatisfied();
        String packet1 = syslog.getPacket(0);
        String packet2 = syslog.getPacket(1);
        // TODO: assert proper content of packet1
        // TODO: assert proper content of packet2
    }

    
    private SubmitObjectsRequest createIncompleteRequest() {
        SubmitObjectsRequest submitObjectsRequest = Ebxml21TestUtils.createTestSubmitObjectRequest();
        RegistryPackageType registryPackageType = (RegistryPackageType)
            submitObjectsRequest.getLeafRegistryObjectList().getObjectRefOrAssociationOrAuditableEvent().get(0);
        registryPackageType.getExternalIdentifier().clear();
        return submitObjectsRequest;
    }
}