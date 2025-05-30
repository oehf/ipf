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

import ca.uhn.hl7v2.model.v24.message.ORU_R01
import ca.uhn.hl7v2.model.v25.message.QBP_Q21
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.openehealth.ipf.modules.hl7.dsl.TestUtils.*

/**
 * @author Marek Václavik
 */
class VariesTest {

    QBP_Q21 msg1
    ORU_R01 msg2

    @BeforeEach
    void setUp() {
        msg1 = load('dsl/msg-05.hl7')
        msg2 = load('dsl/msg-02.hl7')
    }

    @Test
    void testVariesUsage() {
        assert msg1.QPD.class.name.contains('ca.uhn.hl7v2.model.v25.segment.QPD')

        //Test field explicitly defined as Varies: OBX-5
        //-----------------------------------
        def v = msg2.PATIENT_RESULT.ORDER_OBSERVATION.OBSERVATION.OBX[5]
        //Test a repeated Varies field
        //Primitive value can be accessed either as element or as subelement #1
        //Here QPD-3-1-1 returns the same as QPD-3-1
        assert v(0).value == '100'
        def x = v(0)[1].value
        assert x == '100'
        //No exception on accessing further (non-existing) subelements
        assert v(0)[2].getValueOr('') == ''

        //Test a field explicitly defined as Varies: PDQ-3
        //-----------------------------------
        //Access normally filled components
        assert msg1.QPD[3][4][1]?.value == 'HIMSS2006'
        assert msg1.QPD[3][4][2]?.value == '1.3.6.1.4.1.21367.2005.1.1'
        assert msg1.QPD[3][4][3]?.value == 'ISO'

        //Primitive value can be accessed either as element or as subelement #1
        //Here QPD-3-1-1 returns the same as QPD-3-1
        assert msg1.QPD[3][1]?.value == '123456'
        assert msg1.QPD[3][1][1]?.value == '123456'
        //No exception on accessing further (non-existing) subelements
        assert msg1.QPD[3][1][2]?.value == null

        //Access empty Varies component
        assert msg1.QPD[3][2]?.value == null
        //Access subcomponent 1 of an empty field
        assert msg1.QPD[3][2][1]?.value == null

        //Test undefined field => field implicitly defined as Varies: PDQ-4
        //-----------------------------------
        //Test repetition
        assert  msg1.QPD[4](0)[1]?.value == null

        //Primitive value can be accessed either as element or as subelement 1
        //Here QPD-4(0)-4 returns the same as QPD-4(0)-4-1
        assert msg1.QPD[4](0)[4]?.value == 'HIMSS2006'
        assert msg1.QPD[4](0)[4][1]?.value == 'HIMSS2006'
        //No exception on accessing further (non-existing) subelements
        assert msg1.QPD[4](0)[4][2]?.value == null

        //Access non existing repetition
        assert  msg1.QPD[4](1)[1]?.value == null
        //Access non existing component of a non existing repetition
        assert  msg1.QPD[4](3)[4]?.value == null
    }
}