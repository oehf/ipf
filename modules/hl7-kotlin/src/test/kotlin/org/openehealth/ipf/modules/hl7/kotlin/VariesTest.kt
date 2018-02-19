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
import org.junit.Assert.*
import org.junit.Test

/**
 * @author Christian Ohr
 */
class VariesTest {

    val context = DefaultHapiContext()
    val msg1: Message = loadHl7(context, "/msg-05.hl7")
    val msg2: Message = loadHl7(context, "/msg-02.hl7")

    @Test
    fun testVariesUsage() {
        assertTrue(msg1["QPD"]::class.java.name.contains("ca.uhn.hl7v2.model.v25.segment.QPD"))

        //Test field explicitly defined as Varies: OBX-5
        //-----------------------------------
        val v = msg2["PATIENT_RESULT"]["ORDER_OBSERVATION"]["OBSERVATION"]["OBX"][5]

        //Test a repeated Varies field
        //Primitive value can be accessed either as element or as subelement #1
        //Here QPD-3-1-1 returns the same as QPD-3-1
        assertEquals("100", v(0).value)
        val x = v(0)[1].value
        assertEquals("100", x)

        // Index out of bounds on accessing further (non-existing) subelements
        // assertEquals("", v(0)[2].getValueOr(""))

        //Test a field explicitly defined as Varies: PDQ-3
        //-----------------------------------
        //Access normally filled components
        assertEquals("HIMSS2006", msg1["QPD"][3][4][1].value)
        assertEquals("1.3.6.1.4.1.21367.2005.1.1", msg1["QPD"][3][4][2].value)
        assertEquals("ISO", msg1["QPD"][3][4][3].value)

        //Primitive value can be accessed either as element or as subelement #1
        //Here QPD-3-1-1 returns the same as QPD-3-1
        assertEquals("123456", msg1["QPD"][3][1].value)
        assertEquals("123456", msg1["QPD"][3][1][1].value)

        // Index out of bounds on accessing further (non-existing) subelements
        // assertNull(msg1["QPD"][3][1][2].value)

        //Access empty Varies component
        assertNull(msg1["QPD"][3][2].value)
        assertNull(msg1["QPD"][3][2][1].value)

        //Test undefined field => field implicitly defined as Varies: PDQ-4
        //-----------------------------------
        //Test repetition
        assertNull(msg1["QPD"][4](0)[1].value)

        //Primitive value can be accessed either as element or as subelement 1
        //Here QPD-4(0)-4 returns the same as QPD-4(0)-4-1
        assertEquals("HIMSS2006", msg1["QPD"][4](0)[4].value)
        assertEquals("HIMSS2006", msg1["QPD"][4](0)[4][1].value)

        // Index out of bounds on accessing further (non-existing) subelements
        // assertNull(msg1["QPD"][4](0)[4][2].value)
        // assertNull(msg1["QPD"][4](3)[4].value)
        assertNull(msg1["QPD"][4](3)[1].value)


    }

}