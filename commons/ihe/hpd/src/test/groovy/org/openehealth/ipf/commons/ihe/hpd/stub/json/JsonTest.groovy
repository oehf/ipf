/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.stub.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.util.logging.Slf4j
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hpd.HpdValidator
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*
import org.openehealth.ipf.commons.xml.XmlUtils

import javax.xml.bind.JAXBElement

@Slf4j
class JsonTest {

    static final ObjectFactory OBJECT_FACTORY = new ObjectFactory()

    static ObjectMapper mapper

    @BeforeAll
    static void beforeAll() {
        mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
    }

    @Test
    void testBatchRequest() {
        def batchRequest1 = new BatchRequest(
            requestID: '123',
            batchRequests: [
                new SearchRequest(
                    requestID: '124',
                    dn: 'ou=HPDElectronicService',
                    scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                    derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                    filter: new Filter(
                        equalityMatch: new AttributeValueAssertion(name: 'hcIdentifier', value: '1234567')
                    ),
                    attributes: new AttributeDescriptions(
                        attribute: [
                            new AttributeDescription(name: 'hpdServiceAddress'),
                            new AttributeDescription(name: 'boundInstitution'),
                        ],
                    ),
                ),

            ],
        )

        def json1 = mapper.writeValueAsString(batchRequest1)
        log.debug('JSON 1:\n{}', json1)

        def batchRequest2 = mapper.readValue(json1, BatchRequest.class)
        def json2 = mapper.writeValueAsString(batchRequest2)
        log.debug('JSON 2:\n{}', json2)

        assert json1 == json2
    }

    @Test
    void testBatchResponse() {
        def batchResponseString1 = '''
<ns2:batchResponse xmlns:ns2="urn:oasis:names:tc:DSML:2:0:core">
    <ns2:searchResponse requestID="58">
        <ns2:searchResultDone>
            <ns2:resultCode code="0" descr="success"/>
        </ns2:searchResultDone>
    </ns2:searchResponse>
    <ns2:searchResponse requestID="57">
        <ns2:searchResultEntry dn="uid=comm1:102245,ou=HCProfessional,dc=HPD,o=comm1,c=CH">
            <ns2:attr name="telephoneNumber">
                <ns2:value>765 256 64 60</ns2:value>
            </ns2:attr>
            <ns2:attr name="mobile"/>
            <ns2:attr name="hpdProviderLanguageSupported"/>
            <ns2:attr name="facsimileTelephoneNumber">
                <ns2:value>754 354 00 14 $ Personal</ns2:value>
            </ns2:attr>
            <ns2:attr name="hpdHasAService">
                <ns2:value>hpdServiceId=31a0b48e-b409-4468-9add-341969e003d7:2.16.756.5.30.1.166.0.1,ou=HPDElectronicService,dc=HPD,o=comm1,c=CH</ns2:value>
            </ns2:attr>
            <ns2:attr name="hpdProviderPracticeAddress">
                <ns2:value>status=PRIMARY $ addr=Kantonsspital Graubünden 7000 Chur CH $ city=Chur $ country=CH $ postalCode=7000 $ streetName=Kantonsspital Graubünden $ boundInstitution=None</ns2:value>
            </ns2:attr>
            <ns2:attr name="uid">
                <ns2:value>comm1:102245</ns2:value>
            </ns2:attr>
            <ns2:attr name="hcIdentifier">
                <ns2:value>comm1:GPSID:102245:ACTIVE</ns2:value>
                <ns2:value>SIVC:SIVC-ID:995067:ACTIVE</ns2:value>
            </ns2:attr>
            <ns2:attr name="hpdMedicalRecordsDeliveryEmailAddressBound">
                <ns2:value>medappl@comm1.ch $ Personal</ns2:value>
            </ns2:attr>
            <ns2:attr name="memberOf">
                <ns2:value>cn=relKSGR,ou=Relationship,dc=HPD,o=comm1,c=CH</ns2:value>
            </ns2:attr>
            <ns2:attr name="sn">
                <ns2:value>KSGR Radiologie</ns2:value>
            </ns2:attr>
            <ns2:attr name="objectClass">
                <ns2:value>HCProfessional</ns2:value>
                <ns2:value>HPDProvider</ns2:value>
                <ns2:value>inetOrgPerson</ns2:value>
                <ns2:value>naturalPerson</ns2:value>
                <ns2:value>organizationalPerson</ns2:value>
                <ns2:value>person</ns2:value>
                <ns2:value>top</ns2:value>
            </ns2:attr>
        </ns2:searchResultEntry>
        <ns2:searchResultDone>
            <ns2:resultCode code="0" descr="success"/>
        </ns2:searchResultDone>
    </ns2:searchResponse>
    <ns2:addResponse requestID="314"/>
    <ns2:delResponse requestID="315"/>
</ns2:batchResponse>'''

        JAXBElement<BatchResponse> jaxbElement1 = HpdValidator.JAXB_CONTEXT.createUnmarshaller().unmarshal(XmlUtils.source(batchResponseString1))
        def json1 = mapper.writeValueAsString(jaxbElement1.value)
        log.debug('JSON 1:\n{}', json1)

        def batchResponseIntermediary2 = mapper.readValue(json1, BatchResponseIntermediary.class)
        def batchResponse2 = batchResponseIntermediary2.toBatchResponse()
        def json2 = mapper.writeValueAsString(batchResponse2)
        log.debug('JSON 2:\n{}', json2)

        assert json1 == json2
    }

