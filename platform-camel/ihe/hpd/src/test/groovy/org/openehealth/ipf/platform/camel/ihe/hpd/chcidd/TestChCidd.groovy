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
package org.openehealth.ipf.platform.camel.ihe.hpd.chcidd


import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hpd.stub.chcidd.DownloadRequest
import org.openehealth.ipf.commons.ihe.hpd.stub.chcidd.DownloadResponse
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import javax.xml.datatype.DatatypeFactory

/**
 * @author Dmytro Rud
 */
@Disabled
class TestChCidd extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'ch-cidd.xml'

    final String SERVICE1 = 'ch-cidd://localhost:${port}/ch-cidd-service1'
    final String SERVICE_PTON = 'ch-cidd://10.2.200.16/Cpi/CommunityPortalIndex.svc'

    static final DatatypeFactory DATATYPE_FACTORY = DatatypeFactory.newInstance()


    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    private DownloadResponse sendIt(String endpoint, DownloadRequest request) {
        return send(endpoint, request, DownloadResponse.class)
    }

    @Test
    void testChPidd() {
        DownloadRequest request = new DownloadRequest(
                requestID: '123',
                fromDate:  DATATYPE_FACTORY.newXMLGregorianCalendar(new GregorianCalendar(2017, 1 - 1, 1)),
                toDate:    DATATYPE_FACTORY.newXMLGregorianCalendar(new GregorianCalendar(2017, 12, 31, 5, 6, 7)),
        )

        DownloadResponse response = sendIt(SERVICE1, request)
        assert response != null
        assert response.requestID == '456'
    }

    @Test
    @Disabled
    void test_CPI_Community_Information_Delta_Download() {
        DownloadRequest request = new DownloadRequest(
                requestID: '125',
                fromDate:  DATATYPE_FACTORY.newXMLGregorianCalendar(new GregorianCalendar(2017, 1 - 1, 1)),
                toDate:    DATATYPE_FACTORY.newXMLGregorianCalendar(new GregorianCalendar(2018, 9 - 1, 18)),
        )

        DownloadResponse response = sendIt(SERVICE_PTON, request)
        assert response != null
    }

}

