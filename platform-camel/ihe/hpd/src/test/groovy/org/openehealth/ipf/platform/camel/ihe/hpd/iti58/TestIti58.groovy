/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hpd.iti58

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.AttributeValueAssertion
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.Filter
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchRequest
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * @author Dmytro Rud
 */
class TestIti58 extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'iti-58.xml'

    final String SERVICE1 = "hpd-iti58://localhost:${port}/hpd-service1"


    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }

    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void testIti58() {
        BatchRequest request = new BatchRequest(
                batchRequests: [
                        new SearchRequest(
                                dn: 'O=HPDTEST1,DC=HPD',
                                scope: 'wholeSubtree',
                                derefAliases: 'neverDerefAliases',
                                filter: new Filter(
                                        approxMatch: new AttributeValueAssertion(
                                                name: 'displayName',
                                                value: 'Mark Smith',
                                        ),
                                ),
                        ),
                ],
        )


        BatchResponse response = sendIt(SERVICE1, request)
        assert response != null
    }

    BatchResponse sendIt(String endpoint, BatchRequest request) {
        return send(endpoint, request, BatchResponse.class)
    }


}

