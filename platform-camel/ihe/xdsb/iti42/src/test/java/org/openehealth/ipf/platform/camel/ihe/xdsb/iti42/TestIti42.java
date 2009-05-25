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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti42;

import static junit.framework.Assert.assertEquals;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.stub.ebrs.rs.RegistryResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the ITI-42 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/iti-42.xml")
public class TestIti42 {
    @Autowired
    private ProducerTemplate<Exchange> producerTemplate;

    private static final String SERVICE1 = "xdsb-iti42://localhost:9091/xdsb-iti42-service1";
    private static final String SERVICE2 = "xdsb-iti42://localhost:9091/xdsb-iti42-service2";

    /** Calls the route attached to the ITI-42 endpoint. */
    @Test
    public void testIti42() {
        SubmitObjectsRequest request = new SubmitObjectsRequest();
        request.setComment("ok");

        RegistryResponseType response1 =
                (RegistryResponseType) producerTemplate.requestBody(SERVICE1, request);
        assertEquals("service 1: ok", response1.getStatus());

        RegistryResponseType response2 =
                (RegistryResponseType) producerTemplate.requestBody(SERVICE2, request);
        assertEquals("service 2: ok", response2.getStatus());
    }
}