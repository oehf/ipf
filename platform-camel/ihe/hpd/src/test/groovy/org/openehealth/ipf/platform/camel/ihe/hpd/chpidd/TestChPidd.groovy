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

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadRequest
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadResponse
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

import javax.xml.datatype.DatatypeFactory

/**
 * @author Dmytro Rud
 */
class TestChPidd extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'ch-pidd.xml'

    final String SERVICE1    = "ch-pidd://localhost:${port}/ch-pidd-service1"
    final String SERVICE_ZAD = "ch-pidd://ws.epd-ad-a.bag.admin.ch:443/Hpd/ProviderInformationDirectory.svc?secure=true&inInterceptors=#inLogger&outInterceptors=#outLogger"

    static final DatatypeFactory DATATYPE_FACTORY = DatatypeFactory.newInstance()

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeClass
    static void classSetUp() {
        /*
        System.setProperty('javax.net.ssl.keyStore',            );
        System.setProperty('javax.net.ssl.keyStorePassword',    );
        System.setProperty('javax.net.ssl.trustStore',          );
        System.setProperty('javax.net.ssl.trustStorePassword',  );
        System.setProperty(PayloadLoggerBase.PROPERTY_CONSOLE,  'true');
        */

        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void testChPidd() {
        DownloadRequest request = new DownloadRequest(
                requestID: '123',
                fromDate: DATATYPE_FACTORY.newXMLGregorianCalendar(new GregorianCalendar(1980, Calendar.JANUARY, 1, 0, 0, 0)),
                toDate: DATATYPE_FACTORY.newXMLGregorianCalendar(new GregorianCalendar(2020, Calendar.DECEMBER, 31, 23, 59, 59)),
                pageNumber: 2,
                pageSize: 100,
        )

        DownloadResponse response = sendIt(SERVICE1, request)
        assert response != null
        assert response.requestID == '456'
        assert response.totalCount == 777
    }

    DownloadResponse sendIt(String endpoint, DownloadRequest request) {
        send(endpoint, request, DownloadResponse.class)
    }

}

