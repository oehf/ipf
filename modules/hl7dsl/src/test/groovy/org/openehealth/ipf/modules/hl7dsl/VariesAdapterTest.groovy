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
package org.openehealth.ipf.modules.hl7dsl

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.*

/**
 * @author Marek Václavik
 */
class VariesAdapterTest extends GroovyTestCase {

    def msg1
    def msg2

    void setUp() {
        msg1 = load('msg-05.hl7')
        msg2 = load('msg-02.hl7')
    }

    //TODO Organize tests by classes, not by usage;
    //TODO Move tests to SegmentAdadpterTest, CompositeAdapterTest etc. where apropriate

    void testVariesUsage() {
        assert msg1.QPD.target.getClass().getName().contains('ca.uhn.hl7v2.model.v25.segment.QPD')

        //Test field explicitly defined as Varies: OBX-5
        //-----------------------------------
        def v = msg2.PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBSERVATION(0).OBX[5]
        //Test a repeated Varies field
        //Primitive value can be accessed either as element or as subelement #1
        //Here QPD-3-1-1 returns the same as QPD-3-1
        assert (v(0).value ?: '')== '100'
        assert (v(0)[1].value ?: '') == '100'
        //No exception on accessing further (non-existing) subelements
        assert (v(0)[2].value ?: '') == ''

        //Test a field explicitly defined as Varies: PDQ-3
        //-----------------------------------
        //Access normally filled components
        assert (msg1.QPD[3][4][1]?.value ?: '') == 'HIMSS2006'
        assert (msg1.QPD[3][4][2]?.value ?: '') == '1.3.6.1.4.1.21367.2005.1.1'
        assert (msg1.QPD[3][4][3]?.value ?: '') == 'ISO'

        //Primitive value can be accessed either as element or as subelement #1
        //Here QPD-3-1-1 returns the same as QPD-3-1
        assert (msg1.QPD[3][1]?.value ?: '') == '123456'
        assert (msg1.QPD[3][1][1]?.value ?: '') == '123456'
        //No exception on accessing further (non-existing) subelements
        assert (msg1.QPD[3][1][2]?.value ?: '') == ''

        //Access empty Varies component
        assert (msg1.QPD[3][2]?.value ?: '') == ''
        //Access subcomponent 1 of an empty field
        assert (msg1.QPD[3][2][1]?.value ?: '') == ''

        //Test undefined field => field implicitly defined as Varies: PDQ-4
        //-----------------------------------
        //Test repetition
        assert  (msg1.QPD[4](0)[1]?.value ?: '') == ''

        //Primitive value can be accessed either as element or as subelement 1
        //Here QPD-4(0)-4 returns the same as QPD-4(0)-4-1
        assert (msg1.QPD[4](0)[4]?.value ?: '') == 'HIMSS2006'
        assert (msg1.QPD[4](0)[4][1]?.value ?: '') == 'HIMSS2006'
        //No exception on accessing further (non-existing) subelements
        assert (msg1.QPD[4](0)[4][2]?.value ?: '') == ''

        //Access non existing repetition
        assert  (msg1.QPD[4](1)[1]?.value ?: '')  == ''
        //Access non existing component of a non existing repetition
        assert  (msg1.QPD[4](3)[4]?.value ?: '') == ''
    }
}