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

package org.openehealth.ipf.modules.hl7.kotlin.validation.model

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.validation.ValidationException
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.openehealth.ipf.modules.hl7.kotlin.loadHl7
import kotlin.test.assertEquals

/**
 * @author Christian Ohr
 */
class LambdaPrimitiveTypeRuleTest {

    val context = DefaultHapiContext()
    val msg: Message = loadHl7(context, "/msg-01.hl7")

    @Test
    fun testCorrect() {
        val rule = LambdaPrimitiveTypeRule({ _ -> emptyArray() })
        rule.omitLeadingWhitespace = true
        assertEquals("trimmed  ", rule.correct("   \ntrimmed  "))
        rule.omitTrailingWhitespace = true
        assertEquals("trimmed", rule.correct("   \ntrimmed  "))
    }

    @Test
    fun test() {
        val rule = LambdaPrimitiveTypeRule({
            if (it == null || it.length > 10)
                arrayOf(ValidationException("Too long or null")) else
                emptyArray()
        })
        rule.omitLeadingWhitespace = true
        rule.omitTrailingWhitespace = true
        assertTrue(rule.apply("       \ntrimmed").isEmpty())
        rule.omitLeadingWhitespace = false
        rule.omitTrailingWhitespace = false
        assertFalse(rule.apply("       \ntrimmed").isEmpty())
    }
}