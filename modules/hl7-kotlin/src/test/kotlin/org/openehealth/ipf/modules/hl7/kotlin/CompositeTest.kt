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
import ca.uhn.hl7v2.model.v22.datatype.CE
import ca.uhn.hl7v2.model.v22.message.ADT_A01
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author Christian Ohr
 */
class CompositeTest {

    val context = DefaultHapiContext()
    val msg: ADT_A01 = loadHl7(context, "/msg-01.hl7")
    private val composite = msg["NK1"](0)[4]


    @Test
    fun testGetAt() {
        Assertions.assertEquals("NW", composite[4].value)
    }

    @Test
    fun testEncode() {
        Assertions.assertEquals("Irgendwo 23^^Foo^NW^11000^DE", composite.encode())
    }

    @Test
    fun testGetEmptyAt() {
        Assertions.assertEquals(null, msg["PID"][9](0)[1].value)
        Assertions.assertEquals(null, msg["NK1"](0)[21].value)
        Assertions.assertEquals(null, msg["PV2"][2][1].value)
    }

    @Test
    fun testAsBoolean() {
        Assertions.assertEquals("args", msg["PID"][9](0)[1].value ?: "args")
        Assertions.assertEquals("args", msg["NK1"](0)[21].value ?: "args")
        Assertions.assertEquals("args", msg["PV2"][2][1].value ?: "args")
    }

    @Test
    fun testFrom() {
        val compositeCopy = composite.message.copy()["NK1"](0)[4]
        composite[4] = "XY"
        Assertions.assertEquals("NW", compositeCopy[4].value)
        compositeCopy.from(composite)
        Assertions.assertEquals("XY", compositeCopy[4].value)
    }

    @Test
    fun testSetAt() {
        // set via string
        composite[4] = "NX"
        Assertions.assertEquals("NX", composite[4].value)

        // set via primitive adapter
        composite[4] = composite[5]
        Assertions.assertEquals("11000", composite[4].value)
    }

    @Test
    fun testMakeComposite() {
        val ce = newComposite("CE", msg)
        assertTrue(ce is CE)
    }


    @Test
    fun testDestructuring() {
        val (street, _, _, _, _, country) = composite
        Assertions.assertEquals("Irgendwo 23", street.value)
        Assertions.assertEquals("DE", country.value)
    }
}