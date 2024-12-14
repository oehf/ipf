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
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.Structure
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/**
 * @author Christian Ohr
 */
class GroupTest {

    val context = DefaultHapiContext()
    val message: Message = loadHl7(context, "/msg-02.hl7")

    @Test
    fun testInvokeMethod() {
        // invoke method on adapter
        assertEquals(1, message.count("MSH"))
        // invoke method on target
        assertTrue(message.isRequired("MSH"))
    }

    @Test
    fun testGet() {
        // property access on target
        assertEquals("2.4", message.version)
    }

    @Test
    fun testGetAt() {
        assertTrue(message["MSH"] is Segment)
        assertTrue(message["PATIENT_RESULT"](0) is Group)
        assertTrue(message["PATIENT_RESULT", 0] is Group)
        assertTrue(message["PATIENT_RESULT"]["PATIENT"]["PID"] is Segment)

        // alternative notation does not exist in Kotlin
        // assert message['MSH'] == message.MSH
        // assert message['PATIENT_RESULT'] == message.PATIENT_RESULT
        // assert message['PATIENT_RESULT']() instanceof Repeatable
        // assert message['PATIENT_RESULT'](0) instanceof Group
        // assert message['PATIENT_RESULT']['PATIENT']['PID'] == message.PATIENT_RESULT.PATIENT.PID
        // assert message['PATIENT_RESULT'](0)['PATIENT']['PID'] instanceof Segment
    }

    @Test
    fun testPath() {
        assertEquals(null, message.path)

        val pr0 = message["PATIENT_RESULT", 0]

        assertEquals("PATIENT_RESULT(0)",
                pr0.path)
        assertEquals("PATIENT_RESULT(0).PATIENT.PID",
                pr0["PATIENT"]["PID"].path)
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(0)",
                pr0["ORDER_OBSERVATION", 0].path)
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBR",
                pr0["ORDER_OBSERVATION"]["OBR"].path)
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBSERVATION(0)",
                pr0["ORDER_OBSERVATION"]["OBSERVATION", 0].path)
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBSERVATION(0)",
                pr0["ORDER_OBSERVATION"]["OBSERVATION"](0).path)

        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBSERVATION(0).OBX",
                pr0["ORDER_OBSERVATION"]["OBSERVATION"](0)["OBX"].path)
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBSERVATION(0).OBX",
                pr0["ORDER_OBSERVATION"](0)["OBSERVATION"](0)["OBX"].path)

        assertEquals(2, pr0["ORDER_OBSERVATION"](1)["OBSERVATION"]().size)
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(1).OBSERVATION(0).OBX",
                pr0["ORDER_OBSERVATION"](1)["OBSERVATION"](0)["OBX"].path)
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(1).OBSERVATION(1).OBX",
                pr0["ORDER_OBSERVATION"](1)["OBSERVATION"](1)["OBX"].path)

        //OBSERVATION(2) is created even though it is not in the message.
        //This is a feature of the DSL, see call implementation
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(1).OBSERVATION(2).OBX",
                pr0["ORDER_OBSERVATION"](1)["OBSERVATION"](2)["OBX"].path)
    }

    @Test
    fun testCount() {
        assertEquals(2, message["PATIENT_RESULT"](0).count("ORDER_OBSERVATION"))
    }


    @Test
    fun testFind() {
        val obx = message.asIterable().find { it.name == "OBX" }!!
        assertEquals("25026500", obx[3][1].value)
    }

    @Test
    fun testFindAll() {
        val obxs = message.asIterable().filter { it.name == "OBX" }
        assertEquals(3, obxs.size)
    }

    @Test
    fun testFindAndCount() {
        val obxs = message.asIterable().count { it.name == "OBX" }
        assertEquals(3, obxs)
    }

    @Test
    fun testFindIndexOf() {
        val index = message.findIndexOf { it.name == "OBX" }
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBSERVATION(0).OBX", index)
    }

    @Test
    fun testFindLastIndexOf() {
        val index = message.findLastIndexOf { it.name == "OBX" }
        assertEquals("PATIENT_RESULT(0).ORDER_OBSERVATION(1).OBSERVATION(1).OBX", index)
    }

    @Test
    fun testFindIndexValues() {
        val indexes = message.findIndexValues { it.name == "OBX" }
        assertEquals(3, indexes.size)

        assertTrue(
                with(indexes) {
                    contains("PATIENT_RESULT(0).ORDER_OBSERVATION(0).OBSERVATION(0).OBX") &&
                            contains("PATIENT_RESULT(0).ORDER_OBSERVATION(1).OBSERVATION(0).OBX") &&
                            contains("PATIENT_RESULT(0).ORDER_OBSERVATION(1).OBSERVATION(1).OBX")
                }
        )
    }

    @Test
    fun testSplit() {
        val (segments, groups) = message.asIterable().partition { it is Segment }
        assertEquals(27, segments.size)
        assertEquals(7, groups.size)
    }

    @Test
    fun testEach() {
        var numberOfStructures = 0
        message.asIterable().forEach { numberOfStructures++ }
        assertEquals(34, numberOfStructures)
    }

    @Test
    fun testMap() {
        val names = message.asIterable().map { it.name }
        assertEquals(34, names.size)
    }

    @Test
    fun testEachWithIndex() {
        val found = mutableListOf<String>()
        message.eachWithIndex { _, index -> found += index }
        assertTrue(found.contains("PATIENT_RESULT(0).ORDER_OBSERVATION(1).OBSERVATION(1).NTE(17)"))
        assertTrue(found.contains("PATIENT_RESULT(0).PATIENT.PID"))
        assertTrue(found.contains("MSH"))
    }

    @Test
    fun testAll() {
        assertFalse(message.asIterable().all { it is Segment })
    }

    @Test
    fun testAny() {
        assertTrue(message.asIterable().any { it is Group })
    }

    @Test
    fun testFor() {
        var numberOfStructures = 0
        for (structure in message) {
            numberOfStructures++
        }
        assertEquals(34, numberOfStructures)
    }

    @Test
    fun testNrp() {
        val nteCount = observation(message).count("NTE")
        assertTrue(observation(message).nrp("NTE") is Segment)
        assertEquals(nteCount + 1, observation(message).count("NTE"))
    }

    @Test
    fun testIsEmpty() {
        assertFalse(message["PATIENT_RESULT"].empty)
        assertTrue(message["PATIENT_RESULT"]["PATIENT"]["VISIT"].empty)
    }

    @Test
    fun testAccessNonStandardStructure() {
        val znt = message.addNonstandardSegment("ZNT")
        val znt2 = message.addNonstandardSegment("ZNT")
        val zntSegment: Segment = message.get(znt) as Segment
        val znt2Segment: Segment = message.get(znt2) as Segment

        assertEquals(zntSegment, message["ZNT"])
        assertEquals(znt2Segment, message["ZNT2"])
    }

    private fun observation(message: Message): Structure = message["PATIENT_RESULT"](0)["ORDER_OBSERVATION"](1)["OBSERVATION"](1)

}