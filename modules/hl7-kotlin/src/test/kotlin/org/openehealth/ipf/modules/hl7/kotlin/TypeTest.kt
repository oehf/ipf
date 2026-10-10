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
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Structure
import ca.uhn.hl7v2.model.v24.datatype.SI
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


/**
 * @author Christian Ohr
 */
class TypeTest {

    val context = DefaultHapiContext()
    val msg: Message = loadHl7(context, "/msg-02.hl7")!!
    private val obx1 = msg["PATIENT_RESULT"]["ORDER_OBSERVATION"]["OBSERVATION"]["OBX"]
    private val obx2 = msg["PATIENT_RESULT"]["ORDER_OBSERVATION"](1)["OBSERVATION"]["OBX"]
    private val obx3 = msg["PATIENT_RESULT"]["ORDER_OBSERVATION"](1)["OBSERVATION"](1)["OBX"]

    @Test
    fun testIsEmptyOBX1() {
        assertFieldEquals("1", obx1, 1)
        assertFieldEquals("NM", obx1, 2)
        assertFieldEquals("25026500^CREATININE, RANDOM URINE^^25026500^CREATININE, RANDOM URINE", obx1, 3)
        assertFieldsEmpty(obx1, 4)

        //should be [100~200~300] in message
        assertEquals("100", obx1[5](0).value)
        assertEquals("200", obx1[5](1).value)
        assertEquals("300", obx1[5](2).value)
        assertFalse(obx1[5].empty)

        assertFieldEquals("MG/DL", obx1, 6)
        assertFieldEquals("20-370", obx1, 7)
        assertFieldEquals("N", obx1, 8)
        assertFieldsEmpty(obx1, 9, 10)
        assertFieldEquals("F", obx1, 11)
        assertFieldsEmpty(obx1, 12, 13)
        assertFieldEquals("20050914130800", obx1, 14)
        assertFieldEquals("HL^^L", obx1, 15)
        assertFieldsEmpty(obx1, 16, 17)
    }

    @Test
    fun testIsEmptyOBX2() {
        assertFieldEquals("1", obx2, 1)
        assertFieldEquals("NM", obx2, 2)
        assertFieldEquals("50060710^MICROALBUMIN^^50060710^MICROALBUMIN", obx2, 3)
        assertFieldsEmpty(obx3, 4)

        val field = obx2[5]
        assertEquals("400", field(0).value)
        assertFalse(field.empty)

        assertFieldEquals("MG/DL", obx2, 6)
        assertFieldsEmpty(obx2, 7, 8, 9, 10)
        assertFieldEquals("F", obx2, 11)
        assertFieldsEmpty(obx2, 12, 13)
        assertFieldEquals("20050914130800", obx2, 14)
        assertFieldEquals("HL^^L", obx2, 15)
        assertFieldsEmpty(obx2, 16, 17)
    }

    @Test
    fun testMakePrimitive() {
        val si = newPrimitive("SI", msg, "1")
        assertTrue(si is SI)
        assertEquals("1", si.value)
    }

    private fun assertFieldEquals(expected: String, data: Structure, field: Int) {
        val simpleName = data::class.simpleName
        assertEquals(expected, data[field].encode())
        assertFalse(data[field].empty, "$simpleName[$field] must be not empty, but isEmpty() returns true")
    }

    private fun assertFieldsEmpty(data: Structure, vararg fields: Int) {
        val simpleName = data::class.simpleName
        for (field in fields) {
            assertTrue(data[field].empty, "$simpleName[$field] must be empty, but isEmpty() returns false")
        }
    }

    @Test
    fun testCopyRepeatableComposite() {
        val source: Message = makeHl7(context,
                "MSH|^~\\&|A|B|C|D|20050915174948||ADT^A01|1|P|2.5\r" +
                "PID|1||156154||Nachname^Vorname||19920426|F|||Irgendwo 25&Irgendwo&25^^Foo^^11111^DE~Street 2^^Bar\r")!!
        val target = newMessage(context, "ADT", "A01", "2.5")
        target["PID"][11] = source["PID"][11]
        assertEquals("Irgendwo 25&Irgendwo&25^^Foo^^11111^DE", target["PID"][11].encode())
    }

    @Test
    fun testCopyVariesComposite() {
        val target = newMessage(context, "ADT", "A01", "2.5")
        val obx = (target as Structure)["OBX"]
        obx.setObx5Type("CE")
        obx[5][1] = "T57000"
        obx[5][2] = "GALLBLADDER"
        val pid = target["PID"]
        pid[10] = obx[5]
        assertEquals("T57000^GALLBLADDER", pid[10].encode())
    }

    @Test
    fun testExtraComponents() {
        val m: Message = makeHl7(context, "MSH|^~\\&|A|B|C|D|20050915174948||ADT^A01|1|P|2.5\rEVN|A01|20050915^X^Y^Z\r")!!
        val extra = m["EVN"][2].extraComponents
        assertEquals("Y", extra.value)
        assertEquals("Y", extra[1].value)
        assertEquals("Z", extra[2].value)
    }

    @Test
    fun testPrimitiveSubscript() {
        val m: Message = loadHl7(context, "/msg-01.hl7")!!
        val messageType = m["MSH"][9][1]
        assertSame(messageType, messageType[1])
        val missing = messageType[2]
        assertTrue(missing is Null)
        assertNull(missing.value)
        assertNull(missing[3].value)
        assertEquals("x", missing.getValueOr("x"))
        assertTrue(missing.empty)
        assertThrows(Hl7DslException::class.java) { messageType[0] }
        assertThrows(Hl7DslException::class.java) { messageType[2] = "x" }
    }
}
