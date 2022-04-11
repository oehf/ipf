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

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*

import javax.naming.ldap.PagedResultsControl

import static org.openehealth.ipf.platform.camel.ihe.hpd.HpdCamelValidators.iti58RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.hpd.HpdCamelValidators.iti58ResponseValidator

/**
 * @author Dmytro Rud
 */
class Iti58TestRouteBuilder extends RouteBuilder {

    static final ObjectFactory DSML2_OBJECT_FACTORY = new ObjectFactory()

    static int paginationRequestNumber = 0

    void configure() throws Exception {
        from('hpd-iti58:hpd-service1?inInterceptors=#serverInLogger&outInterceptors=#serverOutLogger')
                .process(iti58RequestValidator())
                .process {
                    BatchRequest batchRequest = it.in.getMandatoryBody(BatchRequest.class)
                    it.message.body = new BatchResponse(requestID: batchRequest.requestID)
                }
                .process(iti58ResponseValidator())

        from('hpd-iti58:hpd-service2?inInterceptors=#serverInLogger&outInterceptors=#serverOutLogger&supportPagination=true&paginationStorage=#paginationStorage&supportSorting=true')
        //.process(iti58RequestValidator())
                .process {
                    def batchResponse = new BatchResponse(requestID: '1')
                    if (paginationRequestNumber == 0) {
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseDelResponse(new LDAPResult(
                                requestID: '7771',
                                resultCode: new ResultCode(code: 0),
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseDelResponse(new LDAPResult(
                                requestID: null,
                                resultCode: new ResultCode(code: 0),
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseSearchResponse(new SearchResponse(
                                requestID: '2',
                                searchResultDone: new LDAPResult(resultCode: new ResultCode(code: 0)),
                                searchResultEntry: (0..100).collect {
                                    new SearchResultEntry(dn: "uid=2_${it}")
                                }
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseSearchResponse(new SearchResponse(
                                requestID: '3',
                                searchResultDone: new LDAPResult(resultCode: new ResultCode(code: 0)),
                                searchResultEntry: [
                                        new SearchResultEntry(
                                                dn: 'uid=3_0',
                                                attr: [
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: (0..5).collect { randomString() },
                                                        ),
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: (0..5).collect { randomString() },
                                                        ),
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: (0..5).collect { randomString() },
                                                        ),
                                                ],
                                        ),
                                        new SearchResultEntry(
                                                dn: 'uid=3_1',
                                                attr: [
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: (0..5).collect { randomString() },
                                                        ),
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: [
                                                                        // empty
                                                                ],
                                                        ),
                                                ],
                                        ),
                                        new SearchResultEntry(
                                                dn: 'uid=3_2',
                                                attr: [
                                                        // empty
                                                ],
                                        ),
                                        new SearchResultEntry(
                                                dn: 'uid=3_3',
                                                attr: [
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: (0..7).collect { randomString() },
                                                        ),
                                                ],
                                        ),
                                        new SearchResultEntry(
                                                dn: 'uid=3_4',
                                                attr: [
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: [
                                                                        // empty
                                                                ],
                                                        ),
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: [
                                                                        // empty
                                                                ],
                                                        ),
                                                ],
                                        ),
                                        new SearchResultEntry(
                                                dn: 'uid=3_5',
                                                attr: [
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: (0..5).collect { randomString() },
                                                        ),
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: (0..5).collect { randomString() },
                                                        ),
                                                        new DsmlAttr(
                                                                name: 'abcd',
                                                                value: (0..5).collect { randomString() },
                                                        ),
                                                ],
                                        ),
                                ],
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseSearchResponse(new SearchResponse(
                                requestID: '4',
                                searchResultDone: new LDAPResult(resultCode: new ResultCode(code: 0)),
                                searchResultEntry: (0..5).collect {
                                    new SearchResultEntry(dn: "uid=4_${it}")
                                }
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseSearchResponse(new SearchResponse(
                                requestID: '5',
                                searchResultDone: new LDAPResult(
                                        resultCode: new ResultCode(code: 0),
                                        control: [
                                                ControlUtils.toDsmlv2(new PagedResultsControl(0, [5, 5, 5] as byte[], true))
                                        ],
                                ),
                                searchResultEntry: (0..100).collect {
                                    new SearchResultEntry(dn: "uid=5_${it}")
                                }
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseDelResponse(new LDAPResult(
                                requestID: '6',
                                resultCode: new ResultCode(code: 0),
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseDelResponse(new LDAPResult(
                                requestID: '7',
                                resultCode: new ResultCode(code: 0),
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseDelResponse(new LDAPResult(
                                requestID: '8',
                                control: [
                                        ControlUtils.toDsmlv2(new PagedResultsControl(0, [8, 8, 8] as byte[], true))
                                ],
                                resultCode: new ResultCode(code: 0),
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseErrorResponse(new ErrorResponse(
                                requestID: '9',
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseSearchResponse(new SearchResponse(
                                requestID: '10',
                                searchResultDone: new LDAPResult(
                                        resultCode: new ResultCode(code: 666),
                                ),
                                searchResultEntry: (0..100).collect {
                                    new SearchResultEntry(dn: "uid=10_${it}")
                                }
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseDelResponse(new LDAPResult(
                                requestID: '11',
                                control: [
                                        ControlUtils.toDsmlv2(new PagedResultsControl(0, [11, 11, 11] as byte[], true))
                                ],
                                resultCode: new ResultCode(code: 0),
                        ))
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseDelResponse(new LDAPResult(
                                requestID: '7773',
                                resultCode: new ResultCode(code: 0),
                        ))
                    } else if (paginationRequestNumber == 1) {
                        batchResponse.batchResponses << DSML2_OBJECT_FACTORY.createBatchResponseSearchResponse(new SearchResponse(
                                requestID: '5',
                                searchResultDone: new LDAPResult(
                                        resultCode: new ResultCode(code: 0),
                                        control: [
                                                ControlUtils.toDsmlv2(new PagedResultsControl(0, null, true))
                                        ],
                                ),
                                searchResultEntry: (0..50).collect {
                                    new SearchResultEntry(dn: "uid=5_${it}")
                                }
                        ))
                    }
                    ++paginationRequestNumber
                    it.message.body = batchResponse
                }
                .process {}
        //.process(iti58ResponseValidator())
    }

    private static final String randomString() {
        char[] chars = new char[Math.random() * 10]
        for (int i = 0; i < chars.length; ++i) {
            char ch = (char) 'a' + (int) (Math.random() * 26)
            chars[i] = (Math.random() < 0.5) ? ch.toUpperCase() : ch
        }
        return new String(chars)
    }
}
