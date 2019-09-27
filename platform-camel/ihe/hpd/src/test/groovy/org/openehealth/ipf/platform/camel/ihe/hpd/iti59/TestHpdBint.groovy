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
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * @author Dmytro Rud
 */
class TestHpdBint extends StandardTestContainer {

    static final String CONTEXT_DESCRIPTOR = 'iti-59.xml'

    final String SERVICE_ITI58_A = "hpd-iti58://77.109.165.162:8081/csp/healthshare/bmhpd-com-a/bintmed.hpd.service.WSHttpBindingIProviderInformationDirectory.cls?inInterceptors=#inLogger&outInterceptors=#outLogger"
    final String SERVICE_ITI58_B = "hpd-iti58://77.109.165.162:8081/csp/healthshare/bmhpd-com-b/bintmed.hpd.service.WSHttpBindingIProviderInformationDirectory.cls?inInterceptors=#inLogger&outInterceptors=#outLogger"
    final String SERVICE_ITI59 = "hpd-iti59://10.2.101.10:11317/csp/healthshare/bmhpd/bintmed.hpd.service.WSHttpBindingIProviderInformationDirectory.cls?inInterceptors=#inLogger&outInterceptors=#outLogger"

    @BeforeClass
    static void classSetUp() {
        System.setProperty(PayloadLoggerBase.PROPERTY_CONSOLE, 'true')
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    BatchResponse sendIt(String endpoint, BatchRequest request) {
        return send(endpoint, request, BatchResponse.class)
    }

    @Test
    void test_38757_1a() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new SearchRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'ou=HCRegulatedOrganization,o=BAG,dc=HPD,c=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        substrings: new SubstringFilter(
                                                name: 'HcRegisteredName',
                                                any: [
                                                        'Spital X'
                                                ],
                                        ),
                                ),
                                attributes: new AttributeDescriptions(
                                        attribute: [
                                                new AttributeDescription(
                                                        name: 'uid',
                                                ),
                                        ],
                                ),
                        ),
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI58_A, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        SearchResponse searchResponse = batchResponse.batchResponses[0].value
        assert searchResponse.searchResultEntry.size() == 1
        def ownerDn = searchResponse.searchResultEntry[0].dn
        println "Spital A DN = ${ownerDn}"
    }

    @Test
    void test_38757_1() {
        def uid = 'BINT:org-38757-1'

        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new AddRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: "uid=${uid},ou=HCRegulatedOrganization,dc=HPD,o=BAG,c=CH".toString(),
                                attr: [
                                        new DsmlAttr(name: 'uid',                           value: [uid]),
                                        new DsmlAttr(name: 'hcIdentifier',                  value: ['RefData:OID:1.2.3.38757.1:active']),
                                        new DsmlAttr(name: 'O',                             value: ['Spital X']),
                                        new DsmlAttr(name: 'hcRegisteredName',              value: ['Spital X']),
                                        new DsmlAttr(name: 'businessCategory',              value: ['BAG:2.16.840.1.113883.6.96:22232009']),
                                        new DsmlAttr(name: 'objectClass',                   value: ['top', 'HPDProvider', 'Organization', 'HCRegulatedOrganization']),
                                ]
                        )
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI59, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        LDAPResult addResponse = batchResponse.batchResponses[0].value
        assert addResponse.resultCode.code == 0
    }

    @Test
    void test_38757_2() {
        def uid = 'BINT:ind-38757-2'
        def firstName = 'Dmytro'
        def lastName = 'Rud1'
        def title = 'Dr.'

        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                    new AddRequest(
                            requestID: UUID.randomUUID().toString(),
                            dn: "uid=${uid},ou=HCProfessional,dc=HPD,o=BAG,c=CH".toString(),
                            attr: [
                                    new DsmlAttr(name: 'uid',                           value: [uid]),
                                    new DsmlAttr(name: 'hcIdentifier',                  value: ['RefData:GLN:7603875710000:active']),
                                    new DsmlAttr(name: 'hcProfession',                  value: ['BAG:2.16.756.5.30.1.127.3.10.9:00100']),
                                    new DsmlAttr(name: 'description',                   value: ['Aktivierungsfachmann']),
                                    new DsmlAttr(name: 'displayName',                   value: [[title, firstName, lastName].join(' ')]),
                                    new DsmlAttr(name: 'title',                         value: [title]),
                                    new DsmlAttr(name: 'givenName',                     value: [firstName]),
                                    new DsmlAttr(name: 'sn',                            value: [lastName]),
                                    new DsmlAttr(name: 'cn',                            value: [[lastName, firstName, uid].join(', ')]),
                                    new DsmlAttr(name: 'hpdProviderLanguageSupported',  value: ['de', 'en', 'uk', 'ru']),
                                    new DsmlAttr(name: 'gender',                        value: ['M']),
                                    new DsmlAttr(name: 'mail',                          value: ['dmytro.rud.1@post.ch']),
                                    new DsmlAttr(name: 'physicalDeliveryOfficeName',    value: ['Development and Innovation']),
                                    new DsmlAttr(name: 'telephoneNumber',               value: ['+4141000000001']),
                                    new DsmlAttr(name: 'mobile',                        value: ['+4176000000001']),
                                    new DsmlAttr(name: 'hcRegistrationStatus',          value: ['Unknown']),
                                    new DsmlAttr(name: 'objectClass',                   value: ['top', 'HPDProvider', 'HCProfessional', 'person', 'organizationalPerson', 'inetOrgPerson']),
                            ]
                    )
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI59, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        LDAPResult addResponse = batchResponse.batchResponses[0].value
        assert addResponse.resultCode.code == 0
    }

    @Test
    void test_38757_3() {
        def uid = 'BINT:ind-38757-3'
        def firstName = 'Manfred'
        def lastName = 'Weiss'

        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new AddRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: "uid=${uid},ou=HCProfessional,dc=HPD,o=BAG,c=CH".toString(),
                                attr: [
                                        new DsmlAttr(name: 'uid',                           value: [uid]),
                                        new DsmlAttr(name: 'hcIdentifier',                  value: ['RefData:GLN:7603875720000:active']),
                                        new DsmlAttr(name: 'hcProfession',                  value: ['BAG:2.16.756.5.30.1.127.3.10.9:00100']),
                                        new DsmlAttr(name: 'description',                   value: ['Aktivierungsfachmann']),
                                        new DsmlAttr(name: 'displayName',                   value: [[firstName, lastName].join(' ')]),
                                        new DsmlAttr(name: 'givenName',                     value: [firstName]),
                                        new DsmlAttr(name: 'sn',                            value: [lastName]),
                                        new DsmlAttr(name: 'cn',                            value: [[lastName, firstName, uid].join(', ')]),
                                        new DsmlAttr(name: 'hpdProviderLanguageSupported',  value: ['de', 'en']),
                                        new DsmlAttr(name: 'gender',                        value: ['M']),
                                        new DsmlAttr(name: 'mail',                          value: ['manfred.weiss.2@post.ch']),
                                        new DsmlAttr(name: 'physicalDeliveryOfficeName',    value: ['Development and Innovation']),
                                        new DsmlAttr(name: 'telephoneNumber',               value: ['+4141000000002']),
                                        new DsmlAttr(name: 'mobile',                        value: ['+4176000000002']),
                                        new DsmlAttr(name: 'hcRegistrationStatus',          value: ['Unknown']),
                                        new DsmlAttr(name: 'objectClass',                   value: ['top', 'HPDProvider', 'HCProfessional', 'person', 'organizationalPerson', 'inetOrgPerson']),
                                ]
                        )
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI59, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        LDAPResult addResponse = batchResponse.batchResponses[0].value
        assert addResponse.resultCode.code == 0
    }


    @Test
    void test_38757_4() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new AddRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: "cn=BINT:rel-38757,ou=Relationship,dc=HPD,o=BAG,c=CH".toString(),
                                attr: [
                                        new DsmlAttr(name: 'cn',            value: ['BINT:rel-38757']),
                                        new DsmlAttr(name: 'owner',         value: ['uid=BINT:org-38757-1,ou=HCRegulatedOrganization,dc=HPD,o=BAG,c=CH']),
                                        new DsmlAttr(name: 'member',        value: ['uid=BINT:ind-38757-2,ou=HCProfessional,dc=HPD,o=BAG,c=CH',
                                                                                    'uid=BINT:ind-38757-3,ou=HCProfessional,dc=HPD,o=BAG,c=CH']),
                                        new DsmlAttr(name: 'objectClass',   value: ['top', 'groupOfNames']),
                                ]
                        )
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI59, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        LDAPResult addResponse = batchResponse.batchResponses[0].value
        assert addResponse.resultCode.code == 0
    }


    @Test
    void test_38757_5() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new ModifyRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'uid=BINT:ind-38757-2,ou=HCProfessional,dc=HPD,o=BAG,c=CH',
                                modification: [
                                        new DsmlModification(
                                                operation: DsmlModification.ModificationOperationType.REPLACE,
                                                name: 'mobile',
                                                value: ['+41767390123']),
                                        new DsmlModification(
                                                operation: DsmlModification.ModificationOperationType.ADD,
                                                name: 'facsimileTelephoneNumber',
                                                value: ['+41234567899']),
                                ]
                        )
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI59, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        LDAPResult addResponse = batchResponse.batchResponses[0].value
        assert addResponse.resultCode.code == 0
    }


    @Test
    void test_38757_6() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new DelRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'uid=BINT:ind-38757-3,ou=HCProfessional,dc=HPD,o=BAG,c=CH',
                        )
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI59, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        LDAPResult addResponse = batchResponse.batchResponses[0].value
        assert addResponse.resultCode.code == 0
    }


    @Test
    void test_38757_7() {
        def uid = 'BINT:org-38757-7'

        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new AddRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: "uid=${uid},ou=HCRegulatedOrganization,dc=HPD,o=BAG,c=CH".toString(),
                                attr: [
                                        new DsmlAttr(name: 'uid',                           value: [uid]),
                                        new DsmlAttr(name: 'hcIdentifier',                  value: ['RefData:OID:1.2.3.38757.7:active']),
                                        new DsmlAttr(name: 'O',                             value: ['Post CH AG']),
                                        new DsmlAttr(name: 'hcRegisteredName',              value: ['Post CH AG']),
                                        new DsmlAttr(name: 'businessCategory',              value: ['BAG:2.16.840.1.113883.6.96:22232009']),
                                        new DsmlAttr(name: 'objectClass',                   value: ['top', 'HPDProvider', 'Organization', 'HCRegulatedOrganization']),
                                ]
                        )
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI59, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        LDAPResult addResponse = batchResponse.batchResponses[0].value
        assert addResponse.resultCode.code == 0
    }


    @Test
    void test_38758_100() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new SearchRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'ou=HCRegulatedOrganization,o=BAG,dc=HPD,c=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        substrings: new SubstringFilter(
                                                name: 'hcIdentifier',
                                                initial: 'RefData:OID:2.16.10.89.201:'
                                        ),
                                ),
                                attributes: new AttributeDescriptions(
                                        attribute: [
                                                new AttributeDescription(
                                                        name: 'O',
                                                ),
                                        ],
                                ),
                        ),
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI58_A, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        SearchResponse searchResponse = batchResponse.batchResponses[0].value
        assert searchResponse.searchResultEntry.size() == 1
    }


    @Test
    void test_38758_105() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new SearchRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'ou=HCRegulatedOrganization,o=BAG,dc=HPD,c=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        substrings: new SubstringFilter(
                                                name: 'O',
                                                any: ['Spital Y']
                                        ),
                                ),
                        ),
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI58_B, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        SearchResponse searchResponse = batchResponse.batchResponses[0].value
        assert searchResponse.searchResultEntry.size() == 1
    }

    @Test
    void test_38758_110() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new SearchRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'ou=HCRegulatedOrganization,o=BAG,dc=HPD,c=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        substrings: new SubstringFilter(
                                                name: 'hcRegisteredName',
                                                any: ['Spital X']
                                        ),
                                ),
                        ),
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI58_A, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        SearchResponse searchResponse = batchResponse.batchResponses[0].value
        assert searchResponse.searchResultEntry.size() == 1
    }

    @Test
    void test_38758_200() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new SearchRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'ou=HCProfessional,o=BAG,dc=HPD,c=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        substrings: new SubstringFilter(
                                                name: 'hcSpecialisation',
                                                _final: 'oncology'
                                        ),
                                ),
                                attributes: new AttributeDescriptions(
                                        attribute: [
                                                new AttributeDescription(name: 'uid'),
                                                new AttributeDescription(name: 'sn'),
                                                new AttributeDescription(name: 'givenName'),
                                                new AttributeDescription(name: 'displayName'),
                                        ]
                                )

                        ),
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI58_B, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        SearchResponse searchResponse = batchResponse.batchResponses[0].value
        assert searchResponse.searchResultEntry.size() == 1
    }

    @Test
    void test_38758_205() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new SearchRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'ou=HCProfessional,o=BAG,dc=HPD,c=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        substrings: new SubstringFilter(
                                                name: 'displayName',
                                                any: ['Marston']
                                        ),
                                ),
                                attributes: new AttributeDescriptions(
                                        attribute: [
                                                new AttributeDescription(name: 'uid'),
                                                new AttributeDescription(name: 'sn'),
                                                new AttributeDescription(name: 'givenName'),
                                                new AttributeDescription(name: 'cn'),
                                                new AttributeDescription(name: 'displayName'),
                                        ]
                                )
                        ),
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI58_B, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        SearchResponse searchResponse = batchResponse.batchResponses[0].value
        assert searchResponse.searchResultEntry.size() == 1
    }

    @Test
    void test_38758_300() {
        def batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: [
                        new SearchRequest(
                                requestID: UUID.randomUUID().toString(),
                                dn: 'ou=Relationship,o=BAG,dc=HPD,c=CH',
                                scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                                derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                                filter: new Filter(
                                        equalityMatch: new AttributeValueAssertion(
                                                name: 'owner',
                                                value: 'uid=CommunityB:00000002000,OU=HCRegulatedOrganization,DC=HPD,O=BAG,C=CH',
                                        ),
                                ),
                                attributes: new AttributeDescriptions(
                                        attribute: [
                                                new AttributeDescription(name: 'owner'),
                                                new AttributeDescription(name: 'member'),
                                        ]
                                )
                        ),
                ],
        )

        def batchResponse = sendIt(SERVICE_ITI58_B, batchRequest)
        assert batchResponse.batchResponses.size() == 1
        SearchResponse searchResponse = batchResponse.batchResponses[0].value
        assert searchResponse.searchResultEntry.size() == 1

        def memberSearchRequests = searchResponse.searchResultEntry[0].attr
                .findAll { it.name == 'member' }
                .collect { it.value }
                .flatten()
                .collect {
                    def s = it.toString()
                    s = s.substring(s.indexOf('=') + 1)
                    s = s.substring(0, s.indexOf(','))

                    return new SearchRequest(
                            requestID: UUID.randomUUID().toString(),
                            dn: 'ou=HCProfessional,dc=HPD,o=BAG,c=CH',
                            scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                            derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                            filter: new Filter(
                                    equalityMatch: new AttributeValueAssertion(
                                            name: 'uid',
                                            value: s,
                                    ),
                            ),
                            attributes: new AttributeDescriptions(
                                    attribute: [
                                            new AttributeDescription(name: 'uid'),
                                            new AttributeDescription(name: 'displayName'),
                                    ],
                            ),
                    )
        }

        assert memberSearchRequests.size() == 3

        batchRequest = new BatchRequest(
                requestID: UUID.randomUUID().toString(),
                batchRequests: memberSearchRequests,
        )

        batchResponse = sendIt(SERVICE_ITI58_B, batchRequest)


    }





}
