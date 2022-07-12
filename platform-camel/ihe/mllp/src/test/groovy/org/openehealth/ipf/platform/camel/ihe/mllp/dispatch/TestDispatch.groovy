/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.dispatch

import org.apache.camel.support.DefaultExchange
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AbstractMllpTest

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.openehealth.ipf.platform.camel.hl7.HL7v2.validatingProcessor

/**
 * @author Dmytro Rud
 */
abstract class TestDispatch extends AbstractMllpTest {

    static final String ITI_8_REQUEST =
        'MSH|^~\\&|MESA_PD_SUPPLIER|XYZ_HOSPITAL|dummy|dummy|20081204114742||ADT^A01|123456|T|2.3.1|||ER\n' +
        'EVN|A01|20081204114742\n' +
        'PID|1||001^^^XREF2005~002^^^HIMSS2005||Multiple^Christof^Maria^Prof.^^^L||' +
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
        'PID|1||new^^^&1.2.3.4&ISO~source^^^&1.2.3.4&ISO||EVERYWOMAN^EVE^^^^^L|\n' +
        'MRG|old^^^&1.2.3.4&ISO|||\n'


    // ITI-8 and ITI-64 can be dispatched, ITI-9 must fail
    @Test
    void testHappyCaseAndAudit1() {
        DefaultExchange exchange = new DefaultExchange(camelContext)

        exchange.in.body = send('pix-iti8://localhost:' + getDispatcherPort(), ITI_8_REQUEST)
        validatingProcessor().process(exchange)
        assertACK(exchange.in.body)

        exchange.in.body = send('pix-iti9://localhost:' + getDispatcherPort(), ITI_9_REQUEST)
        // This does not pass response validation!!
        // iti9ResponseValidator().process(exchange)
        assertNAK(exchange.in.body)

        exchange.in.body = send('xpid-iti64://localhost:' + getDispatcherPort(), ITI_64_REQUEST)
        validatingProcessor().process(exchange)
        assertACK(exchange.in.body)

        // ITI-8 from server
        // ITI-8 from client
        // ITI-9 from client (failure)
        // ITI-64 from server
        // ITI-64 from client
        assertAuditEvents { it.messages.size() == 5 }

        assertAuditEvents {
            it.messages.count {
                it.eventIdentification.eventTypeCode[0].code == 'ITI-8' &&
                it.eventIdentification.eventOutcomeIndicator == EventOutcomeIndicator.Success
            } == 2
        }

        assertAuditEvents {
            it.messages.count {
                it.eventIdentification.eventTypeCode[0].code == 'ITI-9' &&
                it.eventIdentification.eventOutcomeIndicator == EventOutcomeIndicator.MajorFailure
            } == 1
        }

        assertAuditEvents {
            it.messages.count {
                it.eventIdentification.eventTypeCode[0].code == 'ITI-64' &&
                it.eventIdentification.eventOutcomeIndicator == EventOutcomeIndicator.Success
            } == 2
        }

    }

    protected abstract String getDispatcherPort()

}
