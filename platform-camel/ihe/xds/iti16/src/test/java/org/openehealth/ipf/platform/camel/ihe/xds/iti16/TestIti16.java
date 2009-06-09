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
package org.openehealth.ipf.platform.camel.ihe.xds.iti16;

import static junit.framework.Assert.assertEquals;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the ITI-16 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/iti-16.xml")
public class TestIti16 {
    @Autowired
    private ProducerTemplate<Exchange> producerTemplate;

    private static final String SERVICE1 = "xds-iti16://localhost:9091/xds-iti16-service1";
    private static final String SERVICE2 = "xds-iti16://localhost:9091/xds-iti16-service2";

    /** Calls the route attached to the ITI-16 endpoint. */
    @Test
    public void testIti16() {
        AdhocQueryRequest request = new AdhocQueryRequest();
        request.setSQLQuery("ok");

        AdhocQueryResponse response1 =
                (AdhocQueryResponse)producerTemplate.requestBody(SERVICE1, request);

        RegistryObjectType actual1 = 
            (RegistryObjectType) response1.getSQLQueryResult().getObjectRefOrAssociationOrAuditableEvent().get(0);
        assertEquals("service 1: ok", actual1.getObjectType());

        AdhocQueryResponse response2 =
                (AdhocQueryResponse)producerTemplate.requestBody(SERVICE2, request);

        RegistryObjectType actual2 = 
            (RegistryObjectType) response2.getSQLQueryResult().getObjectRefOrAssociationOrAuditableEvent().get(0);
        assertEquals("service 2: ok", actual2.getObjectType());
    }
}