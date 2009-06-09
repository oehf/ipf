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

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.OrganizationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the ITI-14 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/iti-14.xml")
public class TestIti14 {
    @Autowired
    private ProducerTemplate<Exchange> producerTemplate;

    private static final String SERVICE1 = "xds-iti14://localhost:9091/xds-iti14-service1";
    private static final String SERVICE2 = "xds-iti14://localhost:9091/xds-iti14-service2";

    /** Calls the route attached to the ITI-14 endpoint. */
    @Test
    public void testIti14() {
        SubmitObjectsRequest request = new SubmitObjectsRequest();
        LeafRegistryObjectListType leafRegistryObjectListType = new LeafRegistryObjectListType();
        List<Object> objectRefOrAssociationOrAuditableEvent = leafRegistryObjectListType.getObjectRefOrAssociationOrAuditableEvent();
        OrganizationType orgType = new OrganizationType();
        orgType.setObjectType("ok");
        objectRefOrAssociationOrAuditableEvent.add(orgType);
        request.setLeafRegistryObjectList(leafRegistryObjectListType);

        RegistryResponse response1 =
                (RegistryResponse) producerTemplate.requestBody(SERVICE1, request);
        assertEquals("service 1: ok", response1.getStatus());

        RegistryResponse response2 =
                (RegistryResponse) producerTemplate.requestBody(SERVICE2, request);
        assertEquals("service 2: ok", response2.getStatus());
    }
}