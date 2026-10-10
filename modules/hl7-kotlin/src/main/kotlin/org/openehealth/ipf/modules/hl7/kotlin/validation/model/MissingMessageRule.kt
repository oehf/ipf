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

import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.validation.ValidationException
import ca.uhn.hl7v2.validation.impl.AbstractMessageRule
import org.openehealth.ipf.modules.hl7.kotlin.get
import org.openehealth.ipf.modules.hl7.kotlin.value

/**
 * @author Christian Ohr
 * @since 3.5
 */
class MissingMessageRule(private val description: String = "", private val sectionReference: String = "") : AbstractMessageRule() {

    override fun getSectionReference(): String = sectionReference
    override fun getDescription(): String = description

    // Reads MSH-9 directly instead of using Message.eventType/triggerEvent, which throw if MSH-9 is
    // empty: a message without message type must be reported as validation error, not as exception
    override fun apply(msg: Message): Array<ValidationException> {
        val msh9 = msg["MSH"][9]
        return arrayOf(
                ValidationException("Message rule required for ${msh9[1].value}^${msh9[2].value} (${msg.version}), but none could be found. Probably the message type is not allowed in this context")
        )
    }
}