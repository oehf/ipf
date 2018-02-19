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
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @author Christian Ohr
 */
class DoubleQuoteTest {

    val context = DefaultHapiContext()
    val msg: Message = loadHl7(context, "/msg-07.hl7")
    val streetAddress = msg["PID"][11](0)[1]
    val address = msg["PID"][11]

    @Test
    fun testGet() {
        // property access on target
        assertEquals( "", address.value2)
        assertEquals( "\"\"", address.value)

        assertEquals( "", streetAddress.value2)
        assertEquals( "\"\"", streetAddress.value)

        assertEquals( "", streetAddress[1].value2)
        assertEquals( "\"\"", streetAddress[1].value)

        assertTrue(address.nullValue)
        assertTrue(streetAddress.nullValue)
        assertTrue(streetAddress[1].nullValue)
    }

}