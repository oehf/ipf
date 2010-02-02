/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.pdq.iti21;


import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.parser.PipeParser
import org.apache.camel.CamelExchangeException
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.RuntimeCamelException
import org.apache.camel.impl.DefaultExchange
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.mllp.core.HandshakeCallbackSSLFilter
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerMarshalInterceptor
import org.openhealthtools.ihe.atna.auditor.events.dicom.SecurityAlertEvent
import static org.junit.Assert.*

/**
 * Tests for HL7 continuations, see § 2.10.2 of the HL7 v.2.5 specification.
 * @author Dmytro Rud
 */
class TestContinuations extends MllpTestContainer {
   
    final String REQUEST_MESSAGE =
        'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|' + 
            '20081031112704||QBP^Q22|324406609|P|2.5|||ER\r' +
        'SFT|XON|ST|ST|ST|TX|TS\r' + 
        'QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~' + 
            '@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\r' +
        'RCP|I|1^RD|||||\n'
     
    @BeforeClass
    static void setUpClass() {
        init('iti-21-continuations.xml')
    }
    
    @Test
    void testHappyCaseAndAudit1() {
        def msg = send(
                'pdq-iti21://localhost:9001?timeout=30000000000' + 
                '&supportInteractiveContinuation=true' + 
                '&supportUnsolicitedFragmentation=true&unsolicitedFragmentationThreshold=3' + 
                '&supportSegmentFragmentation=true',
                REQUEST_MESSAGE)
        assert 4 == msg.QUERY_RESPONSE().size()
        assert 2 == auditSender.messages.size
        assert '4' == msg.QAK[4].value
        assert '4' == msg.QAK[5].value
        assert '0' == msg.QAK[6].value
        
    }

}
