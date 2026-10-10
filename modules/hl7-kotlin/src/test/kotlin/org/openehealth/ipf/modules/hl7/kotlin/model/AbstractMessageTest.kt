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

package org.openehealth.ipf.modules.hl7.kotlin.model

import ca.uhn.hl7v2.model.Structure
import ca.uhn.hl7v2.model.v25.segment.EVN
import ca.uhn.hl7v2.model.v25.segment.MSH
import ca.uhn.hl7v2.model.v25.segment.NK1
import ca.uhn.hl7v2.model.v25.segment.OBX
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AbstractMessageTest {

    class TestMessage : AbstractMessage() {
        override fun structures(structures: Map<Class<out Structure>, Cardinality>): Map<Class<out Structure>, Cardinality> =
                linkedMapOf(
                        MSH::class.java to Cardinality.REQUIRED,
                        EVN::class.java to Cardinality.OPTIONAL,
                        NK1::class.java to Cardinality.REQUIRED_REPEATING,
                        OBX::class.java to Cardinality.OPTIONAL_REPEATING)
    }

    @Test
    fun testCardinalities() {
        val message = TestMessage()
        assertTrue(message.isRequired("MSH"))
        assertFalse(message.isRepeating("MSH"))
        assertFalse(message.isRequired("EVN"))
        assertFalse(message.isRepeating("EVN"))
        assertTrue(message.isRequired("NK1"))
        assertTrue(message.isRepeating("NK1"))
        assertFalse(message.isRequired("OBX"))
        assertTrue(message.isRepeating("OBX"))
    }
}
