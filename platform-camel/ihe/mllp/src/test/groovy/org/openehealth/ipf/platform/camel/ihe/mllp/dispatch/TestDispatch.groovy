/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.dispatch

import org.apache.camel.impl.DefaultExchange
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer

import static org.junit.Assert.assertEquals
import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators.*

/**
 * @author Dmytro Rud
 */
class TestDispatch extends MllpTestContainer {
    static final String CONTEXT_DESCRIPTOR = 'dispatch/dispatch.xml'

    static final String ITI_8_REQUEST =
        'MSH|^~\\&|MESA_PD_SUPPLIER|XYZ_HOSPITAL|dummy|dummy|20081204114742||ADT^A01|123456|T|2.3.1|||ER\n' +
        'EVN|A01|20081204114742\n' +
        'PID|1||001^^^XREF2005~002^^^HIMSS2005||Multiple^Christof^Maria^Prof.|Eisner|' +
        '19530429|M|||Bahnhofstr. 1^^Testort^^01234^DE^H|||||||AccNr01^^^ANICPA|' +
        '111-222-333|\n' +
        'PV1|1|O|\n'

    static final String ITI_9_REQUEST =
        'MSH|^~\\&|MESA_PIX_CLIENT|MESA_DEPARTMENT|MESA_XREF|XYZ_HOSPITAL|'+
        '200603121200||QBP^Q23^QBP_Q21|10501110|P|2.5||||||||\n' +
        'QPD|QRY_1001^Query for Corresponding Identifiers^IHEDEMO|QRY10501110|' +
                'ABC10501^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO^PI|||||\n' +
        'RCP|I||||||\n'

    static final String ITI_64_REQUEST =
        'MSH|^~\\&|REPOSITORY|ENT|RSP1P8|GOOD HEALTH HOSPITAL|200701051530|SEC|ADT^A43^ADT_A43|0000009|P|2.5\n' +
        'EVN|A43|200701051530\n' +
        'PID|1|E2|new^^^&1.2.3.4&ISO~source^^^&1.2.3.4&ISO|||EVERYWOMAN^EVE|\n' +
        'MRG|old^^^&1.2.3.4&ISO|||E1\n'


    static void main(args) {
        init(CONTEXT_DESCRIPTOR, true)
    }

    @BeforeClass
    static void setUpClass() {
        init(CONTEXT_DESCRIPTOR, false)
    }


    // ITI-8 and ITI-64 can be dispatched, ITI-9 must fail
    @Test
    void testHappyCaseAndAudit1() {
        DefaultExchange exchange = new DefaultExchange(camelContext);

        exchange.in.body = send('pix-iti8://localhost:18500', ITI_8_REQUEST)
        iti8ResponseValidator().process(exchange)
        assertACK(exchange.in.body)

        exchange.in.body = send('pix-iti9://localhost:18500', ITI_9_REQUEST)
        iti9ResponseValidator().process(exchange)
        assertNAK(exchange.in.body)

        exchange.in.body = send('xpid-iti64://localhost:18500', ITI_64_REQUEST)
        iti64ResponseValidator().process(exchange)
        assertACK(exchange.in.body)

        // ITI-8 from server
        // ITI-8 from client
        // ITI-9 from client (failure)
        // ITI-64 from server
        // ITI-64 from client
        assertEquals(5, auditSender.messages.size())

        assertEquals(2, auditSender.messages.findAll {
            it.auditMessage.eventIdentification.eventTypeCode[0].code == 'ITI-8' &&
            it.auditMessage.eventIdentification.eventOutcomeIndicator == 0}
        .size())

        assertEquals(1, auditSender.messages.findAll {
            it.auditMessage.eventIdentification.eventTypeCode[0].code == 'ITI-9' &&
            it.auditMessage.eventIdentification.eventOutcomeIndicator == 12}
        .size())

        assertEquals(2, auditSender.messages.findAll {
            it.auditMessage.eventIdentification.eventTypeCode[0].code == 'ITI-64' &&
            it.auditMessage.eventIdentification.eventOutcomeIndicator == 0}
        .size())
    }

}
