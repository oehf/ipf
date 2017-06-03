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
package org.openehealth.ipf.platform.camel.ihe.hpd.chpidd

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadRequest
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadResponse
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * @author Dmytro Rud
 */
class TestChPidd extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'ch-pidd.xml'

    final String SERVICE1 = "ch-pidd://localhost:${port}/ch-pidd-service1"

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }

    @BeforeClass
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void testChPidd() {
        DownloadRequest request = new DownloadRequest(
                requestID: '123',
                fromDate: new XMLGregorianCalendarImpl(new GregorianCalendar(2017, 1, 1, 2, 3, 4)),
                toDate: new XMLGregorianCalendarImpl(new GregorianCalendar(2017, 12, 31, 5, 6, 7)),
        )

        DownloadResponse response = sendIt(SERVICE1, request)
        assert response != null
        assert response.requestID == '456'
    }

    DownloadResponse sendIt(String endpoint, DownloadRequest request) {
        return send(endpoint, request, DownloadResponse.class)
    }

}

