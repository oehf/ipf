/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.ech.ech0214

import org.apache.camel.support.DefaultExchange
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.ech.EchUtils
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.Request as Request0214
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.Response as Response0214
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

class TestEch0214 extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'ech-0214.xml'

    final String SERVICE1 = "ech-0214://localhost:${port}/service-1"
    final String SERVICE2 = "ech-0214://localhost:${port}/service-2"

    static final EchUtils ECH_UTILS = new EchUtils(
        'sedex://T3-CH-24',
        'Open E-Health Foundation',
        'IPF',
        '5.0',
        true,
        'FR'
    )

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void test1() {
        Request0214 request = new Request0214(
            header: ECH_UTILS.createHeader('1020', '5'),
            content: new Request0214.Content(
                SPIDCategory: EchUtils.EPR_SPID_ID_CATEGORY,
                responseLanguage: ECH_UTILS.responseLanguage,
                getInfoPersonRequest: [
                    new Request0214.Content.GetInfoPersonRequest(
                        getInfoPersonRequestId: 1,
                        detailLevelOfResponse: 'standard',
                        pid: new Request0214.Content.GetInfoPersonRequest.Pid(
                            vn: 7560000000002L,
                        ),
                    ),
                ],
            ),
        )

        def exchange = new DefaultExchange(camelContext)
        exchange.in.body = request
        exchange = producerTemplate.send(SERVICE1, exchange)
        def exception = Exchanges.extractException(exchange)
        if (exception) {
            throw exception
        }

        Response0214 response = exchange.message.getMandatoryBody(Response0214.class)
        assert response.positiveResponse.getInfoPersonResponse[0].pids.SPID == ['761337612345678908']
    }

}
