/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hpd.chciq

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * @author Dmytro Rud
 */
class TestChCiq extends StandardTestContainer {
    static final String CONTEXT_DESCRIPTOR = 'ch-ciq.xml'

    final String SERVICE_PTON = 'ch-ciq://10.2.200.16/Cpi/CommunityPortalIndex.svc'

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    BatchResponse sendIt(String endpoint, BatchRequest request) {
        return send(endpoint, request, BatchResponse.class)
    }

    @Test
    @Disabled
    void test_CPI_Community_Information_Query() {
        BatchRequest request = new BatchRequest(
                requestID: '123',
                batchRequests: [
                        new SearchRequest(
                                dn: 'OU=CHCommunity,DC=CPI,O=BAG,C=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        present: new AttributeDescription(
                                                name: 'objectClass',
                                        ),
                                ),
                        ),
                ],
        )

        BatchResponse response = sendIt(SERVICE_PTON, request)
        assert response != null
    }

    @Test
    @Disabled
    void test_Querying_Community_Portal_Index() {
        BatchRequest request = new BatchRequest(
                requestID: '124',
                batchRequests: [
                        new SearchRequest(
                                dn: 'OU=CHCommunity,DC=CPI,O=BAG,C=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        present: new AttributeDescription(name: 'uid'),
                                ),
                                attributes: new AttributeDescriptions(
                                        attribute: [
                                                new AttributeDescription(name: 'shcFullName'),
                                                new AttributeDescription(name: 'shcStatus'),
                                        ],
                                ),
                        ),
                ],
        )

        BatchResponse response = sendIt(SERVICE_PTON, request)
        assert response != null
        assert response.batchResponses.size() == 1
        assert response.batchResponses[0].value instanceof SearchResponse

        SearchResponse searchResponse = response.batchResponses[0].value
        for (entry in searchResponse.searchResultEntry) {
            String name   = entry.attr.find { it.name == 'shcFullName' }?.value?.get(0) ?: 'unknown'
            String status = entry.attr.find { it.name == 'shcStatus' }?.value?.get(0)   ?: 'unknown'
            println "Community ${name} has status ${status} and can be ${status == 'Active' ? '' : 'NOT '}accessed"
        }
    }

}

