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
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Christian Ohr
 */
class MessageTest {

    val context = DefaultHapiContext()
    private val msg1: Message = loadHl7(context, "/msg-01.hl7")
    private val msg2: Message = loadHl7(context, "/msg-04.hl7")
    private val msg3: Message = loadHl7(context, "/msg-03.hl7")
    private val msg4: Message = loadHl7(context, "/msg-08.hl7")
    private val msg5: Message = loadHl7(context, "/msg-05.hl7")

    @Test
    fun testCopy() {
        msg1["MSH"][5] = "X"
        val copy = msg1.copy()
        assertEquals("X", copy["MSH"][5].value)
        copy["MSH"][5] = "Y"
        assertEquals("Y", copy["MSH"][5].value)
        assertEquals("X", msg1["MSH"][5].value)
    }

    @Test
    fun testCopyMessage1() {
        //def msg1Copy = msg1.empty()
        //Messages.copyMessage(msg1, msg1Copy)
        val msg1Copy = msg1.copy()
        assertEquals(msg1.toString(), msg1Copy.toString())
    }

    @Test
    fun testCopyMessage2() {
        val msg2Copy = msg2.copy()
        assertEquals(msg2.toString(), msg2Copy.toString())
    }

    @Test
    fun testCopyMessageWithNonStandardSegments1() {
        val msg3Copy = msg3.copy()
        assertEquals(msg3.toString(), msg3Copy.toString())
    }

    @Test
    fun testCopyMessageWithNonStandardSegments2() {
        val msg4Copy = msg4.copy()
        assertEquals(msg4.toString(), msg4Copy.toString())
    }

    @Test
    fun testResponse22() {
        val response: Message = msg1.respond("ACK")
        assertEquals("ACK^A01", response["MSH"][9].encode())
    }

    @Test
    fun testResponse25() {
        val response: Message = msg3.respond("ACK")
        assertEquals("ACK^T01", response["MSH"][9].encode())
    }

    @Test
    fun testResponse25StructureName() {
        val response: Message = msg5.respond("RSP", "K22")
        assertEquals("RSP^K22^RSP_K21", response["MSH"][9].encode())
    }

    @Test
    fun testMakeMessage22() {
        val msg = newMessage(context, "ADT", "A04", "2.2")
        assertEquals("ADT^A04", msg["MSH"][9].encode())
        assertEquals("ADT", msg.eventType)
        assertEquals("A04", msg.triggerEvent)
        assertEquals("2.2", msg.version)
        assertEquals("ADT_A04", msg.messageStructure)
    }

    @Test
    fun testMakeMessage25() {
        val msg = newMessage(context, "ADT", "A04", "2.5")
        assertEquals("ADT^A04^ADT_A01", msg["MSH"][9].encode())
        assertEquals("ADT", msg.eventType)
        assertEquals("A04", msg.triggerEvent)
        assertEquals("2.5", msg.version)
        assertEquals("ADT_A01", msg.messageStructure)
    }

    @Test
    fun testUnknownMessage() {
        assertEquals("ABC", msg2.eventType)
        assertEquals("XYZ", msg2.triggerEvent)
    }

}