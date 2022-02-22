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
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggingContext
import org.openehealth.ipf.commons.ihe.hpd.HpdValidator
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils
import org.openehealth.ipf.commons.ihe.hpd.controls.pagination.Pagination
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * @author Dmytro Rud
 */
class TestIti58 extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'iti-58.xml'

    final String SERVICE1 = "hpd-iti58://localhost:${port}/hpd-service1?inInterceptors=#clientInLogger&outInterceptors=#clientOutLogger"
    final String SERVICE2 = "hpd-iti58://localhost:${port}/hpd-service2?inInterceptors=#clientInLogger&outInterceptors=#clientOutLogger&supportPagination=true"


    static void main(args) {
        System.setProperty(PayloadLoggerBase.PROPERTY_DISABLED, 'true')
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeClass
    static void classSetUp() {
        System.setProperty(PayloadLoggerBase.PROPERTY_DISABLED, 'true')
        PayloadLoggingContext.applicationId = 'hpdserver'
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @Test
    void testIti58() {
        BatchRequest batchRequest = new BatchRequest(
                requestID: '1',
                batchRequests: [
                        new SearchRequest(
                                requestID: '2',
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

        BatchResponse batchResponse = sendIt(SERVICE1, batchRequest)
        assert batchResponse != null
        assert batchResponse.getBatchResponses().empty
    }

    @Test
    void testIti58Pagination() {
        BatchRequest batchRequest = new BatchRequest(
                requestID: '1',
                batchRequests: [
                        // request with pagination, response with multiple pages
                        new SearchRequest(
                                requestID: '2',
                                control: [
                                        ControlUtils.toDsmlv2(new Pagination(10, null, true)),
                                ],
                                dn: 'ou=2,O=HPDTEST1,DC=HPD',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(present: new AttributeDescription(name: 'uid')),
                        ),
                        // request with pagination, response with single page
                        new SearchRequest(
                                requestID: '3',
                                control: [
                                        ControlUtils.toDsmlv2(new Pagination(10, null, true)),
                                ],
                                dn: 'ou=3,O=HPDTEST1,DC=HPD',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(present: new AttributeDescription(name: 'uid')),
                        ),
                        // request without pagination, response with single page
                        new SearchRequest(
                                requestID: '4',
                                dn: 'ou=4,O=HPDTEST1,DC=HPD',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(present: new AttributeDescription(name: 'uid')),
                        ),
                        // request without pagination, response unexpectedly paginated (server bug)
                        new SearchRequest(
                                requestID: '5',
                                dn: 'ou=5,O=HPDTEST1,DC=HPD',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(present: new AttributeDescription(name: 'uid')),
                        ),
                        // not a search request with pagination -- pagination shall be ignored
                        new DelRequest(
                                requestID: '6',
                                dn: 'ou=6,O=HPDTEST1,DC=HPD',
                                control: [
                                        ControlUtils.toDsmlv2(new Pagination(10, null, true)),
                                ],
                        ),
                        // not a search request without pagination
                        new DelRequest(
                                requestID: '7',
                                dn: 'ou=7,O=HPDTEST1,DC=HPD',
                        ),
                        // not a search request without pagination, response unexpectedly paginated (server bug)
                        new DelRequest(
                                requestID: '8',
                                dn: 'ou=8,O=HPDTEST1,DC=HPD',
                        ),
                        // request with pagination, error response
                        new SearchRequest(
                                requestID: '9',
                                control: [
                                        ControlUtils.toDsmlv2(new Pagination(10, null, true)),
                                ],
                                dn: 'ou=9,O=HPDTEST1,DC=HPD',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(present: new AttributeDescription(name: 'uid')),
                        ),
                        // request with pagination, response with pagination, but status code not 0
                        new SearchRequest(
                                requestID: '10',
                                control: [
                                        ControlUtils.toDsmlv2(new Pagination(10, null, true)),
                                ],
                                dn: 'ou=10,O=HPDTEST1,DC=HPD',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(present: new AttributeDescription(name: 'uid')),
                        ),
                        // request with pagination, wrong response type with pagination
                        new SearchRequest(
                                requestID: '11',
                                control: [
                                        ControlUtils.toDsmlv2(new Pagination(10, null, true)),
                                ],
                                dn: 'ou=11,O=HPDTEST1,DC=HPD',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(present: new AttributeDescription(name: 'uid')),
                        ),
                ],
        )

        BatchResponse batchResponse = sendIt(SERVICE2, batchRequest)

        def map = batchResponse.batchResponses.collectEntries { [(it.value.requestID): it.value] }
        assert map.size() == batchRequest.batchRequests.size()

        assert map['2'] instanceof SearchResponse
        assert map['2'].searchResultEntry.size() == 101

        assert map['3'] instanceof SearchResponse
        assert map['3'].searchResultEntry.size() == 6

        assert map['4'] instanceof SearchResponse
        assert map['4'].searchResultEntry.size() == 6

        assert map['5'] instanceof SearchResponse
        assert map['5'].searchResultEntry.size() == 152

        assert map['6'] instanceof LDAPResult
        assert map['7'] instanceof LDAPResult
        assert map['8'] instanceof LDAPResult

        assert map['9'] instanceof ErrorResponse

        assert map['10'] instanceof SearchResponse
        assert map['10'].searchResultEntry.size() == 10

        assert map['11'] instanceof LDAPResult
    }

    BatchResponse sendIt(String endpoint, BatchRequest request) {
        return send(endpoint, request, BatchResponse.class)
    }

    void doTestDsmlValueJaxb(String fileName, Class expectedClass) {
        def inputStream = getClass().classLoader.getResourceAsStream(fileName)
        def reader = StaxUtils.createXMLStreamReader(inputStream)
        def dataReader = new DataReaderImpl(new JAXBDataBinding(HpdValidator.JAXB_CONTEXT), false)

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

