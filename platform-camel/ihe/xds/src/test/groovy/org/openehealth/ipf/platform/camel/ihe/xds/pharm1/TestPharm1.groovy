/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.pharm1

import org.apache.camel.RuntimeCamelException
import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.xds.core.SampleData
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDispensesQuery
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer

import static org.junit.jupiter.api.Assertions.fail
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS

/**
 * Tests the PHARM-1 component with the webservice and the client defined within the URI.
 * @author Jens Riemschneider
 * @author Quentin Ligier
 */
class TestPharm1 extends XdsStandardTestContainer {

    def static CONTEXT_DESCRIPTOR = 'pharm-1.xml'

    def SERVICE1 = "cmpd-pharm1://localhost:${port}/cmpd-pharm1-service1"
    def SERVICE2 = "cmpd-pharm1://localhost:${port}/cmpd-pharm1-service2"
    def SAMPLE_SERVICE = "cmpd-pharm1://localhost:${port}/myPharm1Service?features=#loggingFeature"

    def SERVICE2_ADDR = "http://localhost:${port}/cmpd-pharm1-service2"

    QueryRegistry request
    FindDispensesQuery query

    static void main(args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, DEMO_APP_PORT)
    }

    @BeforeAll
    static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR)
    }

    @BeforeEach
    void setUp() {
        request = SampleData.createFindDispensesQuery()
        query = request.query

        QueryList<String> params1 = new QueryList<>()
        params1.outerList << ['a', 'b', 'c']
        params1.outerList << ['d', 'e', 'f']
        params1.outerList << ['h', 'i', 'j']

        QueryList<String> params2 = new QueryList<>()
        params2.outerList << ['12', '34']
        params2.outerList << ['56', '78', '90']

        query.extraParameters['$urn:oehf:test1'] = params1
        query.extraParameters['$urn:oehf:test2'] = params2
    }

    @Test
    void testComponentCanBeRestarted() {
        camelContext.stopRoute('service1route')
        try {
            sendIt(SERVICE1, 'service 1').status
            fail('Expected exception: ' + RuntimeCamelException.class)
        }
        catch (Exception ignored) {
        }

        camelContext.startRoute('service1route')
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
    }

    @Test
    void testPharm1() {
        assert SUCCESS == sendIt(SERVICE1, 'service 1').status
        assert SUCCESS == sendIt(SERVICE2, 'service 2').status
        assert auditSender.messages.size() == 4
        checkAudit(EventOutcomeIndicator.Success)
    }

    @Test
    void testPharm1FailureAudit() {
        assert FAILURE == sendIt(SERVICE2, 'falsch').status
        assert auditSender.messages.size() == 2
        checkAudit(EventOutcomeIndicator.SeriousFailure)
    }

    def checkAudit(EventOutcomeIndicator outcome) {
        List<AuditMessage> messages = getAudit(EventActionCode.Execute, SERVICE2_ADDR)
        assert messages.size() == 2
        messages.each { message ->
            assert message.activeParticipants.size() == 2
            assert message.participantObjectIdentifications.size() == 2

            checkEvent(message.eventIdentification, '110112', 'PHARM-1', EventActionCode.Execute, outcome)
            checkSource(message.activeParticipants[0], true)
            checkDestination(message.activeParticipants[1], SERVICE2_ADDR, false)
            checkPatient(message.participantObjectIdentifications[0])
            checkQuery(message.participantObjectIdentifications[1], 'PHARM-1', QueryType.FIND_DISPENSES.getId(), QueryType.FIND_DISPENSES.getId())
        }
    }

    @Test
    void testSample() {
        def response = send(SAMPLE_SERVICE, SampleData.createFindDispensesQuery(), QueryResponse.class)
        assert SUCCESS == response.status
        assert 1 == response.references.size()
        assert 'document01' == response.references[0].id

        response = send(SAMPLE_SERVICE, SampleData.createFindMedicationListQuery(), QueryResponse.class)
        assert SUCCESS == response.status
        assert 1 == response.references.size()
        assert 'document01' == response.references[0].id

        response = send(SAMPLE_SERVICE, SampleData.createFindPrescriptionsQuery(), QueryResponse.class)
        assert FAILURE == response.status

        assert auditSender.messages.size() == 6
        auditSender.messages.each { message ->
            assert message.activeParticipants.size() == 2
            assert message.participantObjectIdentifications.size() == 2
        }

        AuditMessage message = auditSender.messages[0] // FindDispensesQuery, Querying actor audit message
        checkEvent(message.eventIdentification, '110112', 'PHARM-1', EventActionCode.Execute, EventOutcomeIndicator.Success)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], "http://localhost:${port}/myPharm1Service", false)
        checkPatient(message.participantObjectIdentifications[0])
        checkQuery(message.participantObjectIdentifications[1], 'PHARM-1', QueryType.FIND_DISPENSES.getId(), QueryType.FIND_DISPENSES.getId())

        message = auditSender.messages[1] // FindDispensesQuery, Community Pharmacy Manager audit message
        checkEvent(message.eventIdentification, '110112', 'PHARM-1', EventActionCode.Execute, EventOutcomeIndicator.Success)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], "http://localhost:${port}/myPharm1Service", false)
        checkPatient(message.participantObjectIdentifications[0])
        checkQuery(message.participantObjectIdentifications[1], 'PHARM-1', QueryType.FIND_DISPENSES.getId(), QueryType.FIND_DISPENSES.getId())

        message = auditSender.messages[2] // FindMedicationListQuery, Querying actor audit message
        checkEvent(message.eventIdentification, '110112', 'PHARM-1', EventActionCode.Execute, EventOutcomeIndicator.Success)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], "http://localhost:${port}/myPharm1Service", false)
        checkPatient(message.participantObjectIdentifications[0])
        checkQuery(message.participantObjectIdentifications[1], 'PHARM-1', QueryType.FIND_MEDICATION_LIST.getId(), QueryType.FIND_MEDICATION_LIST.getId())

        message = auditSender.messages[3] // FindMedicationListQuery, Community Pharmacy Manager audit message
        checkEvent(message.eventIdentification, '110112', 'PHARM-1', EventActionCode.Execute, EventOutcomeIndicator.Success)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], "http://localhost:${port}/myPharm1Service", false)
        checkPatient(message.participantObjectIdentifications[0])
        checkQuery(message.participantObjectIdentifications[1], 'PHARM-1', QueryType.FIND_MEDICATION_LIST.getId(), QueryType.FIND_MEDICATION_LIST.getId())

        message = auditSender.messages[4] // FindPrescriptionsQuery, Querying actor audit message
        checkEvent(message.eventIdentification, '110112', 'PHARM-1', EventActionCode.Execute, EventOutcomeIndicator.SeriousFailure)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], "http://localhost:${port}/myPharm1Service", false)
        checkPatient(message.participantObjectIdentifications[0])
        checkQuery(message.participantObjectIdentifications[1], 'PHARM-1', QueryType.FIND_PRESCRIPTIONS.getId(), QueryType.FIND_PRESCRIPTIONS.getId())

        message = auditSender.messages[5] // FindPrescriptionsQuery, Community Pharmacy Manager audit message
        checkEvent(message.eventIdentification, '110112', 'PHARM-1', EventActionCode.Execute, EventOutcomeIndicator.SeriousFailure)
        checkSource(message.activeParticipants[0], true)
        checkDestination(message.activeParticipants[1], "http://localhost:${port}/myPharm1Service", false)
        checkPatient(message.participantObjectIdentifications[0])
        checkQuery(message.participantObjectIdentifications[1], 'PHARM-1', QueryType.FIND_PRESCRIPTIONS.getId(), QueryType.FIND_PRESCRIPTIONS.getId())

        [4, 5].each { i ->
            boolean found = false
            message = auditSender.messages[i]
            for (detail in message.participantObjectIdentifications[1].participantObjectDetails) {
                if ((detail.type == 'urn:ihe:iti:xca:2010:homeCommunityId') && detail.value) {
                    found = true
                }
            }
            assert found
        }
    }

    def sendIt(endpoint, value) {
        query.authorPersons = [value]
        send(endpoint, request, QueryResponse.class)
    }
}
