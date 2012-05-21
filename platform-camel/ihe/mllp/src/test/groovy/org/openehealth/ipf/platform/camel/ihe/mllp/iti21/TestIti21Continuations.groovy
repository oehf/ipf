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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti21

import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.EhcacheInteractiveConfigurationStorage
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTestContainer
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase

/**
 * Tests for HL7 continuations, see Section 2.10.2 of the HL7 v.2.5 specification.
 * @author Dmytro Rud
 */
class TestIti21Continuations extends MllpTestContainer {
    
    def static CONTEXT_DESCRIPTOR = 'iti21/iti-21-continuations.xml'
    
    final String REQUEST_MESSAGE =
        'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|' +
        '20081031112704||QBP^Q22|324406609|P|2.5|||ER\r' +
        'SFT|XON|ST|ST|ST|TX|TS\r' +
        'QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~' +
        '@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\r' +
        'RCP|I|1^RD|||||\n'
    
    static void main(args) {
        PayloadLoggerBase.setGloballyEnabled(false)
        init(CONTEXT_DESCRIPTOR, true)
    }
    
    @BeforeClass
    static void setUpClass() {
        PayloadLoggerBase.setGloballyEnabled(false)
        init(CONTEXT_DESCRIPTOR, false)
    }
    
    static String endpointUri(
        int port,
        boolean supportInteractiveContinuation,
        boolean supportUnsolicitedFragmentation,
        boolean supportSegmentFragmentation,
        boolean autoCancel,
        boolean isServerSide)
    {
        return "pdq-iti21://localhost:${port}?timeout=30000000000" +
            "&supportInteractiveContinuation=${supportInteractiveContinuation}" +
            "&interactiveContinuationStorage=#interactiveContinuationStorage" +
            "&supportUnsolicitedFragmentation=${supportUnsolicitedFragmentation}" +
            "&unsolicitedFragmentationThreshold=3" +
            "&unsolicitedFragmentationStorage=#unsolicitedFragmentationStorage" +
            "&supportSegmentFragmentation=${supportSegmentFragmentation}" +
            "&autoCancel=${autoCancel}" +
            "&interceptors=#${isServerSide ? 'server' : 'client'}InLogger," +
                          "#${isServerSide ? 'server' : 'client'}OutLogger"
    }
    
    
    @Test
    void testHappyCaseAndAudit() {
        def msg = send(endpointUri(28210, true, true, true, true, false), REQUEST_MESSAGE)
        assert 4 == msg.QUERY_RESPONSE().size()
        assert 2 == auditSender.messages.size()
        assert '4' == msg.QAK[4].value
        assert '4' == msg.QAK[5].value
        assert '0' == msg.QAK[6].value
        
        // check whether "autoCancel" parameter works
        EhcacheInteractiveConfigurationStorage storage = appContext.getBean('interactiveContinuationStorage')
        assert storage.ehcache.size == 0
    }
    
    @Test
    void testInteractiveAssembly() {
        def msg = send(endpointUri(28211, true, false, false, false, false), REQUEST_MESSAGE)
        assert 4 == msg.QUERY_RESPONSE().size()
        assert 2 == auditSender.messages.size()
        assert '4' == msg.QAK[4].value
        assert '4' == msg.QAK[5].value
        assert '0' == msg.QAK[6].value
    }
}
