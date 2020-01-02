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

import org.apache.cxf.jaxb.JAXBDataBinding
import org.apache.cxf.jaxb.io.DataReaderImpl
import org.apache.cxf.staxutils.StaxUtils
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hpd.HpdValidator
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * @author Dmytro Rud
 */
class TestIti58 extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'iti-58.xml'

    final String SERVICE1 = "hpd-iti58://localhost:${port}/hpd-service1"


    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
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
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
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
        assert response.getBatchResponses().empty
    }

    BatchResponse sendIt(String endpoint, BatchRequest request) {
        return send(endpoint, request, BatchResponse.class)
    }

    void doTestDsmlValueJaxb(String fileName, Class expectedClass) {
        def inputStream = getClass().classLoader.getResourceAsStream(fileName)
        def reader = StaxUtils.createXMLStreamReader(inputStream)
        def dataReader = new DataReaderImpl(new JAXBDataBinding(HpdValidator.JAXB_CONTEXT), false);

        BatchResponse batchResponse = dataReader.read(reader).value
        assert batchResponse.batchResponses.size() == 1
        SearchResponse searchResponse = batchResponse.batchResponses[0].value
        assert searchResponse.searchResultEntry.size() == 1

        def memberIds = searchResponse.searchResultEntry[0].attr
                .findAll { it.name == 'member' }
                .collect { it.value }
                .flatten()
                .findAll { it.class == expectedClass }
                .collect { it.toString() }

        assert memberIds == [
                'uid=CommunityB:00000000101,ou=HCProfessional,dc=HPD,o=BAG,c=CH',
                'uid=CommunityB:00000000102,ou=HCProfessional,dc=HPD,o=BAG,c=CH',
                'uid=CommunityB:00000004000,ou=HCProfessional,dc=HPD,o=BAG,c=CH',
        ]
    }

    @Test
    void testDsmlValueJaxbWithXsiType() {
        doTestDsmlValueJaxb('iti58-response-with-xsi-type.xml', DsmlValue.class)
    }

    @Test
    void testDsmlValueJaxbWithoutXsiType() {
        doTestDsmlValueJaxb('iti58-response-without-xsi-type.xml', String.class)
    }

}

