/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.kotlin

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.v22.message.ADT_A01
import ca.uhn.hl7v2.model.v22.segment.NK1
import ca.uhn.hl7v2.parser.EncodingCharacters
import org.junit.Assert.*
import org.junit.Test

/**
 * @author Christian Ohr
 */
class SegmentTest {

    val context = DefaultHapiContext()
    private val msg1: ADT_A01 = loadHl7(context, "/msg-01.hl7")
    private val msg2: Message = loadHl7(context, "/msg-02.hl7")
    private val msg3: Message = loadHl7(context, "/msg-03.hl7")
    private val nk1 = msg1.nK1!!
    val msh = msg1.msh!!
    private val evn = msg3["EVN"] as Segment
    val pid = msg3["PID"] as Segment
    private val pv1 = msg1.pV1!!
    private val pv2 = msg1.pV2!!

    @Test
    fun testInvokeMethod() {
        // invoke method on adapter
        assertEquals(2, nk1.count(5))

        // invoke method on target
        assertEquals(13, nk1.numFields())
    }

    @Test
    fun testGet() {
        // property access on target
        assertEquals("NK1", nk1.name)
    }

    @Test
    fun testSet() {
        val msg = msg1.copy()
        msh[3] = "SAP-XXX"
        assertEquals("SAP-ISH", msg.msh[3].value)
        msg.msh.from(msh)
        assertEquals("SAP-XXX", msg.msh[3].value)
        msg.msh[3] = "SAP-ISH"
        msg["MSH"] = msh
        assertEquals("SAP-XXX", msg.msh[3].value)
    }

    @Test
    fun testSetRepetitiveSegment() {
        val msg = msg2.copy()
        val nte4 = msg2["PATIENT_RESULT"]["ORDER_OBSERVATION",1]["OBSERVATION",1]["NTE",4]
        val nte3: Segment = msg2["PATIENT_RESULT"]["ORDER_OBSERVATION",1]["OBSERVATION",1]["NTE",3] as Segment
        assertTrue(nte3[3].value!!.startsWith("MICROALBUMINURIA"))
        assertTrue(nte4[3].value!!.startsWith("CLINICAL ALBUMINURIA"))
        msg["PATIENT_RESULT"]["ORDER_OBSERVATION",1]["OBSERVATION",1]["NTE", 4] = nte3
        assertTrue(msg["PATIENT_RESULT"]["ORDER_OBSERVATION",1]["OBSERVATION",1]["NTE", 4][3].value!!.startsWith("MICROALBUMINURIA"))
    }

    @Test
    fun testPrimitiveGetAt() {

        // field with value
        assertTrue(msh[3] is Primitive)
        assertEquals("SAP-ISH", msh[3].value)

        // field without value
        assertTrue(msh[6] is Primitive)
        assertTrue(msh[6].empty)
    }

    @Test
    fun testSettingObx5Type() {
        // create a new OBX segment from scratch
        val obx = msg2["PATIENT_RESULT"]["ORDER_OBSERVATION"].nrp("OBSERVATION")["OBX"]

        // before OBX-5 type has been set, it should be impossible
        // to set this field using IPF DSL
        try {
            obx[5](0)[1] = "T57000"
            obx[5](0)[2] = "GALLBLADDER"
            obx[5](0)[3] = "SNM"
            fail()
        } catch (e: Exception) {
        }

        // set OBX-5 type and prove that this field is now accessible in default manner
        obx.setObx5Type("CE")
        obx[5](0)[1] = "T57000"
        obx[5](0)[2] = "GALLBLADDER"
        obx[5](0)[3] = "SNM"

        val obxString = msg2.parser.doEncode(obx as Segment, EncodingCharacters('|', "^~\\&"))
        assertEquals("OBX||CE|||T57000^GALLBLADDER^SNM", obxString)
    }

    @Test
    fun testCompositeGetAt() {
        assertTrue(nk1[4] is Composite)
        assertTrue(nk1[4][4] is Primitive)
        assertEquals("NW", nk1[4][4].value)
        assertTrue(nk1[4][2] is Primitive)
        assertTrue(nk1[4][2].empty)
        assertTrue(nk1[4][2].value == null)
    }

    @Test
    fun testRepetitionGetAt() {
        assertEquals(2, nk1.count(5))
        assertEquals("333-4444", nk1[5](0).value)
        assertEquals("333-4444", nk1[5, 0].value)
        assertEquals("333-5555", nk1[5](1).value)
        assertEquals("333-5555", nk1[5, 1].value)
        assertEquals("333-4444,333-5555", nk1[5]().joinToString(","))
    }

    @Test
    fun testPrimitiveSetAt() {
        assertTrue(msh[5] is Primitive)
        assertTrue(msh[5].empty)
        // assign value from string
        msh[5] = "abc"
        assertTrue(msh[5] is Primitive)
        assertEquals("abc", msh[5].value)

        // assign value from primitive adapter
        msh[5] = nk1[4][4]
        assertTrue(msh[5] is Primitive)
        assertEquals("NW", msh[5].value)
    }

    @Test
    fun testCompositeSetAt() {
        val nk1Copy = msg1.copy().getNK1(0)
        nk1Copy[4][4] = "XY"
        assertEquals("NW", nk1[4][4].value)
        nk1[4] = nk1Copy[4]
        assertEquals("XY", nk1[4][4].value)
    }

    @Test
    fun testComponentSetAt() {
        nk1[4][2] = "blah"
        assertEquals("blah", nk1[4][2].value)
    }

    @Test
    fun testCompositeCreate() {
        assertTrue(evn[7][1].empty)
        assertTrue(evn[7][2].empty)
        evn[7][1] = 'X'
        evn[7][2] = 'Y'
        assertEquals("X", evn[7][1].value)
        assertEquals("Y", evn[7][2].value)
    }

    @Test
    fun testNrp() {
        nk1.nrp(5).from("333-6666")
        assertEquals(3, nk1.count(5))
        assertEquals("333-4444", nk1[5](0).value)
        assertEquals("333-5555", nk1[5](1).value)
        assertEquals("333-6666", nk1[5](2).value)
    }

    @Test
    fun testZeroCardinality() {
        assertEquals(0, pid.getMaxCardinality(5))
        assertTrue(pid[5](0)[1][2] is Primitive)
        assertTrue(pid[5](0)[1][2].empty)
        pid[5](0)[1][2] = "van"
        assertEquals("van", pid[5](0)[1][2].value)
    }

    @Test
    fun testIsEmpty() {
        assertTrue(pv2.empty)
        assertFalse(pv1.empty)
    }


    @Test
    fun testMakeSegment() {
        val nk1 = newSegment("NK1", msg1)
        assertTrue(nk1 is NK1)
    }

}