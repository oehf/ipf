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
package org.openehealth.ipf.platform.camel.ihe.xds.iti15;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestWebContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.OrganizationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * Tests the ITI-15 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
public class TestIti15 extends StandardTestWebContainer {
    private static final String SERVICE1 = "xds-iti15://localhost:9091/xds-iti15-service1";
    private static final String SERVICE2 = "xds-iti15://localhost:9091/xds-iti15-service2";
    
    @BeforeClass
    public static void setUp() throws Exception {
        startServer(new CXFServlet(), "iti-15.xml");
    }
    
    /** Calls the route attached to the ITI-15 endpoint. */
    @Test
    public void testIti15() {
        SubmitObjectsRequest request = new SubmitObjectsRequest();
        LeafRegistryObjectListType leafRegistryObjectListType = new LeafRegistryObjectListType();
        List<Object> objectRefOrAssociationOrAuditableEvent = leafRegistryObjectListType.getObjectRefOrAssociationOrAuditableEvent();
        OrganizationType orgType = new OrganizationType();
        orgType.setObjectType("ok");
        objectRefOrAssociationOrAuditableEvent.add(orgType);
        request.setLeafRegistryObjectList(leafRegistryObjectListType);

        RegistryResponse response1 =
                (RegistryResponse)getProducerTemplate().requestBody(SERVICE1, request);

        assertEquals("service 1: ok", response1.getStatus());

        RegistryResponse response2 =
                (RegistryResponse)getProducerTemplate().requestBody(SERVICE2, request);

        assertEquals("service 2: ok", response2.getStatus());
    }
}