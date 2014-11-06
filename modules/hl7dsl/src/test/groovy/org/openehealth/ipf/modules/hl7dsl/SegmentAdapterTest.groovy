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

import org.junit.Before
import org.junit.Test

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.*
import ca.uhn.hl7v2.parser.EncodingCharacters

/**
 * @author Martin Krasser
 */
class SegmentAdapterTest extends groovy.test.GroovyAssert {
    
    def nk1
    def nk1_all
    def msh
    def evn
    def pid
	def pv1
	def pv2

    @Before
    void setUp() {
        def msg1 = load('msg-01.hl7')
        def msg2 = load('msg-03.hl7')
        
        nk1 = msg1.NK1(0)
        nk1_all = msg1.NK1
        msh = msg1.MSH
        evn = msg2.EVN
        pid = msg2.PID
		pv1 = msg1.PV1
		pv2 = msg1.PV2
    }

    @Test
    void testInvokeMethod() {
        // invoke method on adapter
        assert nk1.count(5) == 2
        
        // invoke method on target
        assert nk1.numFields() == 13
    }

    @Test
    void testGet() {
        // property access on target
        assert nk1.name == 'NK1'
    }

    @Test
    void testSet() {
        def msg = msh.message.copy()
        msh[3] = 'SAP-XXX'
        assert msg.MSH[3].value == 'SAP-ISH'
        msg.MSH = msh
        assert msg.MSH[3].value == 'SAP-XXX'
    }


    @Test
    void testPrimitiveGetAt() {
        
        // field with value
        assert msh[3] instanceof PrimitiveAdapter
        assert msh[3].value == 'SAP-ISH'

        // field without value
        assert msh[6] instanceof PrimitiveAdapter
        assert !msh[6].value
    }

    @Test
    void testCompositeGetAt() {
        assert nk1[4] instanceof CompositeAdapter
        assert nk1[4][4] instanceof PrimitiveAdapter
        assert nk1[4][4].value == 'NW'
        assert nk1[4][2] instanceof PrimitiveAdapter
        assert !nk1[4][2].value
    }

    @Test
    void testRepetitionGetAt() {
        assert nk1.count(5) == 2
        assert nk1[5](0).value == '333-4444'
        assert nk1[5](1).value == '333-5555'
        assert nk1[5]()*.value == ['333-4444', '333-5555']
    }

    @Test
    void testPrimitiveSetAt() {
        assert msh[5] instanceof PrimitiveAdapter
        assert !msh[5].value
        // assign value from string
        msh[5] = 'abc'
        assert msh[5] instanceof PrimitiveAdapter
        assert msh[5].value == 'abc'
        
        // assign value from primitive adapter
        msh[5] = nk1[4][4]
        assert msh[5] instanceof PrimitiveAdapter
        assert msh[5].value == 'NW'
    }

    @Test
    void testCompositeSetAt() {
        def nk1Copy = nk1.message.copy().NK1(0)
        nk1Copy[4][4].value = 'XY'
        assert nk1[4][4].value == 'NW'
        nk1[4] = nk1Copy[4]
        assert nk1[4][4].value == 'XY'
    }

    @Test
    void testComponentSetAt() {
        nk1[4][2] = 'blah'
        assert nk1[4][2].value == 'blah'
    }

    @Test
    void testCompositeCreate() {
        assert !evn[7][1].value // implicit creation via evn[7]
        assert !evn[7][2].value
        evn[7][1] = 'X'
        evn[7][2] = 'Y'
        assert evn[7][1].value == 'X'
        assert evn[7][2].value == 'Y'
    }

    @Test
    void testGetTarget() {
        assert nk1.target instanceof ca.uhn.hl7v2.model.Segment
    }

    @Test
    void testNrp() {
        nk1.nrp(5).value = '333-6666'
        assert nk1.count(5) == 3
        assert nk1[5](0).value == '333-4444'
        assert nk1[5](1).value == '333-5555'
        assert nk1[5](2).value == '333-6666'
    }

    @Test
    void testZeroCardinality() {
        assert pid.getMaxCardinality(5) == 0
        assert pid[5](0)[1][2] instanceof PrimitiveAdapter
        assert !pid[5](0)[1][2].value
        pid[5](0)[1][2] = 'van'
        assert pid[5](0)[1][2].value == 'van'
    }

    @Test
	void testIsEmpty() {
		assert pv2.isEmpty() == true
		assert pv1.isEmpty() == false
	}

    @Test
    void testSettingObx5Type() {
        // create a new OBX segment from scratch
        MessageAdapter msg = load('msg-02.hl7')
        SelectorClosure observations = msg.PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBSERVATION
        SegmentAdapter obx = observations(observations().size()).OBX

        VariesAdapter obx5 = obx[5](0)

        // before OBX-5 type has been set, it should be impossible
        // to set this field using IPF DSL
        boolean failed = false
        try {
            obx5[1] = 'T57000'
            obx5[2] = 'GALLBLADDER'
            obx5[3] = 'SNM'
        } catch (Exception e) {
            failed = true
        }
        assert failed

        // set OBX-5 type and prove that this filed is now accessible in default manner
        obx.setObx5Type('CE')
        obx5[1] = 'T57000'
        obx5[2] = 'GALLBLADDER'
        obx5[3] = 'SNM'

        String obxString = msg.parser.doEncode(obx.target, new EncodingCharacters('|' as char, '^~\\&'))
        assert obxString == 'OBX||CE|||T57000^GALLBLADDER^SNM'
    }
}