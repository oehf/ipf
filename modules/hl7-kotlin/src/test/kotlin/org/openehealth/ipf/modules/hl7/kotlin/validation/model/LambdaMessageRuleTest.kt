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
import org.junit.Test
import org.openehealth.ipf.modules.hl7.kotlin.get
import org.openehealth.ipf.modules.hl7.kotlin.loadHl7
import org.openehealth.ipf.modules.hl7.kotlin.value
import kotlin.test.assertEquals

/**
 * @author Christian Ohr
 */
class LambdaMessageRuleTest {

    val context = DefaultHapiContext()
    val msg: Message = loadHl7(context, "/msg-01.hl7")

    @Test
    fun testLambda() {

        // Event time must start with '20'
        val lambda1: (Message) -> Array<ValidationException> = {
            val msgDate = it["EVN"][2].value
            if (msgDate != null && msgDate.startsWith("20"))
                emptyArray() else
                arrayOf(ValidationException("baah"))
        }
        val rule1 = LambdaMessageRule(lambda1)
        assertEquals(0, rule1.test(msg).size)

        // Event time must start with '30' - fails.
        val lambda2: (Message) -> Array<ValidationException> = {
            val msgDate = it["EVN"][2].value
            if (msgDate != null && msgDate.startsWith("30"))
                emptyArray() else
                arrayOf(ValidationException("baah"))
        }
        val rule2 = LambdaMessageRule(lambda2)
        val result = rule2.test(msg)
        assertEquals(1, result.size)
        assertEquals("baah", result[0].message)
    }
}