    @Test
    void testSearchBatchRequest() {
        def batchRequest1 = new BatchRequest(
            requestID: '123',
            processing: BatchRequest.RequestProcessingType.SEQUENTIAL,
            responseOrder: BatchRequest.RequestResponseOrder.SEQUENTIAL,
            onError: BatchRequest.RequestErrorHandlingType.EXIT,
            batchRequests: [
                new SearchRequest(
                    requestID: '124',
                    dn: 'ou=HCProfessional,foo=bar',
                    scope: SearchRequest.SearchScope.WHOLE_SUBTREE,
                    derefAliases: SearchRequest.DerefAliasesType.NEVER_DEREF_ALIASES,
                    sizeLimit: 100L,
                    timeLimit: 200L,
                    typesOnly: false,
                    filter: new Filter(
                        and: new FilterSet(
                            filterGroup: [
                                OBJECT_FACTORY.createFilterSetOr(new FilterSet(
                                    filterGroup: [
                                        OBJECT_FACTORY.createFilterSetSubstrings(new SubstringFilter(name: 'hcIdentifier', initial: 'RefData:', any: ['123', '456'], _final: ':ACTIVE')),
                                        OBJECT_FACTORY.createFilterSetEqualityMatch(new AttributeValueAssertion(name: 'sn', value: 'Schmidt')),
                                        OBJECT_FACTORY.createFilterSetGreaterOrEqual(new AttributeValueAssertion(name: 'creationTimestamp', value: '20240525101112')),
                                        OBJECT_FACTORY.createFilterSetLessOrEqual(new AttributeValueAssertion(name: 'creationTimestamp', value: '20240525101112')),
                                    ],
                                )),
                                OBJECT_FACTORY.createFilterSetNot(new Filter(
                                    or: new FilterSet(
                                        filterGroup: [
                                            OBJECT_FACTORY.createFilterSetPresent(new AttributeDescription(name: 'hcProfession')),
                                            OBJECT_FACTORY.createFilterSetApproxMatch(new AttributeValueAssertion(name: 'telephoneNumber', value: '+4176...')),
                                            OBJECT_FACTORY.createFilterSetExtensibleMatch(new MatchingRuleAssertion(name: 'cn', value: 'Mouse, Mickey, comm1:123', dnAttributes: Boolean.TRUE, matchingRule: 'rule123')),
                                        ],
                                    ),
                                )),
                            ]),
                    ),
                    attributes: new AttributeDescriptions(
                        attribute: [
                            new AttributeDescription(name: 'hpdServiceAddress'),
                            new AttributeDescription(name: 'boundInstitution'),
                        ],
                    ),
                ),
            ],
        )
        def xml1 = XmlUtils.renderJaxb(HpdValidator.JAXB_CONTEXT, batchRequest1, false)
        log.debug('XML 1:\n{}', xml1)

        def batchRequestIntermediary1 = SearchBatchRequestIntermediary.fromBatchRequest(batchRequest1)
        def json1 = mapper.writeValueAsString(batchRequestIntermediary1)
        log.debug('JSON 1:\n{}', json1)

        def batchRequestIntermediary2 = mapper.readValue(json1, SearchBatchRequestIntermediary.class)
        def batchRequest2 = batchRequestIntermediary2.toBatchRequest()
        def xml2 = XmlUtils.renderJaxb(HpdValidator.JAXB_CONTEXT, batchRequest2, false)
        log.debug('XML 2:\n{}', xml2)

        assert xml1 == xml2

        def batchRequestIntermediary3 = SearchBatchRequestIntermediary.fromBatchRequest(batchRequest2)
        def json3 = mapper.writeValueAsString(batchRequestIntermediary3)
        log.debug('JSON 3:\n{}', json3)

        assert json1 == json3
    }

}