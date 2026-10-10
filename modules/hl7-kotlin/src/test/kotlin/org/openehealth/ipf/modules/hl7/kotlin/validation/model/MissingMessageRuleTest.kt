/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.modules.hl7.kotlin.validation.model

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.model.Message
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.openehealth.ipf.modules.hl7.kotlin.makeHl7

class MissingMessageRuleTest {

    private val context = DefaultHapiContext()

    @Test
    fun testMessageType() {
        val msg: Message = makeHl7(context, "MSH|^~\\&|A|B|C|D|20050915174948||ADT^A01|1|P|2.5\r")!!
        val result = MissingMessageRule().apply(msg)
        assertEquals(1, result.size)
        assertTrue(result[0].message!!.contains("ADT^A01 (2.5)"))
    }

    @Test
    fun testMissingMessageType() {
        val msg: Message = makeHl7(context, "MSH|^~\\&|A|B|C|D|20050915174948||ADT^A01|1|P|2.5\r")!!
        msg.msh9Clear()
        val result = MissingMessageRule().apply(msg)
        assertEquals(1, result.size)
        assertTrue(result[0].message!!.contains("null^null (2.5)"))
    }

    private fun Message.msh9Clear() = (get("MSH") as ca.uhn.hl7v2.model.Segment).getField(9, 0).clear()
}
