/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.dsl

import ca.uhn.hl7v2.model.v22.message.ADT_A01
import ca.uhn.hl7v2.model.v24.message.ORU_R01
import ca.uhn.hl7v2.model.v25.message.MDM_T01
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import static org.openehealth.ipf.modules.hl7.dsl.TestUtils.load
import static org.openehealth.ipf.modules.hl7.dsl.TestUtils.make

/**
 * @author Martin Krasser
 */
class MessageTest extends groovy.test.GroovyAssert {

    ADT_A01 msg1
    ORU_R01 msg2
    MDM_T01 msg3
    ca.uhn.hl7v2.model.v251.message.ORU_R01 msg4

    @Before
    void setUp() {
        msg1 = load('dsl/msg-01.hl7')
        msg2 = load('dsl/msg-04.hl7')
        msg3 = load('dsl/msg-03.hl7')
        msg4 = load('dsl/msg-08.hl7')
    }

    @Test
    void testMakeStream() {
        def stream = getClass().classLoader.getResource('msg-01.hl7').openStream()
        ADT_A01 result = make(stream)
		
        assert result.MSH[4].value == 'HZL'
    }

    @Test
    void testRender() {
        def writer
        def result
		
        writer = new StringWriter()
        writer << msg1 // msg is writable
        result = writer.buffer.toString()
        assert result.contains('4444')
        assert !result.contains('6666')

        // modify message
        msg1.NK1(0).nrp(5).value = '333-6666'
        
        writer = new StringWriter()
        writer << msg1 // msg is writable
        result = writer.buffer.toString()
        assert result.contains('4444')
        assert result.contains('6666')
        
        result = msg1.toString()
        assert result.contains('4444')
        assert result.contains('6666')
    }

    @Test
    void testCopy() {
		msg1.MSH[5] = 'X'
        ADT_A01 copy = msg1.copy()
        assert copy.MSH[5].value == 'X'
        copy.MSH[5].value = 'Y'
        assert copy.MSH[5].value == 'Y'
        assert msg1.MSH[5].value == 'X'
    }

    @Test
    void testCopyMessage1() {
        //def msg1Copy = msg1.empty()
        //Messages.copyMessage(msg1, msg1Copy)
        def msg1Copy = MessageUtils.copy(msg1)
        assert msg1.toString() == msg1Copy.toString()
    }

    @Test
    void testCopyMessage2() {
        def msg2Copy = MessageUtils.copy(msg2)
        assert msg2.toString() == msg2Copy.toString()
    }

    @Test
    void testCopyMessageWithNonStandardSegments1() {
        def msg3Copy = MessageUtils.copy(msg3)
        assert msg3.toString() == msg3Copy.toString()
    }

    @Test
    void testCopyMessageWithNonStandardSegments2() {
        def msg4Copy = MessageUtils.copy(msg4)
        assert msg4.toString() == msg4Copy.toString()
    }
}