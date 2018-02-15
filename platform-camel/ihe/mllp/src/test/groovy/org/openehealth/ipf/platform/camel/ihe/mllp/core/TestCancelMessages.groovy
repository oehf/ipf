/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core

import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.Parser
import ca.uhn.hl7v2.parser.PipeParser
import org.junit.Test

import static org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerMarshalAndInteractiveResponseReceiverInterceptor.createCancelMessage

/**
 * Tests for creation of automatic HL7v2 cancel messages.
 * @author Dmytro Rud
 */
class TestCancelMessages {

    private static String request(String version) {
        return 'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|' +
               'XYZ_HOSPITAL|||QBP^Q22|11311110|P|' + version + '||||||||\r' +
               'QPD|IHE PDQ Query|QRY11325110|@PID.3.1^PDQ113XX02|||||' +
               '^^^HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO\r' +
               'RCP|I|10^RD|||||\r'
    }

    /**
     * Test for QCN^J01 cancel messages -- HL7v2 version 2.4 and above.
     */
    @Test
    void testJ01() {
        Parser p = new PipeParser()
        Message request = p.parse(request('2.5'))
        String cancelString = createCancelMessage(request, p)
        Message cancel = p.parse(cancelString)

        [1, 2, 3, 4, 5, 6].each { i ->
            assert cancel.MSH[i].value == request.MSH[i].value
        }
        assert cancel.MSH[9][1].value == 'QCN'
        assert cancel.MSH[9][2].value == 'J01'
        assert cancel.MSH[9][3].value == 'QCN_J01'
        assert cancel.MSH[11].value == 'P'
        assert cancel.MSH[12].value == '2.5'

        assert cancel.QID[1].value == 'QRY11325110'
        assert cancel.QID[2].value == 'IHE PDQ Query'
    }

    /**
     * Test for xxx^CNQ cancel messages -- HL7v2 version below 2.4.
     */
    @Test
    void testCNQ() {
        Parser p = new PipeParser()
        Message request = p.parse(request('2.3.1'))
        String cancelString = createCancelMessage(request, p)
        Message cancel = p.parse(cancelString)

        assert cancel.MSH[9][1].value == 'QBP'
        assert cancel.MSH[9][2].value == 'CNQ'
        assert ! cancel.MSH[9][3].value
        assert cancel.MSH[11].value == 'P'
        assert cancel.MSH[12].value == '2.3.1'
    }
}
