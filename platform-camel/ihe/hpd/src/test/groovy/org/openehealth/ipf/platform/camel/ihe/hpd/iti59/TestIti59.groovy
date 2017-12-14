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
package org.openehealth.ipf.platform.camel.ihe.hpd.iti59

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.core.atna.custom.HpdAuditor
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig

/**
 * @author Dmytro Rud
 */
class TestIti59 extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'iti-59.xml'

    final String SERVICE1 = "hpd-iti59://localhost:${port}/hpd-service1"

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT);
    }

    @BeforeClass
    static void classSetUp() {
        HpdAuditor.auditor.config = new AuditorModuleConfig()
        HpdAuditor.auditor.config.setAuditRepositoryHost('localhost')
        HpdAuditor.auditor.config.setAuditRepositoryPort(514)
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void testIti59() {
        BatchRequest request = new BatchRequest(
                batchRequests: [
                        new AddRequest(requestID: 'id-1', attr: [
                                new DsmlAttr(name: 'hcIdentifier', value: ['hcid-1', 'hcid-2']),
                                new DsmlAttr(name: 'foo', value: ['bar', 'spar', 'antiquar']),
                                new DsmlAttr(name: 'hcIdentifier', value: ['hcid-3', 'hcid-4', 'hcid-5']),
                        ]),
                        new DelRequest(requestID: 'id-2', dn: 'dn-2'),
                        new ModifyDNRequest(requestID: 'id-3', dn: 'dn-3', newrdn: 'newrdn-3'),
                ],
        )
        BatchResponse response = sendIt(SERVICE1, request)
        assert response != null
        assert auditSender.messages.size() == 6
    }

    BatchResponse sendIt(String endpoint, BatchRequest request) {
        return send(endpoint, request, BatchResponse.class)
    }


}
