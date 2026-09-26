/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti104

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException
import ca.uhn.hl7v2.model.Message
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hl7v2.PIX

import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * Translation of PIX Feed [ITI-8] acknowledgements into PIXm Patient Identity Feed [ITI-104] responses
 */
class PixFeedResponseToPixmFeedResponseTranslatorTest {

    private PixFeedResponseToPixmFeedResponseTranslator translator

    @BeforeEach
    void setup() {
        translator = new PixFeedResponseToPixmFeedResponseTranslator()
    }

    @Test
    void testRevisedPatient() {
        def outcome = translator.translateToFhir(ack('A08', 'MSA|AA|4711'), [:])
        assertFalse(outcome.created)
    }

    @Test
    void testCreatedPatientByEventTypeParameter() {
        def outcome = translator.translateToFhir(ack('A08', 'MSA|AA|4711'),
            [(PixmFeedRequestToPixFeedTranslator.PIX_FEED_EVENT_TYPE): 'A04'])
        assertTrue(outcome.created)
    }

    @Test
    void testCreatedPatientByAcknowledgement() {
        def outcome = translator.translateToFhir(ack('A01', 'MSA|AA|4711'), [:])
        assertTrue(outcome.created)
    }

    @Test
    void testUnknownDomain() {
        assertThrows(InvalidRequestException) {
            translator.translateToFhir(ack('A08', 'MSA|AE|4711|Unknown key identifier', 'ERR|PID^1^3^204&Unknown key identifier&HL70357'), [:])
        }
    }

    @Test
    void testUnknownSubsumedPatient() {
        assertThrows(ResourceNotFoundException) {
            translator.translateToFhir(ack('A40', 'MSA|AE|4711|Unknown key identifier', 'ERR|MRG^1^1^204&Unknown key identifier&HL70357'), [:])
        }
    }

    @Test
    void testRejected() {
        assertThrows(UnprocessableEntityException) {
            translator.translateToFhir(ack('A08', 'MSA|AR|4711|Unsupported event code'), [:])
        }
    }

    private static Message ack(String trigger, String... segments) {
        def message = "MSH|^~\\&|PIX adapter|IPF|app|fac|20260926120000||ACK^${trigger}|123|P|2.3.1\r" + segments.join('\r') + '\r'
        PIX.FeedInteractions.ITI_8_PIX.hl7v2TransactionConfiguration.hapiContext.pipeParser.parse(message.toString())
    }
}